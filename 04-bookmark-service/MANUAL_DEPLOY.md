# 수동 배포 가이드

## PowerShell에서 직접 실행

### 1단계: API Key 가져오기

```
https://dashboard.heroku.com/account
```

"Reveal" 클릭 → API Key 복사

### 2단계: 환경 변수 설정

```powershell
$env:HEROKU_API_KEY = "YOUR_API_KEY_HERE"
```

**YOUR_API_KEY_HERE 부분에 복사한 API Key를 붙여넣으세요**

### 3단계: 로그인 확인

```powershell
heroku auth:whoami
```

이메일이 표시되면 성공!

### 4단계: Git Remote 설정

```powershell
git remote remove heroku
$apiKey = $env:HEROKU_API_KEY
git remote add heroku "https://heroku:$apiKey@git.heroku.com/zerobase-bookmark-service.git"
```

### 5단계: 배포

```powershell
git push heroku master
```

배포는 3-5분 소요됩니다.

### 6단계: Swagger UI 접속

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
```

## 전체 명령어 (한번에 복사)

```powershell
# API Key를 먼저 https://dashboard.heroku.com/account 에서 복사하세요!

$env:HEROKU_API_KEY = "YOUR_API_KEY_HERE"
heroku auth:whoami
git remote remove heroku
git remote add heroku "https://heroku:$($env:HEROKU_API_KEY)@git.heroku.com/zerobase-bookmark-service.git"
git push heroku master
```

## 배포 완료 확인

```powershell
# 앱 상태
heroku ps --app zerobase-bookmark-service

# 로그 확인
heroku logs --tail --app zerobase-bookmark-service

# 브라우저에서 열기
Start-Process "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
```
