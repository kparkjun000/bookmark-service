# Vercel 배포 빠른 가이드

## 📦 방법 1: Vercel 대시보드 (가장 쉬움)

### 1. 준비

- GitHub에 코드 푸시
- [Vercel](https://vercel.com)에 로그인

### 2. 프로젝트 생성

1. "New Project" 클릭
2. GitHub 리포지토리 선택
3. **중요 설정:**
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend` 📁
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   - **Install Command**: `npm install`

### 3. 환경 변수 추가

Settings → Environment Variables:

```
VITE_API_URL = https://your-backend.railway.app
```

### 4. 배포

"Deploy" 버튼 클릭!

## 🚀 방법 2: Vercel CLI

```bash
# CLI 설치
npm install -g vercel

# frontend 디렉토리로 이동
cd frontend

# 로그인
vercel login

# 개발 배포 (테스트용)
vercel

# 프로덕션 배포
vercel --prod

# 환경 변수 설정
vercel env add VITE_API_URL production
# 입력: https://your-backend.railway.app
```

## ✅ 배포 후 체크리스트

- [ ] 웹사이트 접속 가능
- [ ] 회원가입 작동
- [ ] 로그인 작동
- [ ] Todo CRUD 작동
- [ ] 브라우저 콘솔에 에러 없음

## 🔧 문제 해결

### API 연결 안됨

```bash
# 1. 환경 변수 확인
vercel env ls

# 2. 재배포
vercel --prod
```

### CORS 오류

백엔드 `SecurityConfig.java`에 Vercel URL이 허용되어 있는지 확인:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:3000",
    "https://*.vercel.app"  // ✓
));
```

### 빌드 실패

```bash
# 로컬에서 빌드 테스트
npm install
npm run build
```

## 📱 커스텀 도메인

Vercel 대시보드:

1. Settings → Domains
2. 도메인 입력
3. DNS 설정 (Vercel 안내 따라하기)

## 🔄 자동 배포

GitHub 연동 후:

- `main` 브랜치 push → 자동 프로덕션 배포 🎉
- 다른 브랜치 push → 프리뷰 배포 📝

## 🎉 완료!

배포 URL: `https://your-app.vercel.app`
