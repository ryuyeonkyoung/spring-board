package com.example.crud_practice.dto;

import lombok.*;

import java.time.LocalDateTime;

// 모든 필드가 final이므로 @Setter 필요 없음
@Getter
@Builder
@AllArgsConstructor
public class BoardSummaryDTO {
    private final Long id;
    private final String boardWriter;
    private final String boardTitle;
    private final int boardHits;
    private final LocalDateTime boardCreatedTime;
}