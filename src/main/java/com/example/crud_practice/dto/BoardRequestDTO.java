package com.example.crud_practice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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

    // ❗final을 사용하지 않음:
    // - Spring이 MultipartFile을 바인딩하기 위해 setter 필요
    // - 파일 이름 및 첨부 여부는 업로드 후 서버에서 동적으로 설정됨
    private List<MultipartFile> boardFile; // save.html → Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 저장용 파일 이름 (.uploads/storedFileName)
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)
}
