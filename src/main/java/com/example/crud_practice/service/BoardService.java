package com.example.crud_practice.service;

import com.example.crud_practice.dto.BoardDTO;
import com.example.crud_practice.entity.BaseEntity;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import com.example.crud_practice.repository.BoardFileRepository;
import com.example.crud_practice.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

//데이터 전송 관리
//entity와 dto를 주고받는 역할
// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    // 전통적인 의존성 구조 - DiaryService가 JpaBoardRepository같은 구체적인 구현체에 의존
    // DIP - DiaryService가 추상화(인터페이스 DiaryRepository)에 의존
    // 효과 - repository에 의존하지만 JpaBoardRepository가 교체되어도 코드를 변경할 필요가 없다
    private final BoardRepository boardRepository;
    private final BoardFileRepository boardFileRepository;

    // dto->entity 해서 repository에 저장
    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리
        if (boardDTO.getBoardFile().isEmpty()) {
            // 1. 첨부 파일 없음.
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); // dto->entity
            boardRepository.save(boardEntity); // repository에 저장
        } else {
            // 2. 첨부 파일 있음.
        /*
            1. DTO에 담긴 파일을 꺼냄
            2. 파일의 이름 가져옴
            3. 서버 저장용 이름을 만듦
            // 내사진.jpg => 839798375892_내사진.jpg
            4. 저장 경로 설정
            5. 해당 경로에 파일 저장
            6. board_table에 해당 데이터 save 처리
            7. board_file_table에 해당 데이터 save 처리
         */
            // 자식 데이터가 여러개 있을 수 있는 상황이라 부모데이터 먼저 나와야함.
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO); // dto -> entity. db에 저장하기 전이라 id값이 없다.
            Long savedId = boardRepository.save(boardEntity).getId(); // 엔티티를 데이터베이스에 저장하고, 생성된 기본 키 값을 얻음
            BoardEntity board = boardRepository.findById(savedId).get(); // 1에서 저장된 기본 키를 사용하여 엔티티를 다시 조회
            for (MultipartFile boardFile: boardDTO.getBoardFile()) {
                // 원래 이걸로 단일파일 꺼내고 있었는데 반복문으로 하나씩 꺼내서 필요없어짐.
//                MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
                String originalFilename = boardFile.getOriginalFilename(); // 2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; // 3.
                String savePath = "C:/springboot_img/" + storedFileName; // 4. C:/springboot_img/9802398403948_내사진.jpg
//            String savePath = "/Users/사용자이름/springboot_img/" + storedFileName; // C:/springboot_img/9802398403948_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5.

                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFileRepository.save(boardFileEntity);
            }
        }
    }

    @Transactional //하나의 트랜잭션에서 작동하게 하는 애노테이션. 원자성을 띈다(실패하면 롤백)
    public List<BoardDTO> findAll() {
         /*
            1. repository에서 entitiy를 리스트 형태로 가져온다.
            2. entitylist를 dtolist로 바꾼다.
            3. dtolist를 반환한다.
         */
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            // entity를 dto로 변환.
            // 여기서 boardFileEntity도 접근하니까 @Transactional 이노테이션이 필요함.
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    // save메소드로 update도 하고 insert도 한다.
    // update는 id save와 달리 id 필요
    // controller는 service에서 (공개)메소드만 사용. 직접 데이터베이스나 엔티티에 접근하지 않는다.
    public BoardDTO update(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId()); //코드 겹쳐서 그냥 이거 이용
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() - 1; //0부터 시작해서
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작
        // findall은 리스트객체지만 얘는 page객체. 그래야 메소드 사용가능
        Page<BoardEntity> boardEntities =
                boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id")));

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createdTime
        //map 메소드로 entity를 BoardDTO로 바꿔줌
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(), board.getBoardHits(), board.getCreatedTime()));
        return boardDTOS;
    }
}
