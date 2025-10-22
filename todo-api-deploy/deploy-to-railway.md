# Railway 배포 실행 가이드

## 🚀 배포 단계별 실행

### 1단계: GitHub 저장소 생성 및 푸시

#### GitHub에서 새 저장소 생성
1. [GitHub](https://github.com) 로그인
2. "New repository" 클릭
3. Repository name: `todo-api` 입력
4. Public 또는 Private 선택
5. "Create repository" 클릭

#### 로컬에서 GitHub에 푸시
```bash
# GitHub 저장소 URL을 여기에 입력하세요
git remote add origin https://github.com/YOUR_USERNAME/todo-api.git
git branch -M main
git push -u origin main
```

### 2단계: Railway 배포

#### Railway 프로젝트 생성
1. [Railway](https://railway.app) 접속
2. GitHub 계정으로 로그인
3. "New Project" 클릭
4. "Deploy from GitHub repo" 선택
5. 방금 생성한 `todo-api` 저장소 선택

#### PostgreSQL 추가
1. Railway 대시보드에서 "New" 클릭
2. "Database" → "PostgreSQL" 선택
3. 자동으로 데이터베이스가 프로비저닝됩니다

#### 환경 변수 설정
Railway 프로젝트에서 "Variables" 탭으로 이동하여 다음을 추가:

```
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=${{Postgres.DATABASE_URL}}
JWT_SECRET=mySecretKeyForJWTShouldBeAtLeast256BitsLongForHS256AlgorithmSecureKey2024
```

### 3단계: 배포 확인

#### 자동 배포 진행
- GitHub 푸시 후 Railway가 자동으로 빌드 시작
- "Deployments" 탭에서 진행 상황 확인
- 빌드 완료 후 도메인 자동 생성

#### API 테스트
```bash
# 생성된 Railway URL에서 테스트
curl https://your-app.railway.app/swagger-ui.html
```

## ✅ 배포 완료 체크리스트

- [ ] GitHub 저장소 생성 완료
- [ ] 코드 푸시 완료
- [ ] Railway 프로젝트 생성 완료
- [ ] PostgreSQL 데이터베이스 추가 완료
- [ ] 환경 변수 설정 완료
- [ ] 자동 배포 진행 중
- [ ] Swagger UI 접근 가능
- [ ] API 엔드포인트 정상 동작

## 🔧 문제 해결

### 빌드 실패 시
1. Railway 대시보드 → "Deployments" → "View Logs" 확인
2. Java 21, Maven 설정 확인
3. 환경 변수 재설정

### 데이터베이스 연결 실패 시
1. PostgreSQL 서비스 상태 확인
2. DATABASE_URL 변수 확인
3. 네트워크 설정 확인

## 📝 중요 정보

- **무료 티어**: 월 5달러 크레딧 제공
- **자동 배포**: GitHub 푸시마다 자동 재배포
- **도메인**: `https://your-app-name.railway.app` 형태
- **SSL**: 자동으로 HTTPS 인증서 제공

배포 완료 후 전 세계에서 Todo API 서비스를 이용할 수 있습니다! 🎉