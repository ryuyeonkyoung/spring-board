package com.example.crud_practice.service;

import com.example.crud_practice.dto.BoardDTO;
import com.example.crud_practice.entity.BaseEntity;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

//데이터 전송 관리
//entity와 dto를 주고받는 역할
// DTO -> Entity (Entity Class)
// Entity -> DTO (DTO Class)
@Service
@RequiredArgsConstructor
public class BoardService {
    // 1. DTO를 엔티티로 변환 (변환 과정 생략)
    private final BoardRepository boardRepository;

    // 2. Repository를 통해 엔티티를 저장
    public void save(BoardDTO boardDTO) {
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity); //entity타입으로 받음
    }
}
