# Bookmark Service API

URL 북마크 관리 및 태그 서비스 RESTful API

## 기술 스택

- **언어**: Java 21 (LTS)
- **프레임워크**: Spring Boot 3.2.x
- **빌드 도구**: Maven 3.9.x
- **데이터베이스**: PostgreSQL 16+
- **ORM**: Spring Data JPA + Hibernate 6.x
- **배포**: Heroku
- **API 문서**: Swagger/OpenAPI 3.0

## 주요 기능

- ✅ URL 북마크 저장 및 관리
- ✅ URL 메타데이터 자동 추출 (제목, 설명, 이미지, 사이트명)
- ✅ 태그 기반 북마크 분류 및 관리
- ✅ 북마크 검색 (제목, 설명, URL)
- ✅ 즐겨찾기 기능
- ✅ 공개 북마크 공유
- ✅ 페이지네이션 지원

## 시작하기

### 사전 요구사항

- Java 21
- Maven 3.9.x
- PostgreSQL 16+

### 로컬 실행

1. 저장소 클론

```bash
git clone https://github.com/zerobase/bookmark-service.git
cd bookmark-service
```

2. PostgreSQL 데이터베이스 생성

```sql
CREATE DATABASE bookmarkdb;
```

3. 환경 변수 설정 (또는 application.yml 수정)

```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/bookmarkdb
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password
```

4. 애플리케이션 실행

```bash
mvn clean install
mvn spring-boot:run
```

5. API 문서 확인
   브라우저에서 다음 URL로 접속:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

## Heroku 배포

### 1. Heroku CLI 설치

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# https://devcenter.heroku.com/articles/heroku-cli에서 설치
```

### 2. Heroku 로그인

```bash
heroku login
```

### 3. Heroku 앱 생성

```bash
heroku create your-app-name
```

### 4. PostgreSQL 애드온 추가

```bash
heroku addons:create heroku-postgresql:essential-0
```

### 5. 환경 변수 확인

```bash
heroku config
# DATABASE_URL이 자동으로 설정됨
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

Swagger UI 접속: `https://your-app-name.herokuapp.com/swagger-ui.html`

## API 엔드포인트

### 사용자 관리

- `POST /api/users` - 사용자 생성
- `GET /api/users` - 모든 사용자 조회
- `GET /api/users/{userId}` - 사용자 조회
- `GET /api/users/email/{email}` - 이메일로 사용자 조회
- `DELETE /api/users/{userId}` - 사용자 삭제

### 북마크 관리

- `POST /api/users/{userId}/bookmarks` - 북마크 생성
- `GET /api/users/{userId}/bookmarks` - 북마크 목록 조회
- `GET /api/users/{userId}/bookmarks/{bookmarkId}` - 북마크 상세 조회
- `GET /api/users/{userId}/bookmarks/favorites` - 즐겨찾기 북마크 조회
- `GET /api/users/{userId}/bookmarks/tags/{tagId}` - 태그별 북마크 조회
- `GET /api/users/{userId}/bookmarks/search` - 북마크 검색
- `PUT /api/users/{userId}/bookmarks/{bookmarkId}` - 북마크 수정
- `DELETE /api/users/{userId}/bookmarks/{bookmarkId}` - 북마크 삭제

### 태그 관리

- `POST /api/users/{userId}/tags` - 태그 생성
- `GET /api/users/{userId}/tags` - 사용자 태그 목록 조회
- `GET /api/users/{userId}/tags/{tagId}` - 태그 조회
- `DELETE /api/users/{userId}/tags/{tagId}` - 태그 삭제

### 공개 API

- `GET /api/public/bookmarks` - 공개 북마크 조회
- `GET /api/public/metadata?url={url}` - URL 메타데이터 추출

## 사용 예시

### 1. 사용자 생성

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "name": "홍길동"
  }'
```

### 2. 북마크 생성 (메타데이터 자동 추출)

```bash
curl -X POST http://localhost:8080/api/users/1/bookmarks \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://www.example.com",
    "tagNames": ["개발", "참고자료"],
    "isPublic": false,
    "isFavorite": true
  }'
```

### 3. URL 메타데이터 추출

```bash
curl -X GET "http://localhost:8080/api/public/metadata?url=https://www.example.com"
```

### 4. 북마크 검색

```bash
curl -X GET "http://localhost:8080/api/users/1/bookmarks/search?keyword=개발&page=0&size=20"
```

## 데이터베이스 스키마

### Users

- id (PK)
- email (unique)
- name
- created_at
- updated_at

### Bookmarks

- id (PK)
- url
- title
- description
- image_url
- site_name
- is_public
- is_favorite
- user_id (FK)
- created_at
- updated_at

### Tags

- id (PK)
- name
- user_id (FK)
- created_at
- updated_at

### Bookmark_Tags (조인 테이블)

- bookmark_id (FK)
- tag_id (FK)

## 라이선스

MIT License

## 문의

- Email: admin@bookmarkservice.com
- GitHub: https://github.com/zerobase/bookmark-service
