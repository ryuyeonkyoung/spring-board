package com.example.crud_practice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

// 요청(Request) DTO: @Setter, @ToString, @NoArgsConstructor
@Getter
@Setter // 가변성을 허용해야 함
@ToString // 디버깅 및 로깅용
@NoArgsConstructor // 요청 DTO는 컨트롤러에서 바인딩 시 기본 생성자가 필요함
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

    private List<MultipartFile> boardFile; // save.html → Controller 파일 담는 용도
    private List<String> originalFileName; // 원본 파일 이름
    private List<String> storedFileName; // 저장용 파일 이름 (.uploads/storedFileName)
    private int fileAttached; // 파일 첨부 여부(첨부 1, 미첨부 0)
}
