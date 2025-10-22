# Heroku 배포 완료 가이드

## 현재 상태

✅ Heroku 앱 생성됨: `survey-system-api`
✅ PostgreSQL 데이터베이스 추가됨
✅ JWT_SECRET 환경 변수 설정됨
✅ Git 저장소 초기화 및 커밋 완료
✅ Heroku git remote 추가됨

## 배포 완료하기

배포를 완료하려면 다음 단계를 따라주세요:

### 1단계: 새 PowerShell 터미널 열기

현재 터미널을 종료하고 **새 PowerShell 터미널**을 여세요.

### 2단계: 프로젝트 디렉토리로 이동

```powershell
cd C:\zero-base13week\09-survey-system\survey-api
```

### 3단계: Heroku 로그인

```powershell
heroku login
```

브라우저가 열리면 Heroku 계정으로 로그인하세요.

### 4단계: 배포 실행

```powershell
git push heroku master
```

### 5단계: 배포 확인

```powershell
# 로그 확인
heroku logs --tail -a survey-system-api

# 브라우저에서 앱 열기
heroku open -a survey-system-api
```

## 배포 URL

배포가 완료되면 다음 URL에서 API를 사용할 수 있습니다:

- **API Base URL**: https://survey-system-api-dd94bac93976.herokuapp.com
- **Swagger UI**: https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

## 빠른 배포 (한 번에 실행)

모든 단계를 한 번에 실행하려면:

```powershell
# survey-api 디렉토리에서
heroku login
git push heroku master
heroku open -a survey-system-api
```

## API 테스트

배포 후 Swagger UI에서 API를 테스트할 수 있습니다:

1. https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html 접속
2. `/api/auth/signup`으로 회원가입
3. `/api/auth/login`으로 로그인하여 JWT 토큰 받기
4. "Authorize" 버튼 클릭하여 토큰 입력
5. 다른 API 엔드포인트 테스트

## 문제 해결

### 배포 실패 시

```powershell
# 로그 확인
heroku logs --tail -a survey-system-api

# 앱 재시작
heroku restart -a survey-system-api
```

### 데이터베이스 문제 시

```powershell
# PostgreSQL 상태 확인
heroku pg:info -a survey-system-api

# 데이터베이스 연결 확인
heroku config -a survey-system-api
```

### 환경 변수 확인

```powershell
heroku config -a survey-system-api
```

## 추가 설정 (선택사항)

### JWT Secret 변경

더 안전한 시크릿 키로 변경하려면:

```powershell
heroku config:set JWT_SECRET=your-new-secure-256-bit-key -a survey-system-api
```

### 로그 레벨 조정

```powershell
heroku config:set LOGGING_LEVEL_ROOT=INFO -a survey-system-api
```

## 배포 스크립트 사용 (대안)

PowerShell 스크립트를 사용하여 배포:

```powershell
.\deploy.ps1
```

또는 Bash 스크립트 (Git Bash 사용 시):

```bash
./deploy.sh
```

---

**참고**: 배포 후 첫 시작 시 약 30초~1분 정도 소요될 수 있습니다.
