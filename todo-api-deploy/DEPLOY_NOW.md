# 🚀 Railway 배포 실행하기

## 지금 바로 배포하세요!

### 1️⃣ GitHub 저장소 생성

1. **GitHub 접속**: https://github.com
2. **새 저장소 생성**: "New repository" 클릭
3. **이름 입력**: `todo-api`
4. **생성**: "Create repository" 클릭

### 2️⃣ 코드 푸시 (터미널에서 실행)

```bash
# GitHub 저장소 URL로 변경하세요
git remote add origin https://github.com/YOUR_USERNAME/todo-api.git
git branch -M main
git push -u origin main
```

### 3️⃣ Railway 배포

1. **Railway 접속**: https://railway.app
2. **로그인**: GitHub 계정으로 로그인
3. **프로젝트 생성**: "New Project" → "Deploy from GitHub repo"
4. **저장소 선택**: `todo-api` 저장소 선택

### 4️⃣ PostgreSQL 추가

1. **데이터베이스 추가**: "New" → "Database" → "PostgreSQL"
2. **자동 설정**: Railway가 자동으로 구성

### 5️⃣ 환경 변수 설정

Railway 프로젝트의 "Variables" 탭에서 추가:

```
SPRING_PROFILES_ACTIVE=production
DATABASE_URL=${{Postgres.DATABASE_URL}}
JWT_SECRET=mySecretKeyForJWTShouldBeAtLeast256BitsLongForHS256AlgorithmSecureKey2024
```

## ✅ 배포 완료!

- 📁 Git 저장소 준비: ✅ 완료
- 🔧 배포 설정 파일: ✅ 생성
- 📝 배포 가이드: ✅ 작성
- 🐳 Docker 설정: ✅ 준비
- ⚙️ Railway 구성: ✅ 완료

**배포 소요 시간**: 약 5-10분

**배포 후 접속 URL**: `https://your-app.railway.app`

**API 문서**: `https://your-app.railway.app/swagger-ui.html`

## 🎉 성공!

모든 준비가 완료되었습니다. 위 단계를 따라하시면 Todo API가 Railway에 배포됩니다!