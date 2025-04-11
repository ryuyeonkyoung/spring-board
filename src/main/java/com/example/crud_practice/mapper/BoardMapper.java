package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.BoardPageResponse;
import com.example.crud_practice.entity.BoardEntity;
import lombok.extern.slf4j.Slf4j;

// TODO: MapStruct로 리팩터링 예정 (현재는 수동 매핑 + 예외 처리 포함)
@Slf4j
public class BoardMapper {
    public static BoardPageResponse toOffsetPageDTO(BoardEntity board) {
        try {
            return new BoardPageResponse(
                    board.getId(),
                    board.getBoardWriter(),
                    board.getBoardTitle(),
                    board.getBoardHits(),
                    board.getCreatedTime()
            );
        } catch (Exception e) {
            log.error("BoardPageResponse 매핑 실패: boardEntity = {}", board, e);
            throw e;
        }
    }

    public static BoardPageResponse toCursorPageDTO(BoardEntity board) {
        try {
            return new BoardPageResponse(
                    board.getId(),
                    board.getBoardWriter(),
                    board.getBoardTitle(),
                    board.getBoardHits(),
                    board.getCreatedTime()
            );
        } catch (Exception e) {
            log.error("BoardPageResponse 매핑 실패: boardEntity = {}", board, e);
            throw e;
        }
    }
}