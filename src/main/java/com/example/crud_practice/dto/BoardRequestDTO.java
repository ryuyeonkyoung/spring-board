package com.example.crud_practice.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

// 요청용 DTO: @Setter + @Valid
// @Builder는 @Valid와 충돌할 수 있어 사용하지 않음
@Getter
@Setter // 가변성을 허용해야 함
@NoArgsConstructor
@ToString // 디버깅 및 로깅용
public class BoardRequestDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits;
    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // save.html → Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 저장용 파일 이름 (.uploads/storedFileName)
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)
}
