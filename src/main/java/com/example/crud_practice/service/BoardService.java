package com.example.crud_practice.service;

import com.example.crud_practice.dto.BoardDTO;
import com.example.crud_practice.entity.BaseEntity;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    private final BoardRepository boardRepository;

    // dto->entity 해서 repository에 저장
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity); //entity타입으로 받음
    }

    //dto를
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList = boardRepository.findAll(); //데이터베이스에 있는 모든 엔티티를 리스트 형태로 반환
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity: boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity)); //조회된 각각의 BoardEntity 객체를 BoardDTO로 변환해서 리스트 형태로 만듦
        }
        return boardDTOList;
    }

    @Transactional //하나의 트랜잭션에서 작동하게 하는 애노테이션. 원자성을 띈다(실패하면 롤백)
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity); //dto로 변환
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        //update메소드가 따로 내장되지 않음. save메소드로 update도 하고 insert도 함
        //insert는 id없고 update는 id있음
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId()); //코드 겹쳐서 그냥 이거 이용
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }
}
