package com.example.crud_practice.service;

import com.example.crud_practice.dto.BoardDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import com.example.crud_practice.exception.ResourceNotFoundException;
import com.example.crud_practice.repository.BoardFileRepository;
import com.example.crud_practice.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.logging.Logger;

/**
 * 게시판 도메인의 서비스 계층
 * - 비즈니스 로직 처리 담당
 * - 파일 첨부 여부에 따라 저장 방식 분기
 * - Entity ↔ DTO 변환 및 트랜잭션 제어 포함
 *
 * @Transactional 적용 기준:
 * ✅ 붙여야 하는 경우
 * - 변경 감지가 필요한 경우 (dirty checking)
 * - 여러 쿼리를 하나로 묶어 실패시 롤백이 필요한 경우
 * Lazy 로딩 필드를 조회할 때 (@OneToMany, 따로 설정한 부분)
 *
 * ⚠ 생략 가능한 경우
 * - 단순 조회 (생략하거나 @Transactional(readOnly = true) 설정)
 * - 단건 저장/수정 (dirty checking 필요 없음)
 * - 클래스 전체가 읽기 전용인 경우 (클래스에 @Transactional(readOnly = true) 설정)
 *
 * 참고: https://tech.kakaopay.com/post/jpa-transactional-bri/
*/
@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private static final Logger logger = Logger.getLogger(BoardService.class.getName());

    // DIP 적용: 인터페이스에만 의존하고, 구현체는 Spring이 주입(DI)
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    /**
     * 게시글 저장 처리
     * - 첨부파일 여부에 따라 저장 방식 분기
     * - 게시글이 먼저 저장되어야 자식 파일과의 연관관계 설정 가능
     * - 영속성 컨텍스트 특성상 save 메서드 하나로 insert, update 모두 가능
     */
    public void save(BoardDTO boardDTO) throws IOException {
        // [Case 1] 첨부파일이 없는 경우 → 본문만 저장
        if (boardDTO.getBoardFile().isEmpty()) {
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
            boardRepository.save(boardEntity);
        }
        // [Case 2] 첨부파일이 있는 경우 → 게시글 + 파일 메타데이터 저장
        else {
            /*
             * 첨부파일 저장 처리 순서:
             * - 자식 엔티티(BoardFile)가 부모(Board)를 참조해야 하므로, 부모를 먼저 저장해 ID 확보 필요
             *
             * 1. (부모) 게시글 저장 → ID 생성
             * 2. (조회) ID를 기반으로 게시글 다시 조회 → 영속성 컨텍스트에서 관리됨
             * 3. (자식) 각 파일 저장 → 게시글 ID를 외래키로 연결해서 저장
             *
             * 설계 포인트:
             * - 파일명 충돌 방지를 위해 timestamp 기반 파일명 생성
             * - 실제 파일은 로컬 경로에 저장하고, 파일 정보(BoardFileEntity)만 DB에 저장
             */

            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId)
                    .orElseThrow(() -> {
                        log.error("게시글 저장 후 조회 실패: ID = {}", savedId);
                        return new ResourceNotFoundException("게시글 저장 후 조회 실패: id = " + savedId);
                    });

            for (MultipartFile boardFile: boardDTO.getBoardFile()) {
                String originalFilename = boardFile.getOriginalFilename();
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + storedFileName;

                boardFile.transferTo(new File(savePath));

                BoardFileEntity boardFileEntity =
                        BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);

                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    /**
     * 게시글 전체 조회
     * - Entity 목록을 DTO로 변환하여 반환
     * - Controller 계층에는 Entity를 직접 노출하지 않음
     * - Lazy 로딩 대응을 위해 트랜잭션 범위 유지
     */
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    /**
     * 게시글 단건 조회
     * - ID 기반으로 게시글 조회 후 DTO로 변환
     * - 파일 정보 포함 시 Lazy 로딩 대응을 위해 트랜잭션 필요
     * - Optional.orElseThrow()를 사용해 값이 반드시 있어야 한다는 코드의 의도를 드러냄.
     */
    @Transactional
    public BoardDTO findById(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("게시글 조회 실패: ID = {}", id);
                    return new ResourceNotFoundException("게시글 찾을 수 없음: id =" + id);
                }); // 예외처리 : throw + optional (null 가능해서)
        return BoardDTO.toBoardDTO(boardEntity);
    }

    /**
     * 게시글 조회수 증가 처리
     * - 직접 JPQL로 update 실행
     */
    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    /**
     * 게시글 수정 처리
     * - ID가 존재하는 경우 save → update로 동작
     * - 수정 후 최신 게시글 DTO 반환
     */
    @Transactional
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    /**
     * 게시글 삭제 처리
     * - ID 기준으로 게시글 삭제
     */
    @Transactional
    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    /**
     * 게시글 목록 페이징 처리
     * - PageRequest 기반으로 요청 페이지 추출
     * - Entity → DTO 변환 후 Page 객체로 반환
     * - 전체 게시글 수, 페이지 수 등 함께 전달 가능
     */
    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //0부터 시작
        int pageLimit = 3;

        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
