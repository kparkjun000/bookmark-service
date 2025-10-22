# Heroku Multi-Service Deployment Guide

이 가이드는 URL Shortener 프로젝트를 Heroku에 멀티서비스로 배포하는 방법을 설명합니다.

## 사전 요구사항

1. Heroku CLI 설치

```bash
# Windows (Chocolatey)
choco install heroku-cli

# macOS
brew tap heroku/brew && brew install heroku

# Linux
curl https://cli-assets.heroku.com/install.sh | sh
```

2. Git 설치 및 저장소 초기화

```bash
git init
git add .
git commit -m "Initial commit"
```

3. Heroku 로그인

```bash
heroku login
```

## 배포 방법 1: Git Subtree를 사용한 독립 배포

### 1단계: Heroku 앱 생성

```bash
# URL Shortener Service
heroku create your-url-shortener --region us

# Analytics Service
heroku create your-analytics --region us
```

### 2단계: PostgreSQL 애드온 추가

```bash
# URL Shortener Service에 PostgreSQL 추가
heroku addons:create heroku-postgresql:mini -a your-url-shortener

# Analytics Service는 같은 데이터베이스를 공유
# URL Shortener의 DATABASE_URL을 가져옴
heroku config:get DATABASE_URL -a your-url-shortener

# 가져온 URL을 Analytics Service에 설정
heroku config:set SPRING_DATASOURCE_URL="<복사한_DATABASE_URL>" -a your-analytics
```

### 3단계: 환경 변수 설정

```bash
# URL Shortener Service
heroku config:set BASE_URL=https://your-url-shortener.herokuapp.com -a your-url-shortener
heroku config:set JAVA_OPTS="-Xmx512m" -a your-url-shortener

# Analytics Service
heroku config:set JAVA_OPTS="-Xmx512m" -a your-analytics
```

### 4단계: 빌드팩 설정

```bash
# Java 빌드팩 추가
heroku buildpacks:set heroku/java -a your-url-shortener
heroku buildpacks:set heroku/java -a your-analytics
```

### 5단계: Git Subtree로 배포

```bash
# URL Shortener Service 배포
git subtree push --prefix url-shortener-service https://git.heroku.com/your-url-shortener.git main

# 또는 remote 추가 후
git remote add heroku-shortener https://git.heroku.com/your-url-shortener.git
git subtree push --prefix url-shortener-service heroku-shortener main

# Analytics Service 배포
git remote add heroku-analytics https://git.heroku.com/your-analytics.git
git subtree push --prefix analytics-service heroku-analytics main
```

### 6단계: 로그 확인 및 앱 열기

```bash
# 로그 확인
heroku logs --tail -a your-url-shortener
heroku logs --tail -a your-analytics

# 앱 열기
heroku open -a your-url-shortener
heroku open -a your-analytics
```

## 배포 방법 2: Multi Procfile 빌드팩 사용

이 방법은 하나의 저장소에서 여러 서비스를 관리하기 더 편리합니다.

### 1단계: Multi Procfile 빌드팩 추가

```bash
# URL Shortener Service
heroku buildpacks:add -i 1 https://github.com/heroku/heroku-buildpack-multi-procfile -a your-url-shortener
heroku buildpacks:add heroku/java -a your-url-shortener
heroku config:set PROCFILE=url-shortener-service/Procfile -a your-url-shortener

# Analytics Service
heroku buildpacks:add -i 1 https://github.com/heroku/heroku-buildpack-multi-procfile -a your-analytics
heroku buildpacks:add heroku/java -a your-analytics
heroku config:set PROCFILE=analytics-service/Procfile -a your-analytics
```

### 2단계: 배포

```bash
git push heroku-shortener main
git push heroku-analytics main
```

## 데이터베이스 마이그레이션

첫 배포 후 자동으로 테이블이 생성됩니다 (JPA의 `ddl-auto=update` 설정).

수동으로 스키마를 관리하려면:

```bash
# Heroku Postgres에 접속
heroku pg:psql -a your-url-shortener

# 스키마 확인
\dt
```

## 스케일링

```bash
# Dyno 타입 확인
heroku ps -a your-url-shortener

# 스케일 조정
heroku ps:scale web=1 -a your-url-shortener
heroku ps:scale web=1 -a your-analytics

# 성능 모니터링
heroku metrics -a your-url-shortener
```

## 환경별 설정

### Development

```bash
heroku config:set SPRING_PROFILES_ACTIVE=dev -a your-url-shortener
```

### Production

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod -a your-url-shortener
heroku config:set JAVA_OPTS="-Xmx1g -XX:+UseG1GC" -a your-url-shortener
```

## 트러블슈팅

### 1. 메모리 부족 에러

```bash
heroku config:set JAVA_OPTS="-Xmx512m -XX:MaxMetaspaceSize=128m" -a your-app
```

### 2. 빌드 실패

```bash
# Maven 캐시 클리어
heroku plugins:install heroku-repo
heroku repo:purge_cache -a your-app
```

### 3. 데이터베이스 연결 실패

```bash
# DATABASE_URL 확인
heroku config:get DATABASE_URL -a your-app

# 연결 테스트
heroku run "curl -v telnet://your-db-host:5432" -a your-app
```

### 4. 로그 레벨 조정

```bash
heroku config:set LOGGING_LEVEL_ROOT=DEBUG -a your-app
```

## 모니터링 및 관리

### 로그 관리

```bash
# 실시간 로그
heroku logs --tail -a your-app

# 최근 로그 (1000줄)
heroku logs -n 1000 -a your-app

# 특정 dyno 로그
heroku logs --ps web.1 -a your-app
```

### 데이터베이스 백업

```bash
# 백업 생성
heroku pg:backups:capture -a your-app

# 백업 다운로드
heroku pg:backups:download -a your-app
```

### 메트릭 확인

```bash
heroku metrics -a your-app
```

## 비용 최적화

1. **Eco Dyno 사용** (무료 티어 종료 후)

   - Eco dyno: $5/month (1000시간)
   - Basic dyno: $7/month per dyno

2. **데이터베이스**

   - Mini: $5/month (10,000 rows)
   - Basic: $9/month (10M rows)

3. **자동 슬립 방지** (Eco dyno)

```bash
# UptimeRobot 등의 서비스로 주기적 ping
```

## 유용한 명령어

```bash
# 앱 재시작
heroku restart -a your-app

# 환경 변수 목록
heroku config -a your-app

# 릴리스 히스토리
heroku releases -a your-app

# 이전 버전으로 롤백
heroku rollback -a your-app

# SSH 접속
heroku run bash -a your-app
```

## 참고 링크

- [Heroku Java Support](https://devcenter.heroku.com/articles/java-support)
- [Heroku PostgreSQL](https://devcenter.heroku.com/articles/heroku-postgresql)
- [Heroku Multi Procfile Buildpack](https://github.com/heroku/heroku-buildpack-multi-procfile)
