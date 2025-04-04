package com.example.crud_practice.dto;

import com.example.crud_practice.entity.BoardEntity;
import com.example.crud_practice.entity.BoardFileEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
//@AllArgsConstructor
public class BoardDTO {
    private final Long id;
    private final String boardWriter;
    private final String boardPass;
    private final String boardTitle;
    private final String boardContents;
    private final int boardHits;
    private final LocalDateTime boardCreatedTime;
    private final LocalDateTime boardUpdatedTime;
    private final List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도

    // TODO : 파일 포함 DTO와 파일 미포함 DTO로 분리 검토하기
    // ❗final을 사용하지 않음:
    // - Spring이 MultipartFile을 바인딩하기 위해 setter 필요
    // - 파일 이름 및 첨부 여부는 업로드 후 서버에서 동적으로 설정됨
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    /**
     * [설계 목적: Builder + 불변 필드 조합]
     * <p>
     * DTO를 불변 객체로 유지하기 위해 주요 필드를 final로 선언하고,
     *
     * @Builder는 필요한 필드만 받을 수 있도록 생성자에 직접 정의함.
     * <p>
     * 이렇게 설계한 이유:
     * - @AllArgsConstructor 사용 시: 불변이 아닌 필드까지 생성자에 포함되어 설계가 불명확해짐
     * - @RequiredArgsConstructor 사용 시: Builder가 필요한 전체 필드와 매칭되지 않아 오류 발생
     * - 따라서 Builder 대상 생성자를 직접 작성하여 의도를 명확히 표현하고 오류를 방지함
     * <p>
     * 주의: 이 방식은 Builder 대상 필드만 생성자에 포함되므로,
     * 나머지 비필수/동적 필드는 setter로 따로 주입해야 함
     */

    @Builder
    public BoardDTO(Long id, String boardWriter, String boardPass, String boardTitle, String boardContents, int boardHits, LocalDateTime boardCreatedTime, LocalDateTime boardUpdatedTime, List<MultipartFile> boardFile) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardPass = boardPass;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
        this.boardUpdatedTime = boardUpdatedTime;
        this.boardFile = boardFile;
    }

    public static BoardDTO toBoardDTO(BoardEntity boardEntity) {
        BoardDTO boardDTO = BoardDTO.builder()
                .id(boardEntity.getId())
                .boardWriter(boardEntity.getBoardWriter())
                .boardPass(boardEntity.getBoardPass())
                .boardTitle(boardEntity.getBoardTitle())
                .boardContents(boardEntity.getBoardContents())
                .boardHits(boardEntity.getBoardHits())
                .boardCreatedTime(boardEntity.getCreatedTime())
                .boardUpdatedTime(boardEntity.getUpdatedTime())
                .build();

        if (boardEntity.getFileAttached() == 0) {
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            boardDTO.setFileAttached(boardEntity.getFileAttached()); // 1
            for (BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList() ) {
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
            boardDTO.setOriginalFileName(originalFileNameList);
            boardDTO.setStoredFileName(storedFileNameList);
        }

        return boardDTO;
    }
}
