package com.example.crud_practice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

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


}