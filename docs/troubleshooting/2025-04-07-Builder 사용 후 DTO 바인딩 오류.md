📅 작성일: 2025.04.07<br>
🔧 주제: @Builder 사용 DTO에서 Spring 요청 바인딩 오류 발생

---

### 💡 상황

게시글 저장 기능 구현 중, 클라이언트의 폼 데이터를 `BoardRequestDTO`로 받아 처리하려 할 때  
Spring이 DTO 생성자 바인딩에 실패하며 `IllegalStateException`이 발생함.

- 게시판 등록 기능에서 `BoardRequestDTO`를 요청 파라미터 수신 객체로 사용
- DTO 리팩토링 과정에서 모든 필드를 `final`로 선언하고 `@Builder`를 사용
- Post 요청 시 내부에서 `Cannot resolve parameter names for constructor` 예외 발생

---

### 🔍 원인 분석

- `BoardRequestDTO`에 모든 필드를 `final`로 선언하고,  
  `@Builder`만 사용하면서 기본 생성자와 setter가 없는 구조였음

- Spring의 요청 파라미터 자동 바인딩은  
  기본 생성자 + setter 방식 또는 생성자에 파라미터 이름 정보가 포함되어야 함

- 하지만 `@Builder`는 런타임에 파라미터 이름 정보를 제공하지 않기 때문에  
  Spring이 어떤 값이 어떤 생성자 인자에 대응되는지 판단할 수 없음

- 즉, DTO 구조가 자동 바인딩이 불가능한 형태였음

---

### ✅ 해결 방법

- `BoardRequestDTO`를 **요청 전용 DTO**로 명확히 역할 분리
- 기존 `BoardDTO` → `BoardRequestDTO`로 클래스 이름을 변경하여  
  **요청 DTO임을 명시적으로 드러내고 역할을 분리함**
- 모든 `final` 제거
- `@NoArgsConstructor`, `@Setter`를 추가해 Spring이 기본 생성자 + setter 방식으로 바인딩할 수 있도록 변경

```java

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDTO {
    private Long id;
    private String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private List<MultipartFile> boardFile;
    private List<String> originalFileName;
    private List<String> storedFileName;
    private int fileAttached;
}
```

- 응답용 DTO인 `BoardSummaryDTO`는 기존처럼 `final` + `@Builder` 구조 유지

---

### ✍️ 회고 및 배운 점

- **요청용 DTO는 기본 생성자 + setter 기반으로 설계해야** Spring의 자동 바인딩이 정상 동작함
- `@Builder`는 자동 바인딩에 사용되지 않으며, 내부 조립이나 응답 객체 조합에서만 유효
- 모든 상황에 하나의 DTO를 쓰기보다는  
  **요청용 / 응답용 / 내부 변환용 DTO를 역할별로 나누는 설계가 유지보수에 유리함**
- 실무에서는 DTO 구조만으로도 바인딩 실패나 런타임 오류가 발생할 수 있으므로,  
  각 어노테이션의 동작 방식과 바인딩 흐름을 명확히 이해하는 것이 중요함

---

### 📎 관련 링크 / 참고 자료

- https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods/arguments.html#model-attribute-method-argument
- https://stackoverflow.com/questions/46782915/spring-modelattribute-cannot-resolve-parameter-names
- https://projectlombok.org/features/Builder