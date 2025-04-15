package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.BoardPageResponseDTO;
import com.example.crud_practice.dto.BoardRequestDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

// FIXME: MapStruct로 리팩터링 예정 (현재는 수동 매핑 + 예외 처리 포함)
@Slf4j
public class BoardMapper {
    public static BoardRequestDTO toBoardRequestDTO(BoardEntity boardEntity) {
        // boardEntity → BoardRequestDTO
        BoardRequestDTO boardRequestDTO = BoardRequestDTO.builder()
                .id(boardEntity.getId())
                .boardWriter(boardEntity.getBoardWriter())
                .boardPass(boardEntity.getBoardPass())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContents(boardEntity.getBoardContents())
                .boardHits(boardEntity.getBoardHits())
                .boardCreatedTime(boardEntity.getCreatedTime())
                .boardUpdatedTime(boardEntity.getUpdatedTime())
                .build();


        // final이 아닌 필드는 setter로 별도 주입
        if (boardEntity.getFileAttached() == 0) {
            boardRequestDTO.setFileAttached(0); // 0
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardRequestDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            // FIXME: 반복문 매서드로 분리, Optional 사용 검토
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }

            boardRequestDTO.setOriginalFileName(originalFileNameList);
            boardRequestDTO.setStoredFileName(storedFileNameList);
        }

        return boardRequestDTO;
    }

    public static BoardPageResponseDTO toOffsetPageDTO(BoardEntity board) {
        try {
            return new BoardPageResponseDTO(
                    board.getId(),
                    board.getBoardWriter(),
                    board.getBoardTitle(),
                    board.getBoardHits(),
                    board.getCreatedTime()
            );
        } catch (Exception e) {
            log.error("BoardPageResponseDTO 매핑 실패: boardEntity = {}", board, e);
            throw e;
        }
    }

    public static BoardPageResponseDTO toCursorPageDTO(BoardEntity board) {
        try {
            return new BoardPageResponseDTO(
                    board.getId(),
                    board.getBoardWriter(),
                    board.getBoardTitle(),
                    board.getBoardHits(),
                    board.getCreatedTime()
            );
        } catch (Exception e) {
            log.error("BoardPageResponseDTO 매핑 실패: boardEntity = {}", board, e);
            throw e;
        }
    }
}