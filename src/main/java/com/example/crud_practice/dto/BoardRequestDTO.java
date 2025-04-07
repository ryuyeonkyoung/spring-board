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
@NoArgsConstructor // 요청 DTO이므로 @Setter + @NoArgsConstructor 조합 사용
//@Builder // 요청 파라미터 바인딩에는 builder의 사용이 큰 장점이 없음.
public class BoardRequestDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;
    private List<MultipartFile> boardFile; // save.html -> Controller 파일 담는 용도

    // TODO : 파일 포함 DTO와 파일 미포함 DTO로 분리 검토하기
    // ❗final을 사용하지 않음:
    // - Spring이 MultipartFile을 바인딩하기 위해 setter 필요
    // - 파일 이름 및 첨부 여부는 업로드 후 서버에서 동적으로 설정됨
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)

    // Controller에서 Entity를 직접 노출시키지 않기 위한 방법
    public static BoardRequestDTO toBoardRequestDTO(BoardEntity boardEntity) {
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
            BoardRequestDTO.setFileAttached(boardEntity.getFileAttached()); // 0
        } else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            BoardRequestDTO.setFileAttached(boardEntity.getFileAttached()); // 1
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
}
