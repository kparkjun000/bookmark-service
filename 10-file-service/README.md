# File Upload and Image Processing Service

RESTful API 서비스로 파일 업로드 및 이미지 처리 기능을 제공합니다.

## 기술 스택

- **언어**: Java 21 (LTS)
- **프레임워크**: Spring Boot 3.2.5
- **빌드 도구**: Maven 3.9.x
- **데이터베이스**: PostgreSQL 16+
- **ORM**: Spring Data JPA + Hibernate 6.x
- **배포**: Heroku

## 주요 기능

### 파일 업로드

- 단일 및 다중 파일 업로드 지원
- 파일 크기 제한 (기본 10MB)
- 허용된 파일 확장자 검증 (jpg, jpeg, png, gif, webp, bmp)
- 파일 메타데이터 자동 저장

### 이미지 처리

- 자동 썸네일 생성 (200x200px, 비율 유지)
- 이미지 리사이징 (최대 1920x1080px)
- 이미지 크기 및 해상도 자동 감지
- 이미지 포맷 검증

### 파일 관리

- 파일 다운로드 및 미리보기
- 파일 목록 조회 (페이지네이션 지원)
- 파일 검색 (파일명 및 설명)
- 파일 삭제 (소프트 삭제)

## API 엔드포인트

### 파일 업로드

```http
POST /api/files/upload
Content-Type: multipart/form-data

Parameters:
- file: MultipartFile (required)
- description: String (optional)
- uploadedBy: String (optional)
- generateThumbnail: Boolean (default: true)
- resizeImage: Boolean (default: false)
```

### 다중 파일 업로드

```http
POST /api/files/upload-multiple
Content-Type: multipart/form-data

Parameters:
- files: MultipartFile[] (required)
- description: String (optional)
- uploadedBy: String (optional)
- generateThumbnail: Boolean (default: true)
- resizeImage: Boolean (default: false)
```

### 파일 다운로드

```http
GET /api/files/{fileId}/download
```

### 파일 미리보기

```http
GET /api/files/{fileId}/view
```

### 썸네일 조회

```http
GET /api/files/{fileId}/thumbnail
```

### 파일 메타데이터 조회

```http
GET /api/files/{fileId}
```

### 파일 목록 조회

```http
GET /api/files?page=0&size=10&sortBy=createdAt&sortOrder=desc
```

### 파일 검색

```http
GET /api/files/search?keyword=example&page=0&size=10
```

### 파일 삭제

```http
DELETE /api/files/{fileId}
```

### 헬스 체크

```http
GET /api/health
```

## 로컬 개발 환경 설정

### 사전 요구사항

- Java 21
- Maven 3.9.x
- PostgreSQL 16+

### 1. PostgreSQL 데이터베이스 설정

```sql
CREATE DATABASE fileservice_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE fileservice_db TO postgres;
```

### 2. 애플리케이션 실행

```bash
# 의존성 설치 및 빌드
mvn clean install

# 개발 모드로 실행
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 또는 jar 파일 실행
java -jar target/file-upload-service-1.0.0.jar --spring.profiles.active=dev
```

애플리케이션이 http://localhost:8080 에서 실행됩니다.

## Heroku 배포

### 1. Heroku CLI 설치 및 로그인

```bash
heroku login
```

### 2. Heroku 앱 생성

```bash
heroku create your-app-name
```

### 3. PostgreSQL 애드온 추가

```bash
heroku addons:create heroku-postgresql:essential-0
```

### 4. 환경 변수 설정

```bash
# 프로필 설정
heroku config:set SPRING_PROFILES_ACTIVE=prod

# 파일 업로드 디렉토리 (Heroku는 임시 디렉토리 사용)
heroku config:set FILE_UPLOAD_DIR=/tmp/uploads

# 데이터베이스 URL은 자동으로 설정됩니다 (DATABASE_URL)
```

### 5. 배포

```bash
git add .
git commit -m "Initial commit"
git push heroku main
```

### 6. 데이터베이스 마이그레이션

Heroku는 애플리케이션 시작 시 자동으로 JPA가 테이블을 생성합니다.

### 7. 로그 확인

```bash
heroku logs --tail
```

### 8. 앱 열기

```bash
heroku open
```

## 환경 변수

### 개발 환경 (application-dev.yml)

- `spring.datasource.url`: jdbc:postgresql://localhost:5432/fileservice_db
- `spring.datasource.username`: postgres
- `spring.datasource.password`: postgres

### 프로덕션 환경 (application-prod.yml)

- `DATABASE_URL`: Heroku에서 자동 설정
- `SPRING_PROFILES_ACTIVE`: prod
- `FILE_UPLOAD_DIR`: /tmp/uploads

## 설정

### 파일 업로드 설정 (application.yml)

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

file:
  upload-dir: ./uploads
  max-size: 10485760 # 10MB
  allowed-extensions: jpg,jpeg,png,gif,webp,bmp
```

### 이미지 처리 설정

```yaml
image:
  thumbnail:
    width: 200
    height: 200
  resize:
    max-width: 1920
    max-height: 1080
```

## 테스트

### cURL을 이용한 파일 업로드

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/image.jpg" \
  -F "description=Test image" \
  -F "uploadedBy=testuser" \
  -F "generateThumbnail=true" \
  -F "resizeImage=false"
```

### Postman을 이용한 테스트

1. POST 요청 생성: http://localhost:8080/api/files/upload
2. Body 탭에서 form-data 선택
3. Key: `file`, Type: File, Value: 이미지 파일 선택
4. 추가 파라미터 설정 (description, uploadedBy 등)
5. Send 버튼 클릭

## 응답 형식

### 성공 응답

```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "fileId": "123e4567-e89b-12d3-a456-426614174000",
    "originalFileName": "image.jpg",
    "contentType": "image/jpeg",
    "fileSize": 1024000,
    "width": 1920,
    "height": 1080,
    "downloadUrl": "/api/files/123e4567-e89b-12d3-a456-426614174000/download",
    "thumbnailUrl": "/api/files/123e4567-e89b-12d3-a456-426614174000/thumbnail",
    "uploadedAt": "2024-01-01T12:00:00",
    "message": "File uploaded successfully"
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### 에러 응답

```json
{
  "timestamp": "2024-01-01T12:00:00",
  "status": 400,
  "error": "Invalid File",
  "message": "File extension 'exe' is not allowed",
  "path": "/api/files/upload"
}
```

## 데이터베이스 스키마

### file_metadata 테이블

```sql
CREATE TABLE file_metadata (
    id BIGSERIAL PRIMARY KEY,
    file_id VARCHAR(255) NOT NULL UNIQUE,
    original_file_name VARCHAR(255) NOT NULL,
    stored_file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    file_size BIGINT NOT NULL,
    file_extension VARCHAR(50),
    file_path VARCHAR(1000),
    width INTEGER,
    height INTEGER,
    has_thumbnail BOOLEAN,
    thumbnail_path VARCHAR(1000),
    thumbnail_file_name VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    uploaded_by VARCHAR(255),
    description VARCHAR(1000),
    status VARCHAR(50) NOT NULL
);
```

## 보안 고려사항

1. **파일 유형 검증**: 허용된 확장자만 업로드 가능
2. **파일 크기 제한**: 최대 10MB
3. **경로 순회 공격 방지**: 파일명에 ".." 포함 불가
4. **UUID 기반 파일 ID**: 파일 경로 추측 불가
5. **소프트 삭제**: 실제 파일 삭제 후 DB 상태만 변경

## 성능 최적화

1. **페이지네이션**: 대량의 파일 목록을 효율적으로 조회
2. **인덱스**: file_id, status 컬럼에 인덱스 적용
3. **트랜잭션 관리**: @Transactional을 통한 효율적인 DB 작업
4. **파일 스트리밍**: Resource를 통한 메모리 효율적인 파일 전송

## 라이센스

MIT License

## 개발자

Your Name

## 지원 및 문의

Issues: https://github.com/yourusername/file-upload-service/issues
