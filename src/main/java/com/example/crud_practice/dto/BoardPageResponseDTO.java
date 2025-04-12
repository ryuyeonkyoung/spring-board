package com.example.crud_practice.dto;

import lombok.*;

import java.time.LocalDateTime;

// 응답(Response) DTO: final 필드 사용
@Getter
@RequiredArgsConstructor
@ToString
public class BoardPageResponseDTO {
    private final Long id;
    private final String boardWriter;
    private final String boardTitle;
    private final int boardHits;
    private final LocalDateTime boardCreatedTime;
}