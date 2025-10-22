# Heroku 수동 배포 가이드

이 가이드는 JWT 인증 시스템을 Heroku에 수동으로 배포하는 방법을 단계별로 설명합니다.

## 📋 사전 요구사항

- [x] Heroku CLI 설치 ✅ (버전 8.7.1 확인됨)
- [x] Git 저장소 초기화 ✅ (완료)
- [ ] Heroku 계정

## 🚀 배포 단계

### 방법 1: 자동 스크립트 사용 (권장)

PowerShell에서 실행:

```powershell
cd c:\zero-base13week\03-auth-system
.\deploy-heroku.ps1
```

스크립트가 자동으로:

1. Heroku 로그인 확인
2. 앱 생성
3. PostgreSQL 추가
4. 환경 변수 설정
5. 배포 실행

### 방법 2: 수동 단계별 배포

#### Step 1: Heroku 로그인

```powershell
cd c:\zero-base13week\03-auth-system
heroku login
```

브라우저가 열리면 Heroku 계정으로 로그인하세요.

#### Step 2: Heroku 앱 생성

**옵션 A: 자동 이름 생성**

```powershell
heroku create
```

**옵션 B: 직접 이름 지정**

```powershell
heroku create your-app-name
# 예: heroku create my-jwt-auth-2024
```

> 💡 앱 이름은 전역적으로 고유해야 합니다. 이미 사용 중인 이름은 선택할 수 없습니다.

#### Step 3: PostgreSQL 데이터베이스 추가

```powershell
heroku addons:create heroku-postgresql:mini
```

> 📝 참고: Mini 플랜은 무료이며 개발/테스트에 적합합니다.

데이터베이스 정보 확인:

```powershell
heroku pg:info
```

#### Step 4: 환경 변수 설정

**중요: JWT Secret 설정**

보안을 위해 강력한 랜덤 문자열을 생성하세요:

```powershell
# 옵션 A: PowerShell에서 랜덤 문자열 생성
$jwtSecret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})
heroku config:set JWT_SECRET=$jwtSecret

# 옵션 B: 직접 입력 (최소 256비트 권장)
heroku config:set JWT_SECRET="your-very-secure-secret-key-at-least-256-bits-long"
```

환경 변수 확인:

```powershell
heroku config
```

#### Step 5: 배포

```powershell
git push heroku master
```

> ⏱️ 첫 배포는 Maven이 의존성을 다운로드하고 빌드하므로 5-10분 정도 소요됩니다.

배포 진행 상황:

```
-----> Building on the Heroku-22 stack
-----> Using buildpack: heroku/java
-----> Java app detected
-----> Installing JDK 21
-----> Installing Maven 3.9.6
-----> Executing Maven
       [INFO] Building jar: /tmp/build.../target/auth-system-1.0.0.jar
-----> Discovering process types
       Procfile declares types -> web
-----> Compressing...
-----> Launching...
       Released v3
       https://your-app-name.herokuapp.com/ deployed to Heroku
```

#### Step 6: 배포 확인

**로그 확인:**

```powershell
heroku logs --tail
```

**앱 열기:**

```powershell
# 메인 페이지
heroku open

# Swagger UI
heroku open /swagger-ui.html
```

**앱 상태 확인:**

```powershell
heroku ps
```

예상 출력:

```
=== web (Free): java -Dserver.port=$PORT $JAVA_OPTS -jar target/auth-system-1.0.0.jar (1)
web.1: up 2024/01/01 12:00:00 +0900 (~ 1m ago)
```

## 🧪 배포 후 테스트

### 1. API 상태 확인

브라우저에서 Swagger UI 접속:

```
https://your-app-name.herokuapp.com/swagger-ui.html
```

### 2. 회원가입 테스트

```bash
curl -X POST https://your-app-name.herokuapp.com/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
```

### 3. 로그인 테스트

```bash
curl -X POST https://your-app-name.herokuapp.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## 🔧 유용한 Heroku 명령어

### 로그 관리

```powershell
# 실시간 로그 보기
heroku logs --tail

# 최근 200줄 로그
heroku logs -n 200

# 특정 프로세스 로그
heroku logs --ps web.1 --tail
```

### 앱 관리

```powershell
# 앱 재시작
heroku restart

# 앱 정보
heroku apps:info

# 다이노 스케일링
heroku ps:scale web=1

# 앱 일시 정지 (무료 시간 절약)
heroku ps:scale web=0
```

### 데이터베이스 관리

```powershell
# PostgreSQL 접속
heroku pg:psql

# 데이터베이스 백업
heroku pg:backups:capture

# 백업 다운로드
heroku pg:backups:download

# 데이터베이스 초기화 (주의!)
heroku pg:reset DATABASE --confirm your-app-name
```

### 환경 변수 관리

```powershell
# 모든 환경 변수 보기
heroku config

# 특정 변수 확인
heroku config:get JWT_SECRET

# 변수 추가/수정
heroku config:set KEY=VALUE

# 변수 삭제
heroku config:unset KEY
```

## 🔍 문제 해결

### 배포 실패

**문제**: `git push heroku master` 실패

```
fatal: 'heroku' does not appear to be a git repository
```

**해결**:

```powershell
heroku git:remote -a your-app-name
git push heroku master
```

### 앱이 시작하지 않음

**문제**: 앱이 크래시하거나 시작하지 않음

**해결**:

```powershell
# 로그 확인
heroku logs --tail

# 일반적인 원인:
# 1. 데이터베이스 연결 실패 → DATABASE_URL 확인
# 2. 메모리 부족 → 무료 다이노는 512MB 제한
# 3. 포트 바인딩 오류 → application.yml에 ${PORT:8080} 사용 확인
```

### 데이터베이스 연결 오류

**문제**: `Unable to connect to database`

**해결**:

```powershell
# DATABASE_URL 확인
heroku config:get DATABASE_URL

# PostgreSQL 상태 확인
heroku pg:info

# 데이터베이스가 없으면 추가
heroku addons:create heroku-postgresql:mini
```

### JWT 토큰 오류

**문제**: `Invalid JWT signature`

**해결**:

```powershell
# JWT_SECRET 확인
heroku config:get JWT_SECRET

# 설정되지 않았다면 추가
heroku config:set JWT_SECRET="your-secure-secret-key"

# 앱 재시작
heroku restart
```

## 📊 모니터링

### 무료 다이노 시간 확인

```powershell
heroku ps -a your-app-name
```

### 앱 메트릭 활성화

```powershell
heroku labs:enable log-runtime-metrics
heroku logs --tail | grep "sample#memory"
```

### 애드온으로 모니터링 추가

```powershell
# Papertrail (로그 관리)
heroku addons:create papertrail:chostu

# New Relic (성능 모니터링)
heroku addons:create newrelic:wayne
```

## 🔄 업데이트 배포

코드를 수정한 후:

```powershell
# 변경사항 커밋
git add .
git commit -m "Update: description of changes"

# Heroku에 푸시
git push heroku master

# 배포 완료 후 확인
heroku logs --tail
```

## 🎯 다음 단계

### 커스텀 도메인 연결

```powershell
heroku domains:add www.your-domain.com
```

### SSL 인증서 (자동 HTTPS)

```powershell
# Heroku는 기본적으로 HTTPS 제공
# 커스텀 도메인에도 자동 SSL 적용됨
```

### 프로덕션 모드로 전환

```powershell
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

### CI/CD 설정

GitHub Actions, GitLab CI, 또는 Heroku CI 사용 가능

## 📞 도움말

- **Heroku 문서**: https://devcenter.heroku.com/
- **Heroku CLI**: https://devcenter.heroku.com/articles/heroku-cli
- **PostgreSQL on Heroku**: https://devcenter.heroku.com/articles/heroku-postgresql

## ✅ 배포 체크리스트

배포 전:

- [x] Git 저장소 초기화
- [ ] Heroku 계정 로그인
- [ ] 앱 이름 결정 (선택사항)

배포 중:

- [ ] Heroku 앱 생성
- [ ] PostgreSQL 추가
- [ ] JWT_SECRET 설정
- [ ] 코드 푸시

배포 후:

- [ ] 로그 확인
- [ ] Swagger UI 접속
- [ ] API 테스트 (회원가입/로그인)
- [ ] 데이터베이스 연결 확인

운영:

- [ ] 정기적인 로그 모니터링
- [ ] 데이터베이스 백업
- [ ] 보안 업데이트

---

**현재 상태**:

- ✅ Git 저장소: 초기화 완료
- ✅ Heroku CLI: 설치됨 (v8.7.1)
- ✅ 프로젝트 파일: 모두 준비됨
- ⏳ 다음: Heroku 로그인 및 앱 생성

배포를 시작하려면 위의 **방법 1** 또는 **방법 2**를 따라하세요!
