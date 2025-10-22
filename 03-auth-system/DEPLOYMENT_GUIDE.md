# 배포 가이드

이 문서는 JWT 인증 시스템을 다양한 환경에 배포하는 방법을 설명합니다.

## 📋 목차
- [로컬 개발 환경](#로컬-개발-환경)
- [Docker 배포](#docker-배포)
- [Heroku 배포](#heroku-배포)
- [AWS 배포](#aws-배포)
- [환경별 설정](#환경별-설정)

## 🖥️ 로컬 개발 환경

### 1. PostgreSQL 설치 및 설정

#### Windows (PowerShell)
```powershell
# PostgreSQL 다운로드 및 설치
# https://www.postgresql.org/download/windows/

# 데이터베이스 생성
psql -U postgres
CREATE DATABASE authdb;
\q
```

#### macOS
```bash
# Homebrew를 통한 설치
brew install postgresql@16
brew services start postgresql@16

# 데이터베이스 생성
createdb authdb
```

#### Linux (Ubuntu/Debian)
```bash
# PostgreSQL 설치
sudo apt update
sudo apt install postgresql-16

# PostgreSQL 서비스 시작
sudo systemctl start postgresql
sudo systemctl enable postgresql

# 데이터베이스 생성
sudo -u postgres psql
CREATE DATABASE authdb;
\q
```

### 2. 애플리케이션 실행

```bash
# 개발 프로필로 실행
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# 또는 JAR 파일로 실행
mvn clean package
java -jar -Dspring.profiles.active=dev target/auth-system-1.0.0.jar
```

### 3. 접속 확인

- **애플리케이션**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

## 🐳 Docker 배포

### 1. Docker와 Docker Compose 설치

#### Windows
- [Docker Desktop for Windows](https://docs.docker.com/desktop/install/windows-install/)

#### macOS
```bash
brew install --cask docker
```

#### Linux
```bash
# Docker 설치
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Docker Compose 설치
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

### 2. Docker Compose로 실행

```bash
# 빌드 및 시작
docker-compose up -d --build

# 로그 확인
docker-compose logs -f app

# 중지
docker-compose down

# 볼륨까지 삭제
docker-compose down -v
```

### 3. 개별 Docker 명령어

```bash
# PostgreSQL 컨테이너만 실행
docker run -d \
  --name auth-postgres \
  -e POSTGRES_DB=authdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine

# 애플리케이션 빌드
docker build -t auth-system:1.0.0 .

# 애플리케이션 실행
docker run -d \
  --name auth-app \
  -e DATABASE_URL=jdbc:postgresql://host.docker.internal:5432/authdb \
  -e DATABASE_USERNAME=postgres \
  -e DATABASE_PASSWORD=postgres \
  -p 8080:8080 \
  auth-system:1.0.0
```

## ☁️ Heroku 배포

### 1. 사전 준비

```bash
# Heroku CLI 설치 확인
heroku --version

# Heroku 로그인
heroku login
```

### 2. 새 Heroku 앱 생성

```bash
# 앱 생성
heroku create your-auth-system

# 또는 자동 이름 생성
heroku create

# Git remote 확인
git remote -v
```

### 3. PostgreSQL 애드온 추가

```bash
# Mini 플랜 (무료)
heroku addons:create heroku-postgresql:mini

# 데이터베이스 정보 확인
heroku pg:info

# 데이터베이스 URL 확인
heroku config:get DATABASE_URL
```

### 4. 환경 변수 설정

```bash
# JWT Secret 설정 (강력한 랜덤 문자열 사용)
heroku config:set JWT_SECRET="your-very-secure-secret-key-at-least-256-bits"

# Java 버전 설정 (system.properties에 이미 정의됨)
# 추가 설정이 필요한 경우:
heroku config:set JAVA_TOOL_OPTIONS="-Xmx300m -Xss512k -XX:CICompilerCount=2"
```

### 5. 배포

```bash
# Git 커밋 (변경사항이 있다면)
git add .
git commit -m "Ready for Heroku deployment"

# Heroku에 푸시
git push heroku main

# 또는 다른 브랜치에서
git push heroku your-branch:main
```

### 6. 배포 확인

```bash
# 로그 확인
heroku logs --tail

# 앱 열기
heroku open

# Swagger UI 접속
heroku open /swagger-ui.html

# 앱 정보
heroku apps:info
```

### 7. 데이터베이스 관리

```bash
# psql로 접속
heroku pg:psql

# 백업 생성
heroku pg:backups:capture

# 백업 복원
heroku pg:backups:restore

# 데이터베이스 초기화
heroku pg:reset DATABASE
```

### 8. 스케일링

```bash
# 다이노 상태 확인
heroku ps

# 스케일 업
heroku ps:scale web=2

# 스케일 다운
heroku ps:scale web=1
```

## ☁️ AWS 배포

### AWS Elastic Beanstalk

#### 1. EB CLI 설치

```bash
pip install awsebcli
```

#### 2. 초기화 및 배포

```bash
# EB 초기화
eb init -p java-17 auth-system --region ap-northeast-2

# 환경 생성 및 배포
eb create auth-production

# 환경 변수 설정
eb setenv JWT_SECRET="your-secret" \
  DATABASE_URL="your-rds-url" \
  DATABASE_USERNAME="your-username" \
  DATABASE_PASSWORD="your-password"

# 배포
eb deploy

# 상태 확인
eb status

# 로그 확인
eb logs
```

### AWS EC2 (수동 배포)

#### 1. EC2 인스턴스 생성
- Amazon Linux 2 또는 Ubuntu 선택
- 보안 그룹: 8080 포트 개방

#### 2. SSH 접속 및 환경 설정

```bash
# SSH 접속
ssh -i your-key.pem ec2-user@your-instance-ip

# Java 21 설치 (Amazon Linux 2)
sudo yum install java-21-amazon-corretto -y

# 또는 Ubuntu
sudo apt update
sudo apt install openjdk-21-jdk -y

# PostgreSQL 설치 (또는 RDS 사용)
sudo yum install postgresql15 -y
```

#### 3. 애플리케이션 배포

```bash
# JAR 파일 업로드 (로컬에서)
scp -i your-key.pem target/auth-system-1.0.0.jar ec2-user@your-instance-ip:/home/ec2-user/

# EC2에서 실행
java -jar \
  -Dspring.profiles.active=prod \
  -DDATABASE_URL=your-rds-url \
  -DDATABASE_USERNAME=your-username \
  -DDATABASE_PASSWORD=your-password \
  -DJWT_SECRET=your-secret \
  auth-system-1.0.0.jar
```

#### 4. Systemd 서비스 등록

```bash
# 서비스 파일 생성
sudo vim /etc/systemd/system/auth-system.service

# 내용:
[Unit]
Description=JWT Auth System
After=syslog.target network.target

[Service]
User=ec2-user
ExecStart=/usr/bin/java -jar /home/ec2-user/auth-system-1.0.0.jar
SuccessExitStatus=143
Environment="SPRING_PROFILES_ACTIVE=prod"
Environment="DATABASE_URL=your-rds-url"
Environment="DATABASE_USERNAME=your-username"
Environment="DATABASE_PASSWORD=your-password"
Environment="JWT_SECRET=your-secret"

[Install]
WantedBy=multi-user.target

# 서비스 시작
sudo systemctl daemon-reload
sudo systemctl start auth-system
sudo systemctl enable auth-system

# 상태 확인
sudo systemctl status auth-system
```

## ⚙️ 환경별 설정

### 개발 환경 (dev)

```yaml
# application-dev.yml
spring:
  jpa:
    hibernate:
      ddl-auto: create-drop  # 매번 테이블 재생성
    show-sql: true
logging:
  level:
    com.auth: DEBUG
```

```bash
# 실행
java -jar -Dspring.profiles.active=dev app.jar
```

### 스테이징 환경 (staging)

```yaml
# application-staging.yml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: false
logging:
  level:
    com.auth: INFO
```

### 프로덕션 환경 (prod)

```yaml
# application-prod.yml
spring:
  jpa:
    hibernate:
      ddl-auto: validate  # 스키마 변경 금지
    show-sql: false
logging:
  level:
    root: WARN
    com.auth: INFO
```

```bash
# 실행
java -jar -Dspring.profiles.active=prod app.jar
```

## 🔒 보안 체크리스트

- [ ] JWT Secret을 256비트 이상의 강력한 값으로 설정
- [ ] 데이터베이스 비밀번호 변경 및 안전하게 관리
- [ ] HTTPS 설정 (프로덕션 필수)
- [ ] CORS 설정 검토
- [ ] Rate Limiting 구현 고려
- [ ] 로그에 민감한 정보 노출 방지
- [ ] 정기적인 보안 업데이트

## 🔍 모니터링

### Heroku

```bash
# 메트릭 확인
heroku labs:enable log-runtime-metrics
heroku logs --tail | grep "sample#memory"

# 애드온으로 모니터링 추가
heroku addons:create newrelic:wayne
```

### AWS CloudWatch

```bash
# EB 로그 CloudWatch로 스트리밍
eb logs --cloudwatch-logs enable
```

## 🚨 트러블슈팅

### 메모리 부족

```bash
# Heroku
heroku config:set JAVA_TOOL_OPTIONS="-Xmx256m"

# 일반
java -Xmx512m -jar app.jar
```

### 데이터베이스 연결 실패

```bash
# 연결 테스트
psql $DATABASE_URL

# Heroku에서 연결 확인
heroku pg:psql
```

### 포트 바인딩 오류

```bash
# Heroku는 자동으로 PORT 환경 변수 설정
# application.yml에서 ${PORT:8080} 사용 확인
```

## 📊 성능 최적화

1. **Connection Pool 튜닝**
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
```

2. **JVM 옵션 최적화**
```bash
java -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -jar app.jar
```

3. **정적 파일 CDN 사용**

4. **데이터베이스 인덱스 추가**
```sql
CREATE INDEX idx_username ON users(username);
CREATE INDEX idx_email ON users(email);
```

## 🔄 CI/CD

### GitHub Actions 예시

```yaml
# .github/workflows/deploy.yml
name: Deploy to Heroku

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: akhileshns/heroku-deploy@v3.12.12
        with:
          heroku_api_key: ${{secrets.HEROKU_API_KEY}}
          heroku_app_name: "your-app-name"
          heroku_email: "your-email@example.com"
```


