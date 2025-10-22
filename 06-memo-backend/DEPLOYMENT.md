# Heroku 배포 가이드

이 문서는 Memo Backend API를 Heroku에 배포하는 상세한 가이드입니다.

## 📋 준비사항

1. [Heroku 계정](https://signup.heroku.com/) 생성
2. [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli) 설치
3. Git 설치 및 설정
4. 프로젝트가 Git 저장소로 초기화되어 있어야 함

## 🚀 배포 단계

### 1. Heroku CLI 로그인

```bash
heroku login
```

브라우저가 열리면 Heroku 계정으로 로그인합니다.

### 2. Heroku 앱 생성

```bash
# 앱 이름을 지정하여 생성
heroku create memo-backend-app

# 또는 자동으로 이름 생성
heroku create
```

**결과:**

```
Creating app... done, ⬢ memo-backend-app
https://memo-backend-app.herokuapp.com/ | https://git.heroku.com/memo-backend-app.git
```

### 3. PostgreSQL 데이터베이스 추가

```bash
# Mini 플랜 (무료)
heroku addons:create heroku-postgresql:mini

# 또는 Essential 플랜
heroku addons:create heroku-postgresql:essential-0
```

**확인:**

```bash
heroku addons
```

### 4. 환경 변수 확인

Heroku는 자동으로 `DATABASE_URL` 환경 변수를 설정합니다.

```bash
heroku config
```

**출력 예시:**

```
DATABASE_URL: postgres://username:password@host:5432/database
```

### 5. 프로덕션 프로필 활성화 (선택사항)

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

### 6. Git 저장소 초기화 (아직 안 했다면)

```bash
git init
git add .
git commit -m "Initial commit"
```

### 7. Heroku 원격 저장소 추가

```bash
# 앱 생성 시 자동으로 추가되었다면 생략
heroku git:remote -a memo-backend-app
```

### 8. 배포

```bash
git push heroku main

# 또는 다른 브랜치에서 배포하는 경우
git push heroku master:main
```

**배포 로그 예시:**

```
remote: -----> Building on the Heroku-20 stack
remote: -----> Using buildpack: heroku/java
remote: -----> Java app detected
remote: -----> Installing JDK 21... done
remote: -----> Installing Maven 3.9.6... done
remote: -----> Executing Maven
remote:        [INFO] BUILD SUCCESS
remote: -----> Discovering process types
remote:        Procfile declares types -> web
remote: -----> Compressing...
remote: -----> Launching...
remote:        Released v5
remote:        https://memo-backend-app.herokuapp.com/ deployed to Heroku
```

### 9. 애플리케이션 열기

```bash
heroku open
```

### 10. Swagger UI 접속

```bash
# 브라우저에서 직접 접속
https://memo-backend-app.herokuapp.com/swagger-ui.html
```

## 📊 모니터링 및 관리

### 로그 확인

```bash
# 실시간 로그 보기
heroku logs --tail

# 최근 로그 보기
heroku logs -n 200
```

### 앱 상태 확인

```bash
heroku ps
```

### 데이터베이스 접속

```bash
heroku pg:psql
```

### 앱 재시작

```bash
heroku restart
```

### 스케일 조정

```bash
# Dyno 수 조정
heroku ps:scale web=1
```

## 🔧 환경 변수 관리

### 환경 변수 설정

```bash
heroku config:set KEY=VALUE
```

### 환경 변수 조회

```bash
heroku config
```

### 환경 변수 삭제

```bash
heroku config:unset KEY
```

## 🗄️ 데이터베이스 관리

### 데이터베이스 정보 확인

```bash
heroku pg:info
```

### 백업 생성

```bash
heroku pg:backups:capture
```

### 백업 목록 조회

```bash
heroku pg:backups
```

### 백업 복원

```bash
heroku pg:backups:restore
```

### 데이터베이스 초기화

```bash
heroku pg:reset DATABASE
```

## 🔄 지속적 배포 설정 (GitHub 연동)

### 1. Heroku 대시보드 접속

https://dashboard.heroku.com/

### 2. 앱 선택 → Deploy 탭

### 3. Deployment method 선택

- **GitHub** 선택
- GitHub 계정 연결
- 저장소 검색 및 연결

### 4. Automatic deploys 활성화

- **main** 브랜치 선택
- **Wait for CI to pass before deploy** 체크 (CI 사용 시)
- **Enable Automatic Deploys** 클릭

이제 `main` 브랜치에 푸시할 때마다 자동으로 배포됩니다.

## 🐛 문제 해결

### 1. 빌드 실패

**원인:** Java 버전 불일치

**해결:**

```bash
# system.properties 파일 확인
cat system.properties

# 내용: java.runtime.version=21
```

### 2. 데이터베이스 연결 실패

**원인:** DATABASE_URL 환경 변수 누락

**해결:**

```bash
heroku config
# DATABASE_URL이 있는지 확인

# 없으면 PostgreSQL 애드온 추가
heroku addons:create heroku-postgresql:mini
```

### 3. 포트 바인딩 오류

**원인:** $PORT 환경 변수를 사용하지 않음

**해결:**

```yaml
# application.yml 확인
server:
  port: ${PORT:8080}
```

### 4. 애플리케이션 크래시

**로그 확인:**

```bash
heroku logs --tail
```

**일반적인 해결책:**

- Java 버전 확인
- 의존성 충돌 확인
- 메모리 설정 조정

## 💰 비용 관리

### 무료 플랜 제한사항

- Dyno: 550-1000 시간/월 (계정 인증 시 1000시간)
- PostgreSQL: 10,000 rows
- 30분 비활동 후 슬립 모드

### 앱 깨우기

슬립 모드 방지를 위한 방법:

1. **UptimeRobot 사용** (권장)

   - https://uptimerobot.com/
   - 5분마다 핑 전송

2. **Heroku Scheduler 애드온**
   ```bash
   heroku addons:create scheduler:standard
   ```

### 비용 확인

```bash
heroku billing
```

## 📈 성능 최적화

### 1. JVM 옵션 설정

```bash
heroku config:set JAVA_OPTS="-Xmx512m -Xms256m"
```

### 2. Connection Pool 설정

```yaml
# application-prod.yml
spring:
  datasource:
    hikari:
      maximum-pool-size: 5
      minimum-idle: 2
```

### 3. 로깅 레벨 조정

```yaml
logging:
  level:
    root: WARN
    com.zerobase.memobackend: INFO
```

## 🔒 보안 설정

### HTTPS 강제

Heroku는 기본적으로 HTTPS를 지원합니다.

### 환경 변수 보안

민감한 정보는 절대 코드에 하드코딩하지 말고 환경 변수를 사용하세요:

```bash
heroku config:set SECRET_KEY=your-secret-key
```

## 📱 도메인 설정

### 커스텀 도메인 추가

```bash
heroku domains:add www.example.com
```

### DNS 설정

```
Type: CNAME
Name: www
Value: memo-backend-app.herokuapp.com
```

## 🎯 체크리스트

배포 전 확인사항:

- [ ] `pom.xml`에 모든 필요한 의존성이 포함되어 있는가?
- [ ] `Procfile`이 올바르게 설정되어 있는가?
- [ ] `system.properties`에 Java 21이 명시되어 있는가?
- [ ] 환경 변수가 올바르게 설정되어 있는가?
- [ ] 데이터베이스 연결 설정이 올바른가?
- [ ] API 엔드포인트가 정상 작동하는가?
- [ ] Swagger UI에 접근할 수 있는가?

## 🆘 지원

문제가 발생하면:

1. [Heroku Dev Center](https://devcenter.heroku.com/)
2. [Heroku Status](https://status.heroku.com/)
3. [Stack Overflow](https://stackoverflow.com/questions/tagged/heroku)

---

**축하합니다! 🎉**

이제 Memo Backend API가 Heroku에 성공적으로 배포되었습니다!
