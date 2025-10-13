# Heroku 배포 가이드

## 📋 현재 상태

✅ Git 저장소 초기화 완료
✅ Heroku 앱 생성 완료: `zerobase-bookmark-service`
✅ PostgreSQL 애드온 생성 중
✅ 코드 커밋 완료

## 🚀 배포 방법 1: 자동 스크립트 사용 (권장)

PowerShell이나 CMD에서 다음 명령어를 실행하세요:

```bash
.\deploy.bat
```

스크립트가 자동으로:
1. Heroku 로그인 확인
2. Git 상태 확인
3. 변경사항 커밋
4. PostgreSQL 확인
5. Heroku에 배포

## 🚀 배포 방법 2: 수동 명령어

PowerShell이나 CMD에서 순서대로 실행:

### 1단계: Heroku 로그인
```bash
heroku login
# 브라우저가 열리면 로그인하세요
```

### 2단계: 로그인 확인
```bash
heroku auth:whoami
```

### 3단계: PostgreSQL 준비 대기 (1-2분 소요)
```bash
heroku addons:wait postgresql-clean-34059
```

### 4단계: 배포
```bash
git push heroku master
```

### 5단계: 앱 열기
```bash
heroku open
```

## 🚀 배포 방법 3: GitHub 연동 (가장 쉬움)

### 1. GitHub에 저장소 생성
1. https://github.com/new 접속
2. Repository name: `bookmark-service`
3. Create repository 클릭

### 2. GitHub에 푸시
```bash
git remote add origin https://github.com/YOUR_USERNAME/bookmark-service.git
git branch -M main
git push -u origin main
```

### 3. Heroku에서 GitHub 연동
1. https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github 접속
2. "Connect to GitHub" 클릭
3. 저장소 선택: `bookmark-service`
4. "Enable Automatic Deploys" 클릭
5. "Deploy Branch" 클릭 (main 브랜치)

## 📱 배포 후 접속 URL

### Swagger UI (API 문서 및 테스트)
```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
```

### OpenAPI JSON
```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api-docs
```

### 메인 페이지
```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/
```

## 🔍 배포 확인 및 로그

### 앱 상태 확인
```bash
heroku ps --app zerobase-bookmark-service
```

### 로그 실시간 확인
```bash
heroku logs --tail --app zerobase-bookmark-service
```

### 데이터베이스 확인
```bash
heroku pg:info --app zerobase-bookmark-service
```

### 환경 변수 확인
```bash
heroku config --app zerobase-bookmark-service
```

## 🐛 문제 해결

### 문제 1: "App not compatible with buildpack"
```bash
heroku buildpacks:set heroku/java
```

### 문제 2: PostgreSQL이 준비되지 않음
```bash
# PostgreSQL 상태 확인
heroku addons:info postgresql-clean-34059

# 1-2분 대기 후 재배포
git commit --allow-empty -m "Redeploy"
git push heroku master
```

### 문제 3: 앱이 시작되지 않음
```bash
# 로그 확인
heroku logs --tail

# 앱 재시작
heroku restart --app zerobase-bookmark-service
```

### 문제 4: Git 인증 오류
```bash
# Heroku CLI 재로그인
heroku logout
heroku login

# 또는 HTTPS 대신 SSH 사용
heroku git:remote -a zerobase-bookmark-service
```

## 📊 초기 데이터

앱이 시작되면 자동으로 다음 샘플 데이터가 생성됩니다:

- **사용자**: demo@example.com
- **태그**: 개발, 참고자료, 튜토리얼
- **북마크**: Spring Boot, PostgreSQL, GitHub

Swagger UI에서 바로 테스트할 수 있습니다!

## 🎯 API 사용 예시

### 1. 사용자 조회
```bash
curl https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users
```

### 2. 북마크 목록 조회
```bash
curl https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/users/1/bookmarks
```

### 3. 메타데이터 추출
```bash
curl "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/public/metadata?url=https://github.com"
```

### 4. 공개 북마크 조회
```bash
curl https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/public/bookmarks
```

## 💡 추가 설정 (선택사항)

### 커스텀 도메인 연결
```bash
heroku domains:add www.yourdomain.com
```

### 환경 변수 추가
```bash
heroku config:set KEY=VALUE
```

### 로그 레벨 조정
```bash
heroku config:set LOGGING_LEVEL_ROOT=INFO
```

## 📞 지원

문제가 발생하면:
1. 로그 확인: `heroku logs --tail`
2. Heroku 상태: https://status.heroku.com/
3. 문서: https://devcenter.heroku.com/

