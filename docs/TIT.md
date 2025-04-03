## 📝 TIT (Today I Think)

`/docs/TIT.txt`는 매일 개발 과정에서의 배운 점, 고민한 내용, 적용 아이디어 등을 자유롭게 기록한 개인 메모입니다.
학습 흐름을 자연스럽게 쌓아가기 위해, **초기에는 큰 틀 없이 자유롭게 작성**하며,
**데이터가 충분히 모이면 주제별로 분리**하거나 **velog 포스트로 정리할 예정입니다.**

## DATE: 2025-04-03

### 리팩토링 고려 요소 정리

#### 리팩토링 우선순위 정하기 : 하지 않으면 다음 단계가 불가능한거 / 하지 않으면 배포 불가능한거 / 배포 후 해도 되는거

예외처리 : null만 체크하는 코드 찾기 > 어떻게 개선할지 판단하기 (checked vs unchecked) > 적용
로깅 도입 : 우선 println 다 지우기 > logging이 필요한 부분 판단하기 > 어떤 도구 사용할지 정하기(SLF4J + Logback) > 적용
비즈니스 로직 분리 : 다른 개발자들의 기술 블로그 읽어보기 > 공통적으로 판단하는 기준 추출하기 + 프로젝트 상황에 맞는 기준 분류 > 내 프로젝트에 맞게 적용
DTO Entity 변환 분리 : 왜 필요한지 알아보기 >
클래스 제목 : (코드 배껴 쓰느라 기준이 섞여있음) 다른 github 프로젝트들 찾아보기 > 클래스명 짓는 기준 분류하기(장단점도 조사) > 내 프로젝트에 적용할 클래스 작명 기준 정하기 > 적용
페이징 처리 개선 : 현재 적용된 페이징이 offset인지 cursor인지 다른건지 파악 > 첫 페지는 offset + 다른 페이지는 cursor 적용해보기
트랜잭션 관리 : 보수적으로 트랜잭션 적용해보기 > 성능을 최대화하기 위해 어디까지 트랜잭션을 해제할 수 있는지 적용해보기 > 둘다 성능 테스트 해보기
테스트 코드 작성 : 프로젝트 규모나 특성별로 사용하는 테스트 방법론 알아보기 > 테스트 기준 정하기 > 테스트 툴별 장단점 조사하기 > 테스트 툴 정하기(JUnit5 + Mockito /
SpringBootTest + TestRestTemplate/MockMvc)

#### 우선순위 분류

1. 배포 불가능 + 당장 해야함 : 최소한의 예외 처리, 로깅. 트랜잭션
2. 배포 불가능 + 당장은 아님 : 테스트 코드, 페이징 처리 개선, 예외 처리
3. 배포 후 가능 : 비즈니스 로직 분리, DTO Entity 변환 분리, 클래스명 정리

---

### 계층별 예외 처리

**Service 계층**
-> 예외가 발생할 수 있는 I/O나 조회 실패만 예외처리, 단순 저장 로직(save())는 예외 처리를 따로 하지 않아도 된다.

- 비즈니스 로직 실패 시 -> throw new CustomException(...)
- Optional<T> 반환 메서드 (findById) → 반드시 orElseThrow() 사용
- 내부 로직에서만 쓸거면 null도 사용 가능
  **Controller 계층**
- @RestController 환경에서는 예외 발생 시 → @ControllerAdvice가 JSON 에러 응답 자동 처리
- 뷰 반환이 아닌 API 응답이라면, HTTP 상태 코드 + 에러 메시지 명확히 설정 필요
  **예외 클래스**
- 실무에서는 대부분 `RuntimeException`을 상속받아 커스텀 예외를 정의

### 예외 발생 가능한 주요 JPA 메서드

-> 대부분의 경우 findById()만 신경쓰면 됨.

- **findById()** (단수조회) : Optional<T> 반환 -> orElseThrow() + customException
- findByUsename() (둘 중 하나) : Optional or List<T> (개발자가 정의) -> 반환 방식에 맞게 예외처리
- findAll(), findByEmail() (복수조회) : List<T> 반환 -> 빈 리스트도 예외 처리 불필요
- getById() : 프록시 반환 -> 사용 비권장

### 예외 발생 가능한 주요 MultipartFile 메서드

- transferTo() : void 반환 -> try-catch + IOException,IllegalStateException
- getBytes() : byte[] 반환 -> try-catch + IOException
- getInputStream() : InputStream 반환 -> try-catch + IOException

### Custom Exception 사용, 기본 예외 사용 기준

1. Custom Exception

- 비즈니스 로직 관련 예외

2. 기본 예외 (IOException)

- 시스템 예외 : I/O, 네트워크 통신 등

### 예외 처리 흐름 (REST API 전환 전 기준)

[클라이언트 요청]
→
[Controller 호출]
→
[Service 호출 (비즈니스 로직)]
→
예외 발생 (throw new ...)
→
[Spring이 예외 감지 → @ControllerAdvice or @ExceptionHandler]
→
[에러 뷰 페이지 반환 or 리다이렉트]

---

### 로깅 (초보자 수준에서) -

#### SLF4J + Logback

-> 로그 설정은 Logback, 로그 출력은 SLF4J로 구성하는게 실무 표준 조합

- SLF4J (Simple Logging Facade for Java) : 로그 찍는 문법 제공(log.info()). @Slf4j로 Slf4j 인터페이스를 주로 사용
- Logback : Spring Boot 기본 로그 구현체. logback.xml을 통해 정밀하게 설정 가능 (개인 환경에서는 yml로 간단하게 설정 가능)

#### SLF4J에서 사용 가능한 로깅 레벨 (우선순위 낮은 → 높은)

- TRACE : 가장 상세한 로깅 (로직 흐름의 모든 단계를 보고 싶을 때)
- DEBUG : 디버깅용 (조건 분기, 반복문 내부 확인 등)
- INFO : 정상 흐름 요약 (요청, 완료, 저장 등 주요 이벤트)
- WARN : 서비스는 돌아가지만 경고 상황 (ex. 재시도, 임시 실패)
- ERROR : 예외 발생 시 무조건 사용 (스택트레이스 포함)
  -> log.error 찍을 때 e.getMessage() 말고 e 객체 넣어야 함!!!

[로깅 레벨을 정해보자](https://velog.io/@idonymyeon/%ED%8C%80-%EB%A1%9C%EA%B9%85-%EC%A0%84%EB%9E%B5-%EC%84%B8%EC%9A%B0%EA%B8%B0-prnri6hn)
[옵저버빌리티: 로그라고해서 다 같은 로그가 아니다(1/2)](https://netmarble.engineering/observability-logging-a/)
[옵저버빌리티: 로그라고해서 다 같은 로그가 아니다(2/2)](https://netmarble.engineering/observability-logging-b/)

#### application.yml 설정

[Common Application Properties](https://docs.spring.io/spring-boot/appendix/application-properties/index.html)

#### 로그를 도입해야 하는 계층

- ExceptionHandler, Service : 필수적으로 적용
- Controller : 선택적으로 적용
- Repository : XXX (JPA가 처리함)

---

### REST API 리팩토링 고민

- 현재 프로젝트는 정적 뷰 리소스 기반임.
- 따라서 rest api를 만들기 위해서는 controller에서 view만 분리하고 나머지를 rest로 만들면 될듯
- 하지만 rest api를 만들어야 하는 이유부터 고민해 봐야함. 또 설계가 선행되어야함.
- 게시판 구조는 자원 중심 구조로 설계하기가 쉽고 일관성 있음.
- 또 나는 백엔드만 설계하고 있기 때문에 구조적으로 의미있음.
- 문서 자동화 하고 싶음
- 나는 성능 최적화, 예외, 인증 등 기능 구현 또한 중요하니까 나중에 기능 흐름에 익숙해졌을 때 리팩토링 하는게 좋아보임