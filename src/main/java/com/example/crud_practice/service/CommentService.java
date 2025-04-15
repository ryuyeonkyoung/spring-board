package com.example.crud_practice.service;

import com.example.crud_practice.dto.CommentDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.CommentEntity;
import com.example.crud_practice.exception.CommentSaveException;
import com.example.crud_practice.mapper.CommentMapper;
import com.example.crud_practice.repository.BoardRepository;
import com.example.crud_practice.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final CommentMapper commentMapper;

    // 댓글 저장
    public Long save(CommentDTO commentDTO) {
        // 1. 게시글 존재 여부 확인 (게시글이 존재해야 댓글 저장 가능)
        // Optional을 보다 안전하게 null을 처리함
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());

        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();

            // 2. 댓글 엔티티 생성 후 저장
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId(); // 저장된 댓글의 ID 반환
        } else {
            // 예외처리 : throw + optional (null 가능해서)
            log.error("댓글 저장 실패: 게시글이 존재하지 않음, ID = {}", commentDTO.getBoardId());
            throw new CommentSaveException("댓글 찾을 수 없음: ID = " + commentDTO.getBoardId());
        }
    }

    // 특정 게시글의 모든 댓글 조회 (내림차순 정렬)
    public List<CommentDTO> findAll(Long boardId) {
        // 1. 게시글 조회
        BoardEntity boardEntity = boardRepository.findById(boardId)
                .orElseThrow(() -> {
                    log.error("댓글 조회 실패: 게시글이 존재하지 않음, ID = {}", boardId);
                    return new IllegalArgumentException("전체 게시글 찾을 수 없음: ID = " + boardId);
                });

        // 2. 해당 게시글에 속한 댓글을 내림차순으로 조회
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);

        // 3. Entity → DTO 변환
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity : commentEntityList) {
            CommentDTO commentDTO = commentMapper.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }

        return commentDTOList;
    }
}