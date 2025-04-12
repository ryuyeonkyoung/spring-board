package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.CommentDTO;
import com.example.crud_practice.entity.CommentEntity;

public class CommentMapper {
    public static CommentDTO toCommentDTO(CommentEntity commentEntity, Long boardId) {
        return CommentDTO.builder()
                .id(commentEntity.getId())
                .commentWriter(commentEntity.getCommentWriter())
                .commentContents(commentEntity.getCommentContents())
                .boardId(boardId)
                .commentCreatedTime(commentEntity.getCreatedTime())
                .build();
    }
}
