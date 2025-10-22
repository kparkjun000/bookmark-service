# 🚀 Railway 배포 가이드 (새로운 프로젝트)

## 1. GitHub 새 저장소 생성

1. GitHub 접속: https://github.com
2. "New repository" 클릭
3. Repository name: `todo-api-deploy` (또는 원하는 이름)
4. "Create repository" 클릭

## 2. 코드 푸시

```bash
# 현재 폴더: C:/zero-base13week/todo-api-deploy
git remote add origin https://github.com/YOUR_USERNAME/todo-api-deploy.git
git branch -M main
git push -u origin main
```

## 3. Railway 배포

### Railway 프로젝트 생성
1. https://railway.app 접속
2. "New Project" 클릭
3. "Deploy from GitHub repo" 선택
4. 방금 생성한 `todo-api-deploy` 저장소 선택

### PostgreSQL 추가
1. Railway 대시보드에서 "New" → "Database" → "PostgreSQL"
2. 자동으로 데이터베이스 생성

### 환경 변수 설정
Railway 프로젝트의 **Variables** 탭에서:

```
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=${{Postgres.DATABASE_URL}}
JWT_SECRET=mySecretKeyForJWTShouldBeAtLeast256BitsLongForHS256AlgorithmSecureKey2024
```

## 4. 확인사항

### 빌드 설정
- Builder: NIXPACKS (자동 감지)
- Build Command: `mvn clean install -DskipTests`
- Start Command: `java -Dserver.port=${PORT} -jar target/todo-api-1.0.0.jar`

### 배포 로그 확인
1. "Deployments" 탭에서 진행 상황 확인
2. 빌드 성공 메시지:
   - "Build successful"
   - "Deployment is live"

### API 테스트
```
https://your-app.railway.app/swagger-ui.html
```

## 트러블슈팅

### 빌드 실패 시
Railway Settings에서:
1. "Override Build Command" 체크
2. 입력: `mvn clean package -DskipTests`

### 시작 실패 시
1. "Override Start Command" 체크
2. 입력: `java -jar target/todo-api-1.0.0.jar`

## 완료!
배포가 성공하면 Railway가 제공하는 URL로 API 접속 가능합니다.