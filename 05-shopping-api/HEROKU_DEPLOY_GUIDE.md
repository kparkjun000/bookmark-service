# Heroku 배포 가이드 (단계별 실행)

이 가이드는 Shopping Mall API를 Heroku에 배포하는 방법을 단계별로 설명합니다.

## 🚀 방법 1: 자동 배포 스크립트 사용 (권장)

### Windows

```cmd
deploy-to-heroku.bat
```

스크립트가 자동으로 다음 단계를 실행합니다:

1. Heroku 로그인
2. 앱 생성
3. PostgreSQL 추가
4. 배포

---

## 📋 방법 2: 수동 배포 (단계별)

### 1단계: Heroku 로그인

```bash
heroku login
```

브라우저가 열리면 Heroku 계정으로 로그인하세요.

### 2단계: Heroku 앱 생성

```bash
heroku create your-shopping-api
```

> 💡 `your-shopping-api` 부분을 원하는 앱 이름으로 변경하세요.
> 이름을 생략하면 Heroku가 자동으로 이름을 생성합니다.

**예시:**

```bash
heroku create my-shop-2024
# 또는
heroku create  # 자동 이름 생성
```

### 3단계: PostgreSQL 데이터베이스 추가

```bash
heroku addons:create heroku-postgresql:essential-0
```

### 4단계: 환경 변수 확인

```bash
heroku config
```

다음과 같은 출력을 확인할 수 있습니다:

```
DATABASE_URL: postgres://...
```

### 5단계: Heroku에 배포

```bash
git push heroku master
```

> 💡 기본 브랜치가 `main`인 경우:
>
> ```bash
> git push heroku main
> ```

배포 과정이 시작되며, 다음과 같은 로그가 표시됩니다:

```
remote: -----> Building on the Heroku-20 stack
remote: -----> Using buildpack: heroku/java
remote: -----> Java app detected
remote: -----> Installing JDK 17
remote: -----> Installing Maven 3.9.6
remote: -----> Executing Maven
remote: -----> Discovering process types
remote: -----> Compressing...
remote: -----> Launching...
```

### 6단계: 배포 확인

```bash
# 앱 열기
heroku open

# 또는 Swagger UI로 직접 이동
heroku open /swagger-ui.html
```

---

## 📱 배포 후 확인

### 애플리케이션 상태 확인

```bash
heroku ps
```

**정상 출력:**

```
=== web (Eco): mvnw spring-boot:run
web.1: up 2024/01/01 12:00:00 +0900 (~ 1m ago)
```

### 로그 확인

```bash
# 실시간 로그 보기
heroku logs --tail

# 최근 100줄 보기
heroku logs -n 100
```

### 데이터베이스 확인

```bash
# 데이터베이스 정보
heroku pg:info

# 데이터베이스 연결
heroku pg:psql
```

---

## 🌐 접속 URL

배포가 완료되면 다음 URL로 접속할 수 있습니다:

### 애플리케이션

```
https://your-app-name.herokuapp.com
```

### Swagger UI (API 문서)

```
https://your-app-name.herokuapp.com/swagger-ui.html
```

### API 엔드포인트 예시

```
https://your-app-name.herokuapp.com/api/products
https://your-app-name.herokuapp.com/api/categories
```

---

## 🔧 배포 후 설정

### 앱 이름 확인

```bash
heroku apps:info
```

### 환경 변수 설정 (필요시)

```bash
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

### Dyno 타입 변경 (성능 향상)

```bash
# Basic Dyno로 업그레이드 (never sleeps)
heroku ps:type web=basic

# Standard Dyno로 업그레이드
heroku ps:type web=standard-1x
```

### 스케일링

```bash
# Web dyno 수 증가
heroku ps:scale web=2
```

---

## 🐛 문제 해결

### 1. 빌드 실패

**원인:** Maven 빌드 오류

**해결:**

```bash
# 로컬에서 빌드 테스트
mvnw clean package

# 성공하면 다시 배포
git push heroku master
```

### 2. 애플리케이션 시작 실패

**원인:** 환경 변수 또는 데이터베이스 연결 문제

**해결:**

```bash
# 로그 확인
heroku logs --tail

# DATABASE_URL 확인
heroku config:get DATABASE_URL

# 앱 재시작
heroku restart
```

### 3. H14 - No web processes running

**원인:** Dyno가 실행 중이지 않음

**해결:**

```bash
# Dyno 스케일 확인
heroku ps

# Web dyno 실행
heroku ps:scale web=1
```

### 4. Application Error

**원인:** 다양한 이유 (로그 확인 필요)

**해결:**

```bash
# 상세 로그 확인
heroku logs --tail

# 애플리케이션 재시작
heroku restart
```

---

## 💰 비용 안내

### Eco Dyno (권장 - 개발/테스트)

- **비용:** $5/month
- **특징:** 30분 비활성 후 sleep
- **제한:** 월 1000 dyno 시간

### Basic Dyno

- **비용:** $7/month
- **특징:** Never sleeps
- **제한:** 없음

### Essential PostgreSQL

- **비용:** $5/month
- **용량:** 1GB
- **특징:** 자동 백업

---

## 📊 모니터링

### 메트릭 확인

```bash
heroku metrics
```

### 데이터베이스 상태

```bash
heroku pg:info
```

### 백업 생성

```bash
# 수동 백업
heroku pg:backups:capture

# 자동 백업 설정 (매일 새벽 2시)
heroku pg:backups:schedule DATABASE_URL --at '02:00 Asia/Seoul'
```

---

## 🔄 업데이트 배포

코드를 수정한 후 재배포:

```bash
# 코드 수정 후
git add .
git commit -m "Update: 변경 내용 설명"
git push heroku master
```

---

## 🎯 유용한 명령어 모음

```bash
# 앱 정보 보기
heroku apps:info

# 앱 이름 변경
heroku apps:rename new-name

# 앱 삭제 (주의!)
heroku apps:destroy --app app-name --confirm app-name

# 환경 변수 목록
heroku config

# 환경 변수 설정
heroku config:set KEY=value

# 환경 변수 삭제
heroku config:unset KEY

# 원격 실행
heroku run bash

# 데이터베이스 리셋 (주의! 데이터 삭제)
heroku pg:reset DATABASE_URL --confirm app-name

# 로그 다운로드
heroku logs > logs.txt

# 릴리스 히스토리
heroku releases

# 이전 버전으로 롤백
heroku rollback v123
```

---

## ✅ 배포 체크리스트

- [ ] Heroku CLI 설치 완료
- [ ] Heroku 계정 생성 및 로그인
- [ ] Git 저장소 초기화 및 커밋
- [ ] Heroku 앱 생성
- [ ] PostgreSQL 애드온 추가
- [ ] 코드 배포 (git push)
- [ ] 애플리케이션 정상 실행 확인
- [ ] Swagger UI 접속 확인
- [ ] API 엔드포인트 테스트

---

## 📞 지원

문제가 발생하면:

1. **로그 확인**: `heroku logs --tail`
2. **Heroku 상태 페이지**: https://status.heroku.com/
3. **Heroku 문서**: https://devcenter.heroku.com/

---

## 🎉 축하합니다!

배포가 완료되었습니다! 이제 다음을 즐겨보세요:

✅ **Swagger UI**: https://your-app-name.herokuapp.com/swagger-ui.html
✅ **API 테스트**: Postman, cURL, 또는 Swagger UI 사용
✅ **모니터링**: Heroku Dashboard에서 앱 상태 확인

Happy Coding! 🚀
