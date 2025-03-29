# spring-realworld-board

Spring Boot와 JPA 기반의 게시판 서비스로, 실사용 수준의 구조와 기능 구현을 목표로 합니다.
로그인/회원가입, JWT 인증, 예외 처리 등 실무에서 요구되는 기능과 구조를 학습하며 적용하고 있습니다.

## 🎯 주요 목표 (기획 단계에서 고려 중)
- 실무 환경을 고려한 인증/인가 구조 학습 및 구현 연습
- 사용자 권한에 따른 기능 분리 및 예외 처리 설계 적용
- 도메인 중심 구조(Domain-Driven Design)의 개념 실습
- 계층 구조별 역할 분리 (Controller, Service, Repository) 설계 시도

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