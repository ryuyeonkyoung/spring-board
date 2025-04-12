package com.example.crud_practice.mapper;

import com.example.crud_practice.dto.BoardPageResponse;
import com.example.crud_practice.dto.BoardRequestDTO;
import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

// FIXME: buidler 패턴 사용 검토
// FIXME: MapStruct로 리팩터링 예정 (현재는 수동 매핑 + 예외 처리 포함)
@Slf4j
public class BoardMapper {
    public static BoardRequestDTO toBoardRequestDTO(BoardEntity boardEntity) {
        // boardEntity → BoardRequestDTO
        BoardRequestDTO BoardRequestDTO = new BoardRequestDTO();
        BoardRequestDTO.setId(boardEntity.getId());
        BoardRequestDTO.setBoardWriter(boardEntity.getBoardWriter());
        BoardRequestDTO.setBoardPass(boardEntity.getBoardPass());
        BoardRequestDTO.setBoardTitle(boardEntity.getBoardTitle());
        BoardRequestDTO.setBoardContents(boardEntity.getBoardContents());
        BoardRequestDTO.setBoardHits(boardEntity.getBoardHits());
        BoardRequestDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        BoardRequestDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());


        if (boardEntity.getFileAttached() == 0) {
            BoardRequestDTO.setFileAttached(0); // 0
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            BoardRequestDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            // FIXME: 반복문 매서드로 분리, Optional 사용 검토
            for (BoardFileEntity boardFileEntity : boardEntity.getBoardFileEntityList()) {
                originalFileNameList.add(boardFileEntity.getOriginalFileName());
                storedFileNameList.add(boardFileEntity.getStoredFileName());
            }

            // 파일 이름을 가져가야 함.
            // orginalFileName, storedFileName : board_file_table(BoardFileEntity)
            // join
            // select * from board_table b, board_file_table bf where b.id=bf.board_id
            // and where b.id=?


            // originalFileName, storedFileName: 파일 이름 정보 (board_file_table에서 join으로 조회)
            // 해당 필드들은 final이 아니므로 생성자에 포함되지 않음
            // → builder로는 세팅되지 않아 build() 이후 setter로 별도 주입
            BoardRequestDTO.setOriginalFileName(originalFileNameList);
            BoardRequestDTO.setStoredFileName(storedFileNameList);
        }

        return BoardRequestDTO;
    }

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