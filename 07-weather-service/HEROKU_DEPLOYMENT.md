# Heroku 배포 가이드

이 문서는 Weather Service API를 Heroku에 배포하는 상세한 가이드입니다.

## 목차

1. [사전 준비](#사전-준비)
2. [Heroku 계정 및 CLI 설정](#heroku-계정-및-cli-설정)
3. [애플리케이션 생성 및 설정](#애플리케이션-생성-및-설정)
4. [데이터베이스 설정](#데이터베이스-설정)
5. [환경 변수 설정](#환경-변수-설정)
6. [배포 실행](#배포-실행)
7. [배포 후 확인](#배포-후-확인)
8. [유용한 Heroku 명령어](#유용한-heroku-명령어)

## 사전 준비

### 필요 항목

- Heroku 계정 (https://signup.heroku.com/)
- Git 설치
- Heroku CLI 설치
- OpenWeatherMap API Key

## Heroku 계정 및 CLI 설정

### 1. Heroku CLI 설치

#### Windows

```bash
# Installer 다운로드
https://devcenter.heroku.com/articles/heroku-cli
```

#### macOS

```bash
brew tap heroku/brew && brew install heroku
```

#### Linux

```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

### 2. Heroku 로그인

```bash
heroku login
```

브라우저가 열리면 로그인을 완료하세요.

## 애플리케이션 생성 및 설정

### 1. Heroku 앱 생성

```bash
# 앱 이름을 지정하여 생성
heroku create your-weather-service

# 또는 자동으로 이름 생성
heroku create
```

생성된 앱의 URL이 표시됩니다:

- App URL: `https://your-weather-service.herokuapp.com`
- Git URL: `https://git.heroku.com/your-weather-service.git`

### 2. Git Remote 확인

```bash
git remote -v
```

`heroku`라는 remote가 추가되었는지 확인합니다.

## 데이터베이스 설정

### 1. PostgreSQL 애드온 추가

```bash
# Essential-0 플랜 (무료)
heroku addons:create heroku-postgresql:essential-0

# 또는 다른 플랜 선택
heroku addons:create heroku-postgresql:mini
```

### 2. 데이터베이스 URL 확인

```bash
heroku config:get DATABASE_URL
```

Heroku는 자동으로 `DATABASE_URL` 환경 변수를 설정합니다.

### 3. application.yml 설정

Spring Boot가 Heroku의 `DATABASE_URL`을 자동으로 인식하도록 설정되어 있습니다:

```yaml
spring:
  datasource:
    url: ${DATABASE_URL:jdbc:postgresql://localhost:5432/weatherdb}
```

## 환경 변수 설정

### 1. OpenWeatherMap API Key 설정

```bash
heroku config:set OPENWEATHER_API_KEY=your_actual_api_key_here
```

### 2. Spring Profile 설정

```bash
heroku config:set SPRING_PROFILE=prod
```

### 3. JVM 옵션 설정 (선택사항)

```bash
heroku config:set JAVA_OPTS="-Xmx300m -Xss512k -XX:CICompilerCount=2"
```

### 4. 설정된 환경 변수 확인

```bash
heroku config
```

출력 예시:

```
=== your-weather-service Config Vars
DATABASE_URL:          postgres://...
JAVA_OPTS:            -Xmx300m -Xss512k -XX:CICompilerCount=2
OPENWEATHER_API_KEY:  your_api_key
SPRING_PROFILE:       prod
```

## 배포 실행

### 1. 코드 커밋

```bash
git add .
git commit -m "Initial deployment to Heroku"
```

### 2. Heroku에 Push

```bash
git push heroku main

# 또는 다른 브랜치를 main으로 push
git push heroku your-branch:main
```

### 3. 빌드 과정 확인

배포 중 다음과 같은 과정이 진행됩니다:

1. Java 및 Maven 설치
2. 의존성 다운로드
3. 애플리케이션 빌드
4. JAR 파일 생성
5. 애플리케이션 시작

## 배포 후 확인

### 1. 애플리케이션 열기

```bash
heroku open
```

또는 브라우저에서 직접 접속:

- 메인: `https://your-weather-service.herokuapp.com/api/v1/`
- Swagger: `https://your-weather-service.herokuapp.com/swagger-ui.html`

### 2. 로그 확인

```bash
# 실시간 로그 확인
heroku logs --tail

# 최근 로그만 확인
heroku logs --num=200
```

### 3. API 테스트

```bash
# Health Check
curl https://your-weather-service.herokuapp.com/api/v1/health

# 날씨 조회
curl "https://your-weather-service.herokuapp.com/api/v1/weather/current?city=Seoul&country=KR"
```

### 4. 데이터베이스 연결 확인

```bash
heroku pg:info
```

## 유용한 Heroku 명령어

### 앱 관리

```bash
# 앱 상태 확인
heroku ps

# 앱 재시작
heroku restart

# 앱 scale 조정
heroku ps:scale web=1

# 앱 정보 확인
heroku info
```

### 데이터베이스 관리

```bash
# DB 정보 확인
heroku pg:info

# DB 접속
heroku pg:psql

# DB 백업
heroku pg:backups:capture
heroku pg:backups:download

# DB 백업 스케줄 설정
heroku pg:backups:schedule DATABASE_URL --at "02:00 Asia/Seoul"
```

### 로그 관리

```bash
# 실시간 로그
heroku logs --tail

# 특정 dyno 로그
heroku logs --dyno=web

# 특정 개수만큼 로그 보기
heroku logs --num=500
```

### 환경 변수 관리

```bash
# 모든 환경 변수 보기
heroku config

# 특정 환경 변수 보기
heroku config:get OPENWEATHER_API_KEY

# 환경 변수 설정
heroku config:set KEY=VALUE

# 환경 변수 삭제
heroku config:unset KEY
```

### 배포 관리

```bash
# 현재 릴리스 정보
heroku releases

# 이전 버전으로 롤백
heroku rollback

# 특정 버전으로 롤백
heroku rollback v10
```

## 문제 해결

### 빌드 실패

1. **로그 확인**

   ```bash
   heroku logs --tail
   ```

2. **Java 버전 확인**
   `system.properties` 파일이 올바른지 확인:

   ```
   java.runtime.version=17
   ```

3. **Procfile 확인**
   파일명과 내용이 정확한지 확인

### 애플리케이션 시작 실패

1. **환경 변수 확인**

   ```bash
   heroku config
   ```

2. **데이터베이스 연결 확인**

   ```bash
   heroku pg:info
   ```

3. **로그에서 에러 확인**
   ```bash
   heroku logs --tail | grep -i error
   ```

### 데이터베이스 문제

1. **연결 테스트**

   ```bash
   heroku pg:psql
   \dt  # 테이블 목록 확인
   ```

2. **데이터베이스 재설정** (주의: 모든 데이터 삭제)
   ```bash
   heroku pg:reset DATABASE_URL
   heroku restart
   ```

### 메모리 부족

```bash
# JVM 옵션 조정
heroku config:set JAVA_OPTS="-Xmx250m -Xss512k -XX:CICompilerCount=2"
```

## 비용 관리

### 무료 플랜

- Web Dyno: 550-1000 시간/월 (계정 인증 여부에 따라)
- PostgreSQL: Essential-0 (무료, 1만 행 제한)

### 주의사항

- 무료 dyno는 30분 동안 요청이 없으면 sleep 상태로 전환
- Sleep에서 깨어나는데 몇 초 소요
- 한 달에 허용된 시간을 모두 사용하면 앱이 중지됨

### 비용 확인

```bash
heroku apps:info --json | grep "dyno_hours"
```

## 프로덕션 체크리스트

- [ ] OpenWeatherMap API Key 설정 완료
- [ ] DATABASE_URL 자동 설정 확인
- [ ] SPRING_PROFILE=prod 설정
- [ ] 로그 레벨 확인 (INFO 또는 WARN)
- [ ] Swagger UI 접근 가능 확인
- [ ] Health Check 엔드포인트 동작 확인
- [ ] 모든 API 엔드포인트 테스트 완료
- [ ] 에러 핸들링 동작 확인
- [ ] DB 백업 스케줄 설정 (선택)

## 추가 리소스

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Heroku Java Support](https://devcenter.heroku.com/articles/java-support)
- [Heroku PostgreSQL](https://devcenter.heroku.com/articles/heroku-postgresql)
- [Deploying Spring Boot Applications to Heroku](https://devcenter.heroku.com/articles/deploying-spring-boot-apps-to-heroku)
