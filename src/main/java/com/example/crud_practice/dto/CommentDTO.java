package com.example.crud_practice.dto;

import com.example.crud_practice.entity.CommentEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@ToString
public class CommentDTO {
    private final Long id;
    private final String commentWriter;
    private final String commentContents;
    private final Long boardId;
    private final LocalDateTime commentCreatedTime;

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