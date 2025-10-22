# 수동 배포 가이드

자동 스크립트 대신 수동으로 배포하려면 아래 명령어를 순서대로 실행하세요.

## 단계별 명령어

### 1. Git 초기화 및 커밋

```powershell
cd c:\zero-base13week\10-file-service

git init
git add .
git commit -m "Initial commit for Heroku deployment"
```

### 2. Heroku 로그인

```powershell
heroku login
```

> 브라우저가 열리면 Heroku 계정으로 로그인하세요.

### 3. Heroku 앱 생성

```powershell
# 옵션 1: 자동 이름 생성
heroku create

# 옵션 2: 원하는 이름 지정
heroku create your-unique-app-name
```

### 4. PostgreSQL 데이터베이스 추가

```powershell
# Essential 플랜 (월 $5)
heroku addons:create heroku-postgresql:essential-0

# 데이터베이스 정보 확인
heroku pg:info
```

### 5. 환경 변수 설정

```powershell
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set FILE_UPLOAD_DIR=/tmp/uploads

# 설정 확인
heroku config
```

### 6. 배포

```powershell
git push heroku main
```

### 7. 앱 열기

```powershell
heroku open
```

### 8. 로그 확인

```powershell
heroku logs --tail
```

## 테스트

### Health Check

```powershell
# 브라우저에서
https://your-app-name.herokuapp.com/api/health

# 또는 PowerShell에서
Invoke-RestMethod https://your-app-name.herokuapp.com/api/health
```

### 파일 업로드 테스트

```powershell
# test-image.jpg 파일을 준비한 후
curl -X POST https://your-app-name.herokuapp.com/api/files/upload `
  -F "file=@test-image.jpg" `
  -F "description=Test from Heroku" `
  -F "generateThumbnail=true"
```

## 문제 해결

### 빌드 실패 시

```powershell
# 로컬에서 빌드 테스트
mvn clean package

# Heroku 로그 확인
heroku logs --tail
```

### 앱이 시작하지 않을 때

```powershell
# 다이노 재시작
heroku restart

# 다이노 상태 확인
heroku ps
```

### PostgreSQL 연결 실패

```powershell
# DATABASE_URL 확인
heroku config:get DATABASE_URL

# PostgreSQL 재시작
heroku pg:restart
```

## 유용한 명령어

```powershell
# 앱 정보
heroku apps:info

# 환경 변수 목록
heroku config

# 데이터베이스 콘솔
heroku pg:psql

# 원격 bash 접속
heroku run bash

# 앱 삭제 (주의!)
heroku apps:destroy --app your-app-name --confirm your-app-name
```

## 비용 관리

### 사용하지 않을 때 중지

```powershell
# 다이노 중지
heroku ps:scale web=0

# 다시 시작
heroku ps:scale web=1
```

### 현재 비용

- Eco Dyno: ~$5/월
- PostgreSQL Essential: $5/월
- 총: ~$10/월

## 업데이트 배포

코드 수정 후:

```powershell
git add .
git commit -m "Update: your changes"
git push heroku main
```

---

더 자세한 정보는 `DEPLOY_NOW.md`와 `DEPLOYMENT.md`를 참고하세요.
