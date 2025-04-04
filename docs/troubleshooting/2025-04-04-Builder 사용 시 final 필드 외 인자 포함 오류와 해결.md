📅 작성일: 2025.04.04
<br>
🔧 주제: @Builder 사용 시 final 필드 외 인자 포함 오류
---

### 💡 상황

DTO 리팩토링 후 `@Builder` 적용 → 필드 `final`화 및 분리까지 마친 뒤 실행 중,  
생성자 인자 불일치로 인한 컴파일 오류 발생

```text
java: constructor BoardDTO in class com.example.crud_practice.dto.BoardDTO cannot be applied to given types;
  required: java.lang.Long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,int,java.time.LocalDateTime,java.time.LocalDateTime,java.util.List<org.springframework.web.multipart.MultipartFile>
  found:    java.lang.Long,java.lang.String,java.lang.String,java.lang.String,java.lang.String,int,java.time.LocalDateTime,java.time.LocalDateTime,java.util.List<org.springframework.web.multipart.MultipartFile>,java.util.List<java.lang.String>,java.util.List<java.lang.String>,int
  reason: actual and formal argument lists differ in length
```

---

### 🔍 원인 분석

- `@RequiredArgsConstructor`는 **`final` 필드만 포함한 생성자**를 생성함
- `@Builder`는 객체를 만들기 위해 **모든 필드를 인자로 전달**하려고 시도함
- 하지만 클래스에는 `final` 필드만 받는 생성자만 정의되어 있었고,  
  컴파일러는 그에 맞는 생성자를 찾지 못해 **인자 수 mismatch 에러** 발생

```java
// BoardDTO.java
@Getter
@Setter
@ToString
@Builder
@RequiredArgsConstructor // 모든 필드가 필수적이라는 의도를 전달하기 위해 사용
public class BoardDTO {
    private final Long id;
    private final String boardWriter;
    private final String boardPass;
    private final String boardTitle;
    private final String boardContents;
    private final int boardHits;
    private final LocalDateTime boardCreatedTime;
    private final LocalDateTime boardUpdatedTime;
    private final List<MultipartFile> boardFile;

    private List<String> originalFileName;
    private int fileAttached;
    ...
```

```java
// builder 내부
public BoardDTO build() {
    return new BoardDTO(...); // ← 여기서 문제가 터짐
}
```

---

### ✅ 해결 방법

- `@Builder`를 **직접 생성자에 붙여**, 필요한 final 필드만 대상으로 명시함
- 생성자에 포함되지 않은 필드(originalFileName 등)는  
  **setter를 통해 build() 이후 주입**
- 이를 통해:
    * BoardDTO의 불변성 의도 유지 가능
    * 불변 필드는 생성 시점에 고정, 가변 필드는 파일 유무에 따라 유연하게 설정 가능

```java

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
```