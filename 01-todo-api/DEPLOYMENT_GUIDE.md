# 🚀 Todo App 배포 가이드

Todo API 백엔드와 프론트엔드를 Railway와 Vercel에 배포하는 전체 가이드입니다.

## 📋 목차

1. [백엔드 배포 (Railway)](#백엔드-배포-railway)
2. [프론트엔드 배포 (Vercel)](#프론트엔드-배포-vercel)
3. [연동 설정](#연동-설정)
4. [문제 해결](#문제-해결)

---

## 백엔드 배포 (Railway)

### 1. Railway 준비

1. [Railway](https://railway.app) 가입 및 로그인
2. 새 프로젝트 생성

### 2. PostgreSQL 데이터베이스 추가

1. Railway 프로젝트에서 "+ New" 클릭
2. "Database" > "PostgreSQL" 선택
3. 자동으로 데이터베이스가 생성됩니다

### 3. Spring Boot 앱 배포

#### 방법 A: GitHub 연동 (권장)

1. 프로젝트를 GitHub에 푸시
2. Railway에서 "+ New" > "GitHub Repo" 선택
3. 리포지토리 선택
4. Railway가 자동으로 앱을 빌드하고 배포

#### 방법 B: Railway CLI

```bash
# Railway CLI 설치
npm install -g @railway/cli

# 로그인
railway login

# 프로젝트 초기화 (프로젝트 루트에서)
railway init

# 배포
railway up
```

### 4. 환경 변수 설정

Railway 프로젝트 설정에서 다음 환경 변수 추가:

```env
# PostgreSQL (자동 생성됨, 확인만 하세요)
DATABASE_URL=postgresql://...

# Spring 설정
SPRING_PROFILES_ACTIVE=production
JWT_SECRET=your-256-bit-secret-key-here-make-it-long-and-random

# 서버 포트 (Railway는 자동으로 설정)
PORT=8080
```

### 5. CORS 설정 확인

`src/main/java/com/example/todoapi/config/SecurityConfig.java`에서 CORS를 허용해야 합니다:

```java
.cors(cors -> cors.configurationSource(request -> {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of(
        "http://localhost:3000",
        "https://your-frontend.vercel.app"  // Vercel URL 추가
    ));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    return config;
}))
```

### 6. 배포 확인

```bash
# 헬스 체크
curl https://your-app.railway.app/api/health

# Swagger UI 확인
https://your-app.railway.app/swagger-ui.html
```

---

## 프론트엔드 배포 (Vercel)

### 1. Vercel 준비

1. [Vercel](https://vercel.com) 가입 및 로그인
2. GitHub 계정 연동

### 2. 배포 방법 A: Vercel 대시보드 (권장)

1. "New Project" 클릭
2. GitHub 리포지토리 선택
3. 프로젝트 설정:

   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   - **Install Command**: `npm install`

4. 환경 변수 추가:

   ```
   VITE_API_URL=https://your-app.railway.app
   ```

5. "Deploy" 클릭

### 3. 배포 방법 B: Vercel CLI

```bash
# Vercel CLI 설치
npm install -g vercel

# frontend 디렉토리로 이동
cd frontend

# 로그인
vercel login

# 배포
vercel

# 프로덕션 배포
vercel --prod
```

CLI 사용 시 환경 변수 설정:

```bash
vercel env add VITE_API_URL production
# Railway URL 입력: https://your-app.railway.app
```

### 4. 자동 배포 설정

GitHub 연동 후:

- `main` 브랜치 push → 자동 프로덕션 배포
- 다른 브랜치 push → 프리뷰 배포 생성

### 5. 커스텀 도메인 (선택사항)

1. Vercel 프로젝트 > Settings > Domains
2. 도메인 추가 및 DNS 설정

---

## 연동 설정

### 1. 백엔드 URL 업데이트

프론트엔드가 배포된 후, 백엔드의 CORS 설정에 Vercel URL 추가:

```java
// SecurityConfig.java
config.setAllowedOrigins(List.of(
    "http://localhost:3000",
    "https://your-app.vercel.app",
    "https://your-custom-domain.com"  // 커스텀 도메인이 있다면
));
```

변경 후 Railway에 재배포하세요.

### 2. 프론트엔드 API URL 설정

Vercel 프로젝트 설정에서 환경 변수 확인:

```
VITE_API_URL=https://your-app.railway.app
```

### 3. 테스트

1. Vercel URL 접속: `https://your-app.vercel.app`
2. 회원가입/로그인 테스트
3. Todo 생성, 수정, 삭제 테스트
4. 브라우저 콘솔에서 에러 확인

---

## 문제 해결

### 백엔드 문제

#### 데이터베이스 연결 오류

```bash
# Railway 로그 확인
railway logs

# DATABASE_URL 환경 변수 확인
railway variables
```

#### 앱이 시작되지 않음

- `system.properties`에서 Java 버전 확인 (Java 21)
- `pom.xml`의 Maven 버전 확인
- Railway 빌드 로그 확인

#### CORS 오류

- `SecurityConfig.java`에서 프론트엔드 URL이 허용되었는지 확인
- 브라우저 개발자 도구의 네트워크 탭에서 응답 헤더 확인

### 프론트엔드 문제

#### 빌드 실패

```bash
# 로컬에서 빌드 테스트
cd frontend
npm install
npm run build
```

#### API 연결 안됨

- 브라우저 콘솔에서 `VITE_API_URL` 확인
- Vercel 환경 변수 설정 확인
- 백엔드 CORS 설정 확인

#### 라우팅 404 오류

- `vercel.json`의 rewrites 설정 확인
- SPA를 위해 모든 경로를 `index.html`로 리다이렉트해야 함

#### 환경 변수가 적용되지 않음

- Vite는 `VITE_` 접두사 필요
- 환경 변수 변경 후 재배포 필요
- Vercel 대시보드에서 재배포: Deployments > ... > Redeploy

### 일반적인 문제

#### JWT 토큰 만료

- 백엔드 `application.yml`에서 토큰 만료 시간 확인
- 프론트엔드에서 자동 로그아웃 동작 확인

#### HTTPS Mixed Content 경고

- 백엔드도 HTTPS를 사용해야 함 (Railway는 기본 제공)
- HTTP와 HTTPS를 혼용하지 마세요

---

## 📊 배포 체크리스트

### 백엔드 (Railway)

- [ ] PostgreSQL 데이터베이스 생성
- [ ] GitHub 리포지토리 연동
- [ ] 환경 변수 설정 (`DATABASE_URL`, `JWT_SECRET`, `SPRING_PROFILES_ACTIVE`)
- [ ] CORS 설정 (프론트엔드 URL 허용)
- [ ] 앱 시작 확인 (헬스 체크)
- [ ] Swagger UI 접근 가능

### 프론트엔드 (Vercel)

- [ ] GitHub 리포지토리 연동
- [ ] Root Directory 설정 (`frontend`)
- [ ] 환경 변수 설정 (`VITE_API_URL`)
- [ ] 빌드 성공
- [ ] 배포 성공
- [ ] 웹사이트 접근 가능
- [ ] API 연동 테스트
- [ ] 모든 페이지 라우팅 작동

### 연동

- [ ] 회원가입 작동
- [ ] 로그인 작동
- [ ] Todo CRUD 작동
- [ ] 브라우저 콘솔에 에러 없음
- [ ] 모바일 반응형 확인

---

## 🎉 완료!

축하합니다! Todo 앱이 성공적으로 배포되었습니다.

- 🔧 백엔드: `https://your-app.railway.app`
- 🌐 프론트엔드: `https://your-app.vercel.app`
- 📚 API 문서: `https://your-app.railway.app/swagger-ui.html`

## 📞 지원

문제가 발생하면:

1. 각 플랫폼의 로그 확인 (Railway logs, Vercel logs)
2. 브라우저 개발자 도구의 콘솔 및 네트워크 탭 확인
3. 환경 변수 설정 재확인
4. CORS 설정 재확인
