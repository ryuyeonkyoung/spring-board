# spring-realworld-board
Spring Boot와 JPA 등 필요한 기술들을 학습하기 위한 개인 레포지토리입니다.<br>
이후 UMC 스터디에 참여하여 부족한 역량을 이어서 학습하였습니다.

> 참고 강의/코드
> - [코딩레시피 - 스프링부트 게시판 프로젝트](https://www.youtube.com/watch?v=YshcPPHClR4&list=PLV9zd3otBRt7jmXvwCkmvJ8dH5tR_20c0)
> - [codingrecipe1/board](https://github.com/codingrecipe1/board)
> - [shinsunyoung/springboot-developer](https://github.com/shinsunyoung/springboot-developer)

---

## 🛠 기술 스택
| 분류 | 기술 | 설명 |
|------|------|------|
| Backend | Spring Boot 3.x | REST API 개발 및 DI 기반 아키텍처 |
| ORM | Spring Data JPA | 도메인 중심의 데이터 접근 계층 구현 |
| DB | MySQL | 관계형 데이터 저장소 |
| 인증 | Spring Security, JWT, OAuth2 | 인증/인가 흐름 구성 |
| View | Thymeleaf | 서버 사이드 렌더링 기반 View 엔진 |
| Build | Gradle | 프로젝트 빌드 및 의존성 관리 |

---

## 📌 주요 기능
### 📝 게시판 기능 (클론코딩 기반 확장)
- [x] 게시글 CRUD, 페이징, 댓글, 파일 업로드 기능 구현  
  → 게시판 기능은 기존 강의 코드를 기반으로 구현하며, 일부는 수정 및 확장 중입니다.

### 🔐 인증/인가 기능
- [x]  **Spring Security**를 이용한 로그인, 로그아웃, 회원가입 구현
- [ ] **JWT** 기반 로그인/로그아웃 구현
- [ ] **OAuth2** 기반 소셜 로그인 구현
