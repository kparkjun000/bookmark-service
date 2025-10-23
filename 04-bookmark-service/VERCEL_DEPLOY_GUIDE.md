# 🚀 Vercel 배포 가이드

## 현재 상황

Vercel 로그인이 진행 중입니다.

브라우저에서 다음 URL로 접속하여 로그인하세요:

```
https://vercel.com/oauth/device?user_code=RLQQ-XMVJ
```

## 로그인 완료 후 진행할 단계

### 1단계: 프로젝트 빌드 테스트

```bash
cd c:\zero-base13week\bookmark-frontend
npm run build
```

### 2단계: Vercel 배포

```bash
vercel --prod
```

### 3단계: 환경 변수 설정 (필요시)

```bash
vercel env add API_BASE_URL
# 값: https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api
```

## 자동 배포 설정

### GitHub 연동 (권장)

1. GitHub에 저장소 생성: https://github.com/new
2. 프론트엔드 코드 푸시
3. Vercel Dashboard에서 GitHub 연동
4. 자동 배포 활성화

### 수동 배포

```bash
# 프로젝트 디렉토리에서
vercel --prod

# 또는
vercel deploy --prod
```

## 배포 후 확인사항

### ✅ CORS 설정 확인

백엔드에서 CORS가 올바르게 설정되었는지 확인:

- https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users

### ✅ 프론트엔드 기능 테스트

- 사용자 목록 조회
- 북마크 목록 조회
- 공개 북마크 조회
- API 연동 확인

## 예상 배포 URL

Vercel 배포 후 다음과 같은 URL이 생성됩니다:

```
https://bookmark-frontend-[random].vercel.app
```

## 문제 해결

### CORS 오류 발생 시

1. 백엔드 CORS 설정 확인
2. 프론트엔드 API URL 확인
3. 브라우저 개발자 도구에서 네트워크 탭 확인

### 빌드 실패 시

```bash
# 의존성 재설치
rm -rf node_modules package-lock.json
npm install

# 빌드 재시도
npm run build
```

### 배포 실패 시

```bash
# 로그 확인
vercel logs

# 재배포
vercel --prod --force
```

## 완료 후 테스트

배포가 완료되면:

1. 프론트엔드 URL 접속
2. 사용자 목록 확인
3. 북마크 기능 테스트
4. API 연동 확인

---

**브라우저에서 Vercel 로그인을 완료하신 후 위 단계를 진행하세요!** 🚀
