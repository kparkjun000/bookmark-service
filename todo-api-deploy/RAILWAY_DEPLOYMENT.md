# Railway 배포 가이드

## 배포 단계

### 1. GitHub 저장소 준비
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin <GitHub 저장소 URL>
git push -u origin main
```

### 2. Railway 프로젝트 생성
1. [Railway](https://railway.app) 접속 및 로그인
2. "New Project" 클릭
3. "Deploy from GitHub repo" 선택
4. 생성한 GitHub 저장소 선택

### 3. PostgreSQL 데이터베이스 추가
1. Railway 프로젝트에서 "New" 클릭
2. "Database" → "PostgreSQL" 선택
3. 자동으로 데이터베이스가 생성됩니다

### 4. 환경 변수 설정
Railway 프로젝트 Settings에서 다음 환경 변수를 설정:

```
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=${{Postgres.DATABASE_URL}}
JWT_SECRET=mySecretKeyForJWTShouldBeAtLeast256BitsLongForHS256AlgorithmSecureKey2024
```

### 5. 도메인 설정 (선택사항)
1. Settings → Domains
2. "Generate Domain" 클릭하여 무료 도메인 생성
3. 또는 커스텀 도메인 설정

## 중요 파일들

### railway.json
Railway 빌드 설정
```json
{
  "$schema": "https://railway.app/railway.schema.json",
  "build": {
    "builder": "DOCKERFILE"
  },
  "deploy": {
    "restartPolicyType": "ON_FAILURE",
    "restartPolicyMaxRetries": 10
  }
}
```

### nixpacks.toml
Nixpacks 빌드 설정 (Dockerfile 대신 사용 가능)
```toml
[phases.setup]
nixPkgs = ['maven', 'openjdk21']

[phases.build]
cmds = ['mvn clean package -DskipTests']

[start]
cmd = 'java -jar target/todo-api-1.0.0.jar'
```

### Dockerfile
Docker 컨테이너 설정
```dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/todo-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

## 배포 후 확인

### API 엔드포인트 테스트
```bash
# Swagger UI 접근
https://your-app.railway.app/swagger-ui.html

# 헬스 체크
curl https://your-app.railway.app/api/auth/signup
```

### 로그 확인
Railway 대시보드에서 "Deployments" → "View Logs"로 애플리케이션 로그 확인

## 문제 해결

### 빌드 실패 시
1. Java 21 버전 확인
2. Maven 의존성 확인
3. 환경 변수 설정 확인

### 데이터베이스 연결 실패 시
1. DATABASE_URL 환경변수 확인
2. PostgreSQL 서비스 상태 확인
3. 네트워크 연결 확인

### JWT 인증 실패 시
1. JWT_SECRET 환경변수 확인 (256비트 이상)
2. 토큰 만료 시간 확인

## 비용
- **무료 티어**: 월 5달러 크레딧
- **PostgreSQL**: 무료 500MB
- **애플리케이션**: 무료 512MB RAM

배포 완료 후 https://your-app.railway.app에서 API 서비스를 이용할 수 있습니다.