# 빠른 시작 가이드

## 1. 사전 요구사항 확인

- Java 21 설치
- Maven 3.9.x 설치
- PostgreSQL 16+ 설치 (또는 Docker)
- Git 설치

## 2. 데이터베이스 설정

### 옵션 A: 로컬 PostgreSQL 사용

```sql
-- PostgreSQL에 접속
psql -U postgres

-- 데이터베이스 생성
CREATE DATABASE urlshortener;

-- 사용자 생성 (선택사항)
CREATE USER urluser WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE urlshortener TO urluser;
```

### 옵션 B: Docker 사용 (추천)

```bash
# Docker Compose로 PostgreSQL 실행
docker-compose up -d postgres

# 데이터베이스 확인
docker-compose exec postgres psql -U postgres -c "\l"
```

## 3. 프로젝트 클론 및 빌드

```bash
# 프로젝트 클론
git clone <repository-url>
cd 08-url-shortener

# URL Shortener Service 빌드
cd url-shortener-service
mvn clean package
cd ..

# Analytics Service 빌드
cd analytics-service
mvn clean package
cd ..
```

## 4. 서비스 실행

### 터미널 1: URL Shortener Service

```bash
cd url-shortener-service
mvn spring-boot:run
```

또는:

```bash
java -jar target/url-shortener-service-1.0.0.jar
```

서비스가 http://localhost:8080 에서 실행됩니다.

### 터미널 2: Analytics Service

```bash
cd analytics-service
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

또는:

```bash
java -jar target/analytics-service-1.0.0.jar --server.port=8081
```

서비스가 http://localhost:8081 에서 실행됩니다.

## 5. API 테스트

### URL 단축 생성

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://www.github.com"
  }'
```

응답 예시:

```json
{
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://www.github.com"
}
```

### 리디렉션 테스트

브라우저에서 http://localhost:8080/abc123 을 열거나:

```bash
curl -L http://localhost:8080/abc123
```

### QR 코드 생성

브라우저에서:

```
http://localhost:8080/api/urls/abc123/qr
```

### 통계 조회

```bash
curl http://localhost:8081/api/analytics/abc123
```

## 6. Docker Compose로 전체 실행 (간편)

```bash
# 모든 서비스 빌드 및 실행
docker-compose up --build

# 백그라운드 실행
docker-compose up -d

# 로그 확인
docker-compose logs -f

# 종료
docker-compose down
```

## 7. Health Check

### URL Shortener Service

```bash
curl http://localhost:8080/api/health
```

### Analytics Service

```bash
curl http://localhost:8081/api/analytics/health
```

### Spring Boot Actuator

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
```

## 8. 데이터베이스 확인

```bash
# PostgreSQL 접속
psql -U postgres -d urlshortener

# 테이블 확인
\dt

# 데이터 조회
SELECT * FROM shortened_urls;
SELECT * FROM click_events;
```

## 문제 해결

### 포트 충돌

포트 8080 또는 8081이 이미 사용 중이면:

```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -i :8080
kill -9 <PID>
```

또는 다른 포트로 실행:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### 데이터베이스 연결 실패

환경 변수 설정:

```bash
# Windows PowerShell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/urlshortener"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="postgres"

# Linux/Mac
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/urlshortener"
export SPRING_DATASOURCE_USERNAME="postgres"
export SPRING_DATASOURCE_PASSWORD="postgres"
```

### 빌드 실패

Maven 캐시 정리:

```bash
mvn clean
rm -rf ~/.m2/repository
mvn install
```

## 다음 단계

1. [API_EXAMPLES.md](API_EXAMPLES.md) - 더 많은 API 사용 예제
2. [HEROKU_DEPLOYMENT.md](HEROKU_DEPLOYMENT.md) - Heroku 배포 가이드
3. [README.md](README.md) - 전체 프로젝트 문서

## 유용한 명령어

```bash
# 테스트 실행
mvn test

# 코드 커버리지
mvn jacoco:report

# 의존성 확인
mvn dependency:tree

# 프로젝트 정보
mvn site

# 빌드 건너뛰고 실행
mvn spring-boot:run -Dmaven.test.skip=true
```
