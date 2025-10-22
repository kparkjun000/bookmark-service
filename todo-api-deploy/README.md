# Todo API Service

할일(Todo) 관리를 위한 RESTful API 서비스입니다.

## 기술 스택

- Java 21
- Spring Boot 3.2.0
- Spring Security 6.x + JWT
- PostgreSQL 16
- Maven 3.9.11
- Swagger/OpenAPI

## 주요 기능

### MVP 기능
- **Todo CRUD**: 할일 생성, 조회, 수정, 삭제
- **사용자 관리**: 회원가입, 로그인, JWT 인증
- **상태 관리**: 완료/미완료, 우선순위별 조회

### 확장 기능
- **카테고리 관리**: 카테고리별 todo 그룹화
- **검색 기능**: 제목/설명 기반 검색
- **통계**: 완료율, 카테고리별 통계
- **데이터 내보내기**: JSON/CSV 형태로 내보내기

## 로컬 개발 환경 설정

### 사전 요구사항
- JDK 21
- Maven 3.9.11
- PostgreSQL 16

### 데이터베이스 설정
```sql
CREATE DATABASE todoapi;
```

### 애플리케이션 실행
```bash
mvn clean install
mvn spring-boot:run
```

## API 문서

애플리케이션 실행 후 아래 URL에서 Swagger UI를 통해 API 문서를 확인할 수 있습니다:
- http://localhost:8080/swagger-ui.html

## Railway 배포

### 환경 변수 설정
Railway에서 다음 환경 변수를 설정해야 합니다:
- `DATABASE_URL`: PostgreSQL 연결 URL
- `JWT_SECRET`: JWT 서명용 비밀 키 (최소 256비트)
- `SPRING_PROFILES_ACTIVE`: production

### 배포 단계
1. Railway 프로젝트 생성
2. PostgreSQL 서비스 추가
3. GitHub 리포지토리 연결
4. 환경 변수 설정
5. 자동 배포

## API 엔드포인트

### 인증
- `POST /api/auth/signup`: 회원가입
- `POST /api/auth/login`: 로그인

### Todo
- `GET /api/todos`: 할일 목록 조회
- `POST /api/todos`: 할일 생성
- `GET /api/todos/{id}`: 할일 상세 조회
- `PUT /api/todos/{id}`: 할일 수정
- `DELETE /api/todos/{id}`: 할일 삭제
- `GET /api/todos/search`: 할일 검색
- `GET /api/todos/status/{completed}`: 상태별 조회
- `GET /api/todos/priority/{priority}`: 우선순위별 조회

### 카테고리
- `GET /api/categories`: 카테고리 목록 조회
- `POST /api/categories`: 카테고리 생성
- `PUT /api/categories/{id}`: 카테고리 수정
- `DELETE /api/categories/{id}`: 카테고리 삭제

### 통계
- `GET /api/statistics`: 통계 조회

### 내보내기
- `GET /api/export/json`: JSON 형식으로 내보내기
- `GET /api/export/csv`: CSV 형식으로 내보내기