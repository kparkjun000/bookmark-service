# Memo Backend API

텍스트 메모 및 검색 기능을 제공하는 RESTful API 서비스입니다.

## 📋 목차

- [기능](#기능)
- [기술 스택](#기술-스택)
- [시작하기](#시작하기)
- [API 문서](#api-문서)
- [Heroku 배포](#heroku-배포)

## ✨ 기능

### 1. 메모 CRUD

- ✅ 메모 생성
- ✅ 메모 조회 (전체/개별)
- ✅ 메모 수정
- ✅ 메모 삭제

### 2. 태그 관리

- ✅ 태그 생성/수정/삭제
- ✅ 메모에 태그 추가
- ✅ 태그별 메모 조회
- ✅ 태그 색상 지정

### 3. 전문 검색

- ✅ 키워드로 메모 검색 (제목 + 내용)
- ✅ 태그로 메모 필터링
- ✅ 작성자로 메모 검색

### 4. 메모 공유

- ✅ 공유 링크 생성
- ✅ 만료 시간 설정
- ✅ 공유 링크 비활성화
- ✅ 공유 메모 조회

## 🛠 기술 스택

| 분류         | 기술                            |
| ------------ | ------------------------------- |
| 언어         | Java 21 (LTS)                   |
| 프레임워크   | Spring Boot 3.2.5               |
| 빌드 도구    | Maven 3.9.x                     |
| 데이터베이스 | PostgreSQL 16+                  |
| ORM          | Spring Data JPA + Hibernate 6.x |
| API 문서     | SpringDoc OpenAPI 3 (Swagger)   |
| 배포         | Heroku                          |

## 🚀 시작하기

### 필수 요구사항

- Java 21 이상
- Maven 3.9.x 이상
- PostgreSQL 16 이상

### 로컬 데이터베이스 설정

```bash
# PostgreSQL 데이터베이스 생성
createdb memodb

# 또는 psql 사용
psql -U postgres
CREATE DATABASE memodb;
```

### 환경 변수 설정

`.env` 파일을 생성하거나 환경 변수를 설정합니다:

```bash
DATABASE_URL=jdbc:postgresql://localhost:5432/memodb
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_password
```

### 애플리케이션 실행

```bash
# Maven을 사용한 빌드 및 실행
mvn clean install
mvn spring-boot:run

# 또는 JAR 파일 직접 실행
java -jar target/memo-backend-1.0.0.jar
```

애플리케이션은 기본적으로 `http://localhost:8080` 에서 실행됩니다.

## 📚 API 문서

### Swagger UI

애플리케이션 실행 후 아래 URL에서 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui.html
```

### 주요 엔드포인트

#### 메모 API

| Method | Endpoint                              | 설명               |
| ------ | ------------------------------------- | ------------------ |
| GET    | `/api/memos`                          | 모든 메모 조회     |
| GET    | `/api/memos/{id}`                     | 특정 메모 조회     |
| POST   | `/api/memos`                          | 메모 생성          |
| PUT    | `/api/memos/{id}`                     | 메모 수정          |
| DELETE | `/api/memos/{id}`                     | 메모 삭제          |
| GET    | `/api/memos/search?keyword={keyword}` | 키워드로 메모 검색 |
| GET    | `/api/memos/tag/{tagName}`            | 태그로 메모 검색   |
| GET    | `/api/memos/author/{author}`          | 작성자로 메모 검색 |

#### 태그 API

| Method | Endpoint                             | 설명           |
| ------ | ------------------------------------ | -------------- |
| GET    | `/api/tags`                          | 모든 태그 조회 |
| GET    | `/api/tags/{id}`                     | 특정 태그 조회 |
| POST   | `/api/tags`                          | 태그 생성      |
| PUT    | `/api/tags/{id}`                     | 태그 수정      |
| DELETE | `/api/tags/{id}`                     | 태그 삭제      |
| GET    | `/api/tags/search?keyword={keyword}` | 태그 검색      |

#### 메모 공유 API

| Method | Endpoint                           | 설명                  |
| ------ | ---------------------------------- | --------------------- |
| POST   | `/api/shared/memos/{memoId}`       | 공유 링크 생성        |
| GET    | `/api/shared/{shareToken}`         | 공유 메모 조회        |
| GET    | `/api/shared/memos/{memoId}/links` | 메모의 공유 링크 목록 |
| DELETE | `/api/shared/{shareToken}`         | 공유 링크 비활성화    |

### 요청 예시

#### 메모 생성

```bash
curl -X POST http://localhost:8080/api/memos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "프로젝트 아이디어",
    "content": "새로운 메모 앱 개발하기",
    "author": "홍길동",
    "tagNames": ["개발", "아이디어"]
  }'
```

#### 메모 검색

```bash
curl -X GET "http://localhost:8080/api/memos/search?keyword=프로젝트"
```

#### 메모 공유 링크 생성

```bash
curl -X POST http://localhost:8080/api/shared/memos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "expiresInHours": 24
  }'
```

## 🌐 Heroku 배포

### 1. Heroku CLI 설치

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# Heroku CLI 인스톨러 다운로드: https://devcenter.heroku.com/articles/heroku-cli
```

### 2. Heroku 로그인

```bash
heroku login
```

### 3. 앱 생성

```bash
heroku create your-app-name
```

### 4. PostgreSQL 애드온 추가

```bash
heroku addons:create heroku-postgresql:mini
```

### 5. 환경 변수 설정

Heroku는 자동으로 `DATABASE_URL`을 설정합니다. 추가 설정이 필요한 경우:

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

### 6. 배포

```bash
git add .
git commit -m "Initial commit"
git push heroku main
```

### 7. 애플리케이션 열기

```bash
heroku open
```

Swagger UI는 `https://your-app-name.herokuapp.com/swagger-ui.html` 에서 확인할 수 있습니다.

### 8. 로그 확인

```bash
heroku logs --tail
```

## 📦 데이터베이스 스키마

### Memos 테이블

| 컬럼       | 타입         | 설명      |
| ---------- | ------------ | --------- |
| id         | BIGINT       | 기본 키   |
| title      | VARCHAR(200) | 메모 제목 |
| content    | TEXT         | 메모 내용 |
| author     | VARCHAR(100) | 작성자    |
| created_at | TIMESTAMP    | 생성 시간 |
| updated_at | TIMESTAMP    | 수정 시간 |

### Tags 테이블

| 컬럼       | 타입        | 설명               |
| ---------- | ----------- | ------------------ |
| id         | BIGINT      | 기본 키            |
| name       | VARCHAR(50) | 태그 이름 (유니크) |
| color      | VARCHAR(7)  | 태그 색상 (Hex)    |
| created_at | TIMESTAMP   | 생성 시간          |
| updated_at | TIMESTAMP   | 수정 시간          |

### Shared_Memos 테이블

| 컬럼        | 타입        | 설명               |
| ----------- | ----------- | ------------------ |
| id          | BIGINT      | 기본 키            |
| memo_id     | BIGINT      | 메모 외래 키       |
| share_token | VARCHAR(64) | 공유 토큰 (유니크) |
| expires_at  | TIMESTAMP   | 만료 시간          |
| is_active   | BOOLEAN     | 활성화 상태        |
| created_at  | TIMESTAMP   | 생성 시간          |

## 🧪 테스트

```bash
mvn test
```

## 📝 라이선스

이 프로젝트는 MIT 라이선스를 따릅니다.

## 👥 기여

이슈와 풀 리퀘스트를 환영합니다!

## 📞 문의

- Email: contact@zerobase.com
- GitHub: https://github.com/zerobase

---

Made with ❤️ by ZeroBase
