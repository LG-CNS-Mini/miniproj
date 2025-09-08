# Instagram Clone (React + Spring Boot)

**간단 소개**

이 프로젝트는 `React (JS/JSX)` 프론트엔드와 `Spring Boot` 백엔드로 구성된 인스타그램 클론 웹 애플리케이션입니다. 참고 레포지토리(Instagram-Clone-Coding)를 기반으로, 교육용/포트폴리오 목적의 기능을 분석 · 설계 · 구현 · 배포까지 포함한 완성형 예시입니다.

---

## 주요 목표

* 인스타그램의 핵심 기능(회원가입/로그인, 피드, 좋아요/댓글, 팔로우, 게시물 업로드 등)을 학습 목적에 맞춰 구현
* Frontend와 Backend 간의 협업(REST API 설계, 인증/인가, 파일 업로드) 경험 제공
* 코드 품질(리팩토링, 테스트) 및 실제 배포(AWS/EC2, S3) 경험

---

## 기능 요약

* 인증/인가: JWT 기반 로그인 및 권한 처리
* 프로필: 사용자 프로필 조회/수정
* 피드: 게시물 리스트(무한 스크롤), 좋아요, 저장
* 게시물: 업로드(이미지), 삭제, 수정
* 팔로우: 팔로우/언팔로우, 팔로워/팔로잉 리스트
* 댓글: CRUD(댓글 작성/조회/수정/삭제)
* 알림(간단): 좋아요/댓글/팔로우 알림(선택 구현)
* 검색: 사용자 검색, 해시태그(선택 구현)

---

## 아키텍처 (개요)

```
[React (Client)] <--> [Spring Boot (REST API)] <--> [MariaDB / Redis]
```

* 인증: Spring Security + JWT
* DB: MariaDB, Redis는 세션/캐시/팔로우 집계 등에 활용
* 스토리지: 로컬 파일 스토리지(개발용)
* 배포: AWS EC2 (프론트 정적 서빙 또는 CDN + 백엔드), Nginx 리버스 프록시

---

## 기술 스택

**Frontend**

* React (JS / JSX)
* 상태관리: Context
* HTTP: Axios
* UI: Styled-Components / CSS 모듈

**Backend**

* Java 17+, Spring Boot 3.x
* Spring Security, JWT
* Spring Data JPA (MariaDB)
* Querydsl (선택)
* 테스트: JUnit5, Mockito

**인프라**

* DB: MariaDB (RDS)
* 캐시/세션: Redis
* 파일 스토리지: 로컬 스토리지
* 배포: AWS EC2, Nginx, SSL (Certificate Manager/Let's Encrypt)

---

## 로컬 개발 환경 설정

### 요구사항

* Node.js (v14+ 권장)
* Java 17+
* Gradle
* MariaDB
* Redis 

### 환경 변수 (.env / application.yml)

**Frontend (.env)**

```
REACT_APP_API_BASE_URL=http://localhost:8080/api
REACT_APP_WS_URL=http://localhost:8080/ws
```

**Backend (application.yml 또는 환경변수)**

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/instagram_clone
    username: your_db_user
    password: your_db_password
  jpa:
    hibernate:
      ddl-auto: update
jwt:
  secret: your_jwt_secret_key
  expirationMs: 86400000
```

## API 문서

* Swagger를 통해 API 문서를 제공합니다. (`http://localhost:8088/swagger-ui/index.htm`)
* 프로젝트 참조 레포지토리의 API 문서를 기반으로 엔드포인트를 설계하세요.

---

## 배포

* 정적 파일(React)은 Nginx에서 서빙
* Spring Boot 앱은 로컬에서 실행

---

## 개발/확장 가이드

* 이미지 파일은 S3에 저장하고, DB에는 S3 URL을 저장하는 것을 권장
* 토큰 기반 인증(JWT) 사용 시 Refresh token 관리 전략을 정의
* 실시간 채팅은 메시지 보존/읽음 처리(읽음 상태)와 방 관리 설계 필요
* 대용량 트래픽 고려 시 페이지네이션/캐싱(캐시 무효화 전략) 도입

---

## 기여자(참고)

참고 레포지토리
`https://github.com/Instagram-Clone-Coding`



# 요구사항 명세서

## 1. 인증 및 사용자 관리
### 1.1 로그인
- JWT 기반 이메일 로그인
- Facebook OAuth 로그인
### 1.2 로그아웃
### 1.3 프로필
- 닉네임, 프로필 사진 조회 및 수정

## 2. 게시글 관리
### 2.1 게시글 등록
- 텍스트 + 이미지 업로드
### 2.2 게시글 조회
- 타임라인 형태 피드 (무한 스크롤 or 페이징)

## 3. 상호작용
### 3.1 좋아요
- 좋아요/취소, 수량 표시
### 3.2 팔로우
- 팔로우/언팔로우
### 3.3 알림
- 좋아요/팔로우 시 알림 전달

## 4. (Optional) 추가 기능
- 댓글 기능 (텍스트 기반)
- 알림 페이지
- 사용자 검색
- 프로필 페이지 (유저별 게시물 확인)

## 5. 기술 스택
- 프론트엔드: React (함께 사용할 주요 라이브러리 명시 가능)
- 백엔드: Spring Boot + JWT + Facebook OAuth
- DB: (예: MySQL or H2)
- 파일 저장: (예: AWS S3 or 서버 저장)
- 알림: (예: WebSocket or DB 조회 기반)
