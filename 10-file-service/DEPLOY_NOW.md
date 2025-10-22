# 🚀 Heroku 즉시 배포 가이드

이 프로젝트는 Heroku 배포를 위한 모든 설정이 완료되어 있습니다!

## ✅ 사전 준비 완료 항목

- [x] Heroku CLI 설치됨 (v8.7.1)
- [x] Procfile 생성됨
- [x] system.properties 설정됨 (Java 21)
- [x] application-prod.yml 구성됨
- [x] pom.xml 의존성 완료

## 📋 배포 단계 (5분 소요)

### 1️⃣ Git 설정 및 커밋

```powershell
# Git 초기화 (이미 완료)
git init

# 파일 추가
git add .

# 첫 커밋
git commit -m "Initial commit for Heroku deployment"
```

### 2️⃣ Heroku 로그인

```powershell
heroku login
```

브라우저가 열리면 로그인하세요.

### 3️⃣ Heroku 앱 생성

```powershell
# 앱 이름 자동 생성
heroku create

# 또는 원하는 이름 지정 (전 세계적으로 유일해야 함)
heroku create your-file-service-app-name
```

### 4️⃣ PostgreSQL 데이터베이스 추가

```powershell
# Essential 플랜 (무료)
heroku addons:create heroku-postgresql:essential-0

# 데이터베이스 정보 확인
heroku pg:info
```

### 5️⃣ 환경 변수 설정

```powershell
# Spring 프로필 설정
heroku config:set SPRING_PROFILES_ACTIVE=prod

# 파일 업로드 디렉토리
heroku config:set FILE_UPLOAD_DIR=/tmp/uploads

# 설정 확인
heroku config
```

### 6️⃣ 배포!

```powershell
# Heroku에 푸시 (배포 시작)
git push heroku main

# 또는 다른 브랜치에서
git push heroku your-branch:main
```

### 7️⃣ 앱 열기

```powershell
# 브라우저에서 앱 열기
heroku open

# 또는 직접 URL 접속
# https://your-app-name.herokuapp.com
```

### 8️⃣ 로그 확인

```powershell
# 실시간 로그 보기
heroku logs --tail

# 최근 로그만 보기
heroku logs -n 1000
```

---

## 🎯 배포 후 테스트

### Health Check

```powershell
# PowerShell
curl https://your-app-name.herokuapp.com/api/health

# 또는 브라우저에서
# https://your-app-name.herokuapp.com/api/health
```

### 파일 업로드 테스트

```powershell
curl -X POST https://your-app-name.herokuapp.com/api/files/upload `
  -F "file=@test-image.jpg" `
  -F "description=Test from Heroku" `
  -F "generateThumbnail=true"
```

---

## 🔧 유용한 명령어

### 앱 관리

```powershell
# 앱 정보
heroku apps:info

# 다이노 상태
heroku ps

# 앱 재시작
heroku restart

# 원격 콘솔 접속
heroku run bash
```

### 데이터베이스

```powershell
# PostgreSQL 콘솔
heroku pg:psql

# 데이터베이스 백업
heroku pg:backups:capture

# 백업 다운로드
heroku pg:backups:download
```

### 환경 변수

```powershell
# 모든 환경 변수 보기
heroku config

# 특정 변수 보기
heroku config:get DATABASE_URL

# 변수 삭제
heroku config:unset VARIABLE_NAME
```

---

## ⚠️ 중요 사항

### Heroku 파일 시스템 제한

- Heroku는 **임시 파일 시스템**을 사용합니다
- 다이노가 재시작되면 `/tmp` 디렉토리의 파일이 **삭제**됩니다 (약 24시간마다)
- 영구 저장을 위해서는 **AWS S3** 같은 외부 스토리지 사용을 권장합니다

### 데이터베이스 연결

- `DATABASE_URL` 환경 변수는 Heroku가 자동으로 설정합니다
- PostgreSQL Essential 플랜 제한:
  - 1GB 스토리지
  - 20개 동시 연결
  - 월 $5 (무료 플랜 없음, 2022년 11월부터)

---

## 🐛 문제 해결

### 빌드 실패

```powershell
# 로컬에서 먼저 빌드 테스트
mvn clean package

# 빌드 로그 자세히 보기
heroku logs --tail
```

### 앱이 시작되지 않음

```powershell
# 로그 확인
heroku logs --tail

# 다이노 재시작
heroku restart

# 스케일 확인
heroku ps:scale web=1
```

### 데이터베이스 연결 실패

```powershell
# DATABASE_URL 확인
heroku config:get DATABASE_URL

# PostgreSQL 재시작
heroku pg:restart

# 연결 확인
heroku pg:info
```

---

## 📊 모니터링

### Heroku Dashboard

https://dashboard.heroku.com/apps/your-app-name

### 메트릭 확인

```powershell
# Dyno 메트릭
heroku metrics:web

# PostgreSQL 메트릭
heroku pg:info
```

---

## 💰 비용 관리

### 현재 리소스

- **Web Dyno (Eco)**: ~$5/월
- **PostgreSQL (Essential)**: $5/월
- **총 예상 비용**: ~$10/월

### 비용 절감

```powershell
# 사용하지 않을 때 다이노 중지
heroku ps:scale web=0

# 다시 시작
heroku ps:scale web=1
```

---

## 🔄 업데이트 배포

코드를 수정한 후:

```powershell
# 변경사항 커밋
git add .
git commit -m "Update: your changes"

# Heroku에 푸시
git push heroku main

# 배포 확인
heroku logs --tail
```

---

## 🎉 배포 완료!

배포가 성공하면 다음 URL에서 앱에 접속할 수 있습니다:

- **API Base URL**: `https://your-app-name.herokuapp.com`
- **Health Check**: `https://your-app-name.herokuapp.com/api/health`
- **API Docs**: README.md, API_EXAMPLES.md 참고

---

## 📚 추가 리소스

- [Heroku 공식 문서](https://devcenter.heroku.com/)
- [DEPLOYMENT.md](DEPLOYMENT.md) - 상세 배포 가이드
- [API_EXAMPLES.md](API_EXAMPLES.md) - API 사용 예제
- [README.md](README.md) - 전체 프로젝트 문서

---

## ✨ 다음 단계

1. **커스텀 도메인 설정**

   ```powershell
   heroku domains:add www.yourdomain.com
   ```

2. **SSL 인증서 자동 프로비저닝**

   ```powershell
   heroku certs:auto:enable
   ```

3. **CI/CD 파이프라인 구성**

   - GitHub Actions
   - Heroku Pipelines

4. **모니터링 도구 추가**
   - New Relic
   - Datadog
   - Scout APM

---

**Happy Deploying! 🚀**
