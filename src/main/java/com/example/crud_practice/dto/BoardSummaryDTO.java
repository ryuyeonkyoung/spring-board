package com.example.crud_practice.dto;

import lombok.*;

import java.time.LocalDateTime;

/* *
 * [설계 목적: final 필드 사용]
 * 응답 DTO를 불변 객체로 유지하기 위해 필드를 final로 선언함.
 * 모든 필드가 final이므로 @Setter은 필요 없음
 */
@Getter
@RequiredArgsConstructor
public class BoardSummaryDTO {
    private final Long id;
    private final String boardWriter;
    private final String boardTitle;
    private final int boardHits;
    private final LocalDateTime boardCreatedTime;
}