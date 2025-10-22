# Heroku 배포 단계별 가이드

이 문서는 Weather Service API를 Heroku에 배포하는 구체적인 단계를 설명합니다.

## 🚀 빠른 배포 (자동 스크립트)

### Windows 사용자

```bash
.\deploy-heroku.bat
```

### macOS/Linux 사용자

```bash
chmod +x deploy-heroku.sh
./deploy-heroku.sh
```

스크립트가 모든 단계를 자동으로 처리합니다!

---

## 📋 수동 배포 단계

자동 스크립트를 사용하지 않고 직접 배포하려면 아래 단계를 따르세요.

### 1단계: 사전 준비

#### 필요한 것들

- [ ] Heroku 계정 (https://signup.heroku.com/)
- [ ] Heroku CLI 설치
- [ ] Git 설치
- [ ] OpenWeatherMap API Key

#### Heroku CLI 설치

**Windows:**

```bash
# https://devcenter.heroku.com/articles/heroku-cli 에서 다운로드
```

**macOS:**

```bash
brew tap heroku/brew && brew install heroku
```

**Linux:**

```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

### 2단계: Heroku 로그인

```bash
heroku login
```

브라우저가 열리면 로그인을 완료하세요.

### 3단계: Heroku 앱 생성

```bash
# 앱 이름을 지정하여 생성 (앱 이름은 고유해야 함)
heroku create your-weather-service

# 또는 자동으로 이름 생성
heroku create
```

**출력 예시:**

```
Creating app... done, ⬢ your-weather-service
https://your-weather-service.herokuapp.com/ | https://git.heroku.com/your-weather-service.git
```

앱 이름을 기억해두세요!

### 4단계: PostgreSQL 데이터베이스 추가

```bash
heroku addons:create heroku-postgresql:essential-0 --app your-weather-service
```

**주의:** 앱 이름을 실제 생성한 이름으로 변경하세요.

데이터베이스가 초기화될 때까지 약 30초 정도 기다리세요.

### 5단계: 환경 변수 설정

#### OpenWeatherMap API Key 설정 (필수)

```bash
heroku config:set OPENWEATHER_API_KEY=your_actual_api_key --app your-weather-service
```

**API Key 발급 방법:**

1. https://openweathermap.org/api 방문
2. 무료 계정 생성 (Free plan)
3. API Keys 탭에서 키 복사

#### 기타 환경 변수 설정

```bash
# Spring Profile 설정
heroku config:set SPRING_PROFILE=prod --app your-weather-service

# JVM 옵션 설정 (선택사항)
heroku config:set JAVA_OPTS="-Xmx300m -Xss512k -XX:CICompilerCount=2" --app your-weather-service
```

#### 설정 확인

```bash
heroku config --app your-weather-service
```

**출력 예시:**

```
=== your-weather-service Config Vars
DATABASE_URL:          postgres://...
JAVA_OPTS:            -Xmx300m -Xss512k -XX:CICompilerCount=2
OPENWEATHER_API_KEY:  your_api_key
SPRING_PROFILE:       prod
```

### 6단계: Git 저장소 설정

#### Git 초기화 (처음인 경우)

```bash
git init
git add .
git commit -m "Initial commit for Heroku deployment"
```

#### 이미 Git 저장소가 있는 경우

```bash
git add .
git commit -m "Prepare for Heroku deployment"
```

#### Heroku remote 추가

```bash
heroku git:remote --app your-weather-service
```

#### Remote 확인

```bash
git remote -v
```

**출력 예시:**

```
heroku  https://git.heroku.com/your-weather-service.git (fetch)
heroku  https://git.heroku.com/your-weather-service.git (push)
```

### 7단계: Heroku에 배포

```bash
# main 브랜치를 사용하는 경우
git push heroku main

# master 브랜치를 사용하는 경우
git push heroku master
```

**배포 과정 (5-10분 소요):**

1. ✓ 코드 업로드
2. ✓ Java 17 설치
3. ✓ Maven 설치
4. ✓ 의존성 다운로드
5. ✓ 애플리케이션 빌드
6. ✓ JAR 파일 생성
7. ✓ 애플리케이션 시작

### 8단계: 배포 확인

#### 앱 상태 확인

```bash
heroku ps --app your-weather-service
```

**정상 출력:**

```
=== web (Free): java -Dserver.port=$PORT ... (1)
web.1: up 2024/05/01 12:00:00 +0900 (~ 1m ago)
```

#### 로그 확인

```bash
heroku logs --tail --app your-weather-service
```

성공 메시지를 찾으세요:

```
Started WeatherServiceApplication in X.XXX seconds
Tomcat started on port(s): XXXXX (http)
```

#### Health Check 테스트

```bash
curl https://your-weather-service.herokuapp.com/api/v1/health
```

**정상 응답:**

```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "service": "Weather Service API",
    "version": "1.0.0"
  }
}
```

### 9단계: Swagger UI 확인

브라우저에서 접속:

```
https://your-weather-service.herokuapp.com/swagger-ui.html
```

또는 명령어로 열기:

```bash
heroku open --app your-weather-service
```

### 10단계: API 테스트

#### 현재 날씨 조회

```bash
curl "https://your-weather-service.herokuapp.com/api/v1/weather/current?city=Seoul&country=KR"
```

#### 날씨 예보 조회

```bash
curl "https://your-weather-service.herokuapp.com/api/v1/weather/forecast?city=Tokyo&country=JP&days=3"
```

---

## 🔧 배포 후 관리

### 로그 모니터링

```bash
# 실시간 로그 확인
heroku logs --tail --app your-weather-service

# 최근 100줄 확인
heroku logs -n 100 --app your-weather-service

# 에러만 확인
heroku logs --tail --app your-weather-service | grep -i error
```

### 앱 재시작

```bash
heroku restart --app your-weather-service
```

### 환경 변수 변경

```bash
# 변수 추가/수정
heroku config:set KEY=VALUE --app your-weather-service

# 변수 삭제
heroku config:unset KEY --app your-weather-service

# 전체 확인
heroku config --app your-weather-service
```

### 데이터베이스 관리

```bash
# DB 정보 확인
heroku pg:info --app your-weather-service

# DB 접속
heroku pg:psql --app your-weather-service

# 백업 생성
heroku pg:backups:capture --app your-weather-service

# 백업 다운로드
heroku pg:backups:download --app your-weather-service
```

### 스케일 조정

```bash
# Dyno 개수 확인
heroku ps --app your-weather-service

# Web dyno 1개로 설정
heroku ps:scale web=1 --app your-weather-service
```

---

## 🐛 문제 해결

### 1. 앱이 시작되지 않음

**확인 사항:**

```bash
# 로그 확인
heroku logs --tail --app your-weather-service

# 환경 변수 확인
heroku config --app your-weather-service

# DATABASE_URL이 설정되어 있는지 확인
# OPENWEATHER_API_KEY가 설정되어 있는지 확인
```

### 2. Database 연결 오류

```bash
# PostgreSQL 상태 확인
heroku pg:info --app your-weather-service

# DB가 없다면 추가
heroku addons:create heroku-postgresql:essential-0 --app your-weather-service
```

### 3. Out of Memory 에러

```bash
# JVM 메모리 설정 조정
heroku config:set JAVA_OPTS="-Xmx250m -Xss512k -XX:CICompilerCount=2" --app your-weather-service

# 앱 재시작
heroku restart --app your-weather-service
```

### 4. API 호출이 실패함

**OpenWeatherMap API Key 확인:**

```bash
# API Key가 설정되어 있는지 확인
heroku config:get OPENWEATHER_API_KEY --app your-weather-service

# 없다면 설정
heroku config:set OPENWEATHER_API_KEY=your_key --app your-weather-service
```

### 5. 빌드 실패

```bash
# pom.xml과 Java 버전 확인
# system.properties에 java.runtime.version=17 설정 확인

# 로컬에서 빌드 테스트
mvn clean package

# 성공하면 다시 배포
git add .
git commit -m "Fix build issues"
git push heroku main
```

---

## 📊 모니터링 및 성능

### 앱 성능 확인

```bash
# 메모리 사용량
heroku ps --app your-weather-service

# 요청 응답 시간 (로그에서 확인)
heroku logs --tail --app your-weather-service | grep "Completed"
```

### Heroku 대시보드 사용

브라우저에서:

```
https://dashboard.heroku.com/apps/your-weather-service
```

여기서 확인 가능:

- 앱 상태
- 메트릭 (요청 수, 응답 시간, 메모리 사용량)
- 에러 로그
- DB 상태
- 환경 변수

---

## 🔄 업데이트 배포

코드를 수정한 후 다시 배포하려면:

```bash
# 1. 변경사항 커밋
git add .
git commit -m "Update: 변경 내용 설명"

# 2. Heroku에 푸시
git push heroku main

# 3. 로그로 배포 확인
heroku logs --tail --app your-weather-service
```

---

## 💰 비용 관리

### 무료 플랜 제한

- Web Dyno: 550-1000 시간/월
- PostgreSQL: 10,000 행 제한 (Essential-0)
- 30분 비활동 시 sleep 상태
- Sleep에서 깨어나는데 몇 초 소요

### 사용량 확인

```bash
# 앱 정보
heroku apps:info --app your-weather-service

# DB 사용량
heroku pg:info --app your-weather-service
```

---

## ✅ 배포 체크리스트

배포 전:

- [ ] OpenWeatherMap API Key 발급 완료
- [ ] Heroku 계정 생성 및 CLI 설치
- [ ] Git 저장소 준비
- [ ] pom.xml 확인
- [ ] Procfile 확인
- [ ] system.properties 확인

배포 후:

- [ ] Health check 성공
- [ ] Swagger UI 접근 가능
- [ ] 현재 날씨 API 테스트 성공
- [ ] 날씨 예보 API 테스트 성공
- [ ] 로그에 에러 없음
- [ ] 데이터베이스 연결 확인

---

## 📞 도움말

문제가 계속되면:

1. **Heroku 지원 문서**: https://devcenter.heroku.com/
2. **로그 확인**: `heroku logs --tail --app your-weather-service`
3. **Heroku 상태**: https://status.heroku.com/
4. **OpenWeatherMap 상태**: https://openweathermap.org/

---

## 🎉 완료!

배포가 성공적으로 완료되었습니다!

**앱 URL:**

- 메인: `https://your-weather-service.herokuapp.com`
- Swagger: `https://your-weather-service.herokuapp.com/swagger-ui.html`
- Health: `https://your-weather-service.herokuapp.com/api/v1/health`

즐거운 개발 되세요! 🚀
