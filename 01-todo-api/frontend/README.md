# Todo App Frontend

React + Vite를 사용한 Todo 관리 애플리케이션의 프론트엔드입니다.

## 🚀 기능

- ✅ 사용자 인증 (회원가입, 로그인, JWT)
- ✅ Todo CRUD (생성, 조회, 수정, 삭제)
- ✅ 우선순위 관리 (높음, 중간, 낮음)
- ✅ 카테고리별 분류
- ✅ 상태별 필터링 (전체, 진행중, 완료)
- ✅ 검색 기능
- ✅ 반응형 디자인

## 🛠 기술 스택

- React 18
- Vite 5
- React Router DOM 6
- Axios
- CSS3

## 📦 로컬 개발 환경 설정

### 사전 요구사항

- Node.js 18 이상
- npm 또는 yarn

### 설치 및 실행

```bash
# 의존성 설치
npm install

# 개발 서버 실행 (http://localhost:3000)
npm run dev

# 프로덕션 빌드
npm run build

# 빌드된 앱 미리보기
npm run preview
```

### 환경 변수 설정

`.env` 파일을 생성하고 다음 내용을 추가하세요:

```env
VITE_API_URL=http://localhost:8080
```

프로덕션 환경에서는 실제 백엔드 API URL을 설정하세요.

## 🌐 Vercel 배포 가이드

### 1. Vercel 계정 생성 및 로그인

[Vercel](https://vercel.com)에 가입하거나 로그인합니다.

### 2. 배포 방법 A: Vercel CLI 사용

```bash
# Vercel CLI 설치 (전역)
npm install -g vercel

# frontend 디렉토리로 이동
cd frontend

# Vercel 로그인
vercel login

# 배포 (처음)
vercel

# 프로덕션 배포
vercel --prod
```

### 3. 배포 방법 B: GitHub 연동 (권장)

1. 프로젝트를 GitHub 리포지토리에 푸시
2. Vercel 대시보드에서 "New Project" 클릭
3. GitHub 리포지토리 선택
4. **Root Directory** 설정: `frontend` 선택
5. **Build Command**: `npm run build`
6. **Output Directory**: `dist`
7. **Install Command**: `npm install`
8. 환경 변수 설정:
   - `VITE_API_URL`: 백엔드 API URL (예: `https://your-api.railway.app`)

### 4. 환경 변수 설정

Vercel 프로젝트 설정에서:

- **Settings** > **Environment Variables**
- 다음 변수 추가:
  - `VITE_API_URL`: 백엔드 API URL

### 5. 자동 배포

GitHub와 연동한 경우:

- `main` 또는 `master` 브랜치에 push하면 자동으로 프로덕션 배포
- 다른 브랜치에 push하면 프리뷰 배포 생성

## 📁 프로젝트 구조

```
frontend/
├── public/              # 정적 파일
├── src/
│   ├── components/     # React 컴포넌트
│   │   ├── Login.jsx
│   │   ├── Signup.jsx
│   │   ├── TodoList.jsx
│   │   ├── TodoItem.jsx
│   │   ├── TodoForm.jsx
│   │   ├── Navbar.jsx
│   │   └── PrivateRoute.jsx
│   ├── services/       # API 서비스
│   │   └── api.js
│   ├── utils/          # 유틸리티 함수
│   │   └── auth.js
│   ├── styles/         # 스타일시트
│   │   └── global.css
│   ├── App.jsx         # 메인 App 컴포넌트
│   └── main.jsx        # 엔트리 포인트
├── index.html
├── package.json
├── vite.config.js
└── vercel.json         # Vercel 설정
```

## 🔐 인증 흐름

1. 사용자가 회원가입/로그인
2. JWT 토큰을 localStorage에 저장
3. API 요청 시 Authorization 헤더에 토큰 추가
4. 토큰이 없거나 만료된 경우 로그인 페이지로 리다이렉트

## 🎨 주요 컴포넌트

### TodoList

- Todo 목록 표시 및 관리
- 필터링, 검색 기능
- 통계 표시

### TodoItem

- 개별 Todo 항목 표시
- 완료 토글, 수정, 삭제 기능

### TodoForm

- Todo 생성/수정 폼
- 유효성 검증

### Login/Signup

- 인증 관련 폼
- JWT 토큰 관리

## 📝 API 연동

백엔드 API는 다음 엔드포인트를 사용합니다:

- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `GET /api/todos` - Todo 목록 조회
- `POST /api/todos` - Todo 생성
- `PUT /api/todos/:id` - Todo 수정
- `DELETE /api/todos/:id` - Todo 삭제
- `GET /api/categories` - 카테고리 목록 조회

## 🚨 문제 해결

### CORS 오류

백엔드에서 CORS를 허용해야 합니다. Spring Boot의 경우 `SecurityConfig`에서 설정하세요.

### 환경 변수가 적용되지 않음

- Vite는 `VITE_` 접두사가 필요합니다
- 환경 변수 변경 후 개발 서버를 재시작하세요
- Vercel에서는 프로젝트 설정에서 환경 변수를 추가하세요

### 라우팅이 작동하지 않음

`vercel.json`에 rewrite 설정이 있는지 확인하세요.

## 📄 라이선스

MIT License
