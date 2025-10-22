# 빠른 시작 가이드

이 가이드는 File Upload and Image Processing Service를 빠르게 시작하는 방법을 안내합니다.

## 목차

1. [로컬 개발 환경 설정](#로컬-개발-환경-설정)
2. [Docker로 실행](#docker로-실행)
3. [첫 번째 API 호출](#첫-번째-api-호출)
4. [Heroku에 배포](#heroku에-배포)

---

## 로컬 개발 환경 설정

### 1단계: 사전 요구사항 확인

```bash
# Java 21 확인
java -version

# Maven 확인
mvn -version

# PostgreSQL 확인
psql --version
```

### 2단계: PostgreSQL 데이터베이스 생성

```bash
# PostgreSQL 접속
psql -U postgres

# 데이터베이스 생성
CREATE DATABASE fileservice_db;

# 종료
\q
```

또는 SQL 스크립트 실행:

```sql
CREATE DATABASE fileservice_db;
CREATE USER postgres WITH PASSWORD 'postgres';
GRANT ALL PRIVILEGES ON DATABASE fileservice_db TO postgres;
```

### 3단계: 프로젝트 빌드

```bash
# Windows
mvnw.cmd clean install

# Linux/Mac
chmod +x mvnw
./mvnw clean install
```

### 4단계: 애플리케이션 실행

```bash
# Windows
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

# Linux/Mac
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

애플리케이션이 http://localhost:8080 에서 실행됩니다.

### 5단계: 동작 확인

브라우저에서 http://localhost:8080/api/health 접속

예상 응답:

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "status": "UP",
    "timestamp": "2024-01-15T10:30:00",
    "service": "File Upload Service"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## Docker로 실행

Docker와 Docker Compose가 설치되어 있다면 더 쉽게 실행할 수 있습니다.

### 1단계: Docker 빌드 및 실행

```bash
# Docker Compose로 전체 스택 실행 (PostgreSQL + 애플리케이션)
docker-compose up -d

# 로그 확인
docker-compose logs -f app
```

### 2단계: 중지 및 삭제

```bash
# 중지
docker-compose down

# 볼륨까지 삭제 (데이터베이스 데이터 삭제)
docker-compose down -v
```

---

## 첫 번째 API 호출

### cURL로 파일 업로드

```bash
# 테스트 이미지 다운로드 (선택사항)
curl -o test-image.jpg https://picsum.photos/1920/1080

# 파일 업로드
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@test-image.jpg" \
  -F "description=First test image" \
  -F "uploadedBy=tester" \
  -F "generateThumbnail=true" \
  -F "resizeImage=false"
```

예상 응답:

```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "fileId": "123e4567-e89b-12d3-a456-426614174000",
    "originalFileName": "test-image.jpg",
    "contentType": "image/jpeg",
    "fileSize": 2048576,
    "width": 1920,
    "height": 1080,
    "downloadUrl": "/api/files/123e4567-e89b-12d3-a456-426614174000/download",
    "thumbnailUrl": "/api/files/123e4567-e89b-12d3-a456-426614174000/thumbnail",
    "uploadedAt": "2024-01-15T10:30:00",
    "message": "File uploaded successfully"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 업로드된 파일 조회

```bash
# 파일 목록 조회
curl http://localhost:8080/api/files

# 특정 파일 메타데이터 조회
curl http://localhost:8080/api/files/{fileId}

# 파일 다운로드
curl http://localhost:8080/api/files/{fileId}/download -o downloaded.jpg

# 썸네일 다운로드
curl http://localhost:8080/api/files/{fileId}/thumbnail -o thumbnail.jpg
```

### 웹 브라우저에서 테스트

1. 브라우저에서 http://localhost:8080/api/files 접속
2. 업로드된 파일 목록 확인
3. `thumbnailUrl`을 복사하여 브라우저에 붙여넣기
4. 썸네일 이미지 확인

---

## Heroku에 배포

### 빠른 배포 (5분 완성)

1. **Heroku CLI 설치**

   ```bash
   # Windows (Chocolatey)
   choco install heroku-cli

   # macOS
   brew tap heroku/brew && brew install heroku
   ```

2. **로그인 및 앱 생성**

   ```bash
   heroku login
   heroku create your-file-service
   ```

3. **PostgreSQL 추가**

   ```bash
   heroku addons:create heroku-postgresql:essential-0
   ```

4. **환경 변수 설정**

   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   heroku config:set FILE_UPLOAD_DIR=/tmp/uploads
   ```

5. **Git 설정 및 배포**

   ```bash
   git init
   git add .
   git commit -m "Initial deployment"
   git push heroku main
   ```

6. **앱 열기**

   ```bash
   heroku open
   ```

7. **로그 확인**
   ```bash
   heroku logs --tail
   ```

---

## Postman으로 테스트

### 1단계: Postman Collection 가져오기

1. Postman 실행
2. Import 클릭
3. 아래 JSON을 복사하여 붙여넣기

```json
{
  "info": {
    "name": "File Upload Service API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Upload File",
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "formdata",
          "formdata": [
            {
              "key": "file",
              "type": "file",
              "src": "/path/to/image.jpg"
            },
            {
              "key": "description",
              "value": "Test image",
              "type": "text"
            },
            {
              "key": "uploadedBy",
              "value": "tester",
              "type": "text"
            },
            {
              "key": "generateThumbnail",
              "value": "true",
              "type": "text"
            },
            {
              "key": "resizeImage",
              "value": "false",
              "type": "text"
            }
          ]
        },
        "url": {
          "raw": "{{base_url}}/api/files/upload",
          "host": ["{{base_url}}"],
          "path": ["api", "files", "upload"]
        }
      }
    },
    {
      "name": "Get All Files",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/files?page=0&size=10",
          "host": ["{{base_url}}"],
          "path": ["api", "files"],
          "query": [
            {
              "key": "page",
              "value": "0"
            },
            {
              "key": "size",
              "value": "10"
            }
          ]
        }
      }
    },
    {
      "name": "Get File Metadata",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/files/{{fileId}}",
          "host": ["{{base_url}}"],
          "path": ["api", "files", "{{fileId}}"]
        }
      }
    },
    {
      "name": "Download File",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/files/{{fileId}}/download",
          "host": ["{{base_url}}"],
          "path": ["api", "files", "{{fileId}}", "download"]
        }
      }
    },
    {
      "name": "Delete File",
      "request": {
        "method": "DELETE",
        "header": [],
        "url": {
          "raw": "{{base_url}}/api/files/{{fileId}}",
          "host": ["{{base_url}}"],
          "path": ["api", "files", "{{fileId}}"]
        }
      }
    }
  ],
  "variable": [
    {
      "key": "base_url",
      "value": "http://localhost:8080",
      "type": "string"
    },
    {
      "key": "fileId",
      "value": "",
      "type": "string"
    }
  ]
}
```

### 2단계: Environment 설정

- Variable: `base_url`
- Value: `http://localhost:8080` (로컬) 또는 `https://your-app.herokuapp.com` (Heroku)

---

## 문제 해결

### 포트 충돌 (Port 8080 already in use)

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

또는 다른 포트 사용:

```bash
# application-dev.yml에 추가
server:
  port: 8081
```

### PostgreSQL 연결 실패

```bash
# PostgreSQL 서비스 상태 확인
# Windows
sc query postgresql-x64-16

# Linux
sudo systemctl status postgresql

# Mac
brew services list
```

### 파일 업로드 디렉토리 권한 에러

```bash
# Windows
mkdir uploads
icacls uploads /grant Users:F

# Linux/Mac
mkdir -p uploads
chmod 777 uploads
```

---

## 다음 단계

1. **API 문서 읽기**: `API_EXAMPLES.md` 참고
2. **배포 가이드**: `DEPLOYMENT.md` 참고
3. **전체 README**: `README.md` 참고

---

## 빠른 명령어 참고

```bash
# 프로젝트 빌드
mvn clean install

# 개발 모드 실행
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 테스트 실행
mvn test

# Docker Compose 실행
docker-compose up -d

# Heroku 배포
git push heroku main

# Heroku 로그
heroku logs --tail

# 애플리케이션 중지
Ctrl + C
```

---

## 추가 지원

- GitHub Issues: 문제 보고
- README.md: 자세한 문서
- API_EXAMPLES.md: API 사용 예제
- DEPLOYMENT.md: 배포 가이드
