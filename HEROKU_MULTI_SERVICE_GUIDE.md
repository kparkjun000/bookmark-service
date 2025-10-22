# Heroku 멀티서비스 배포 가이드

## 📋 목차

1. [개요](#개요)
2. [서비스 목록](#서비스-목록)
3. [사전 준비](#사전-준비)
4. [배포 방법](#배포-방법)
5. [서비스 관리](#서비스-관리)
6. [문제 해결](#문제-해결)

---

## 📖 개요

이 프로젝트는 **하나의 Git 저장소**에서 **10개의 독립적인 Spring Boot 서비스**를 관리하고 배포하는 **멀티서비스 모노레포**입니다.

### 멀티서비스의 장점

- ✅ 하나의 저장소에서 모든 서비스 관리
- ✅ 각 서비스는 독립적인 Heroku 앱으로 배포
- ✅ 서비스별 독립적인 URL, 환경변수, 데이터베이스
- ✅ 개별 또는 일괄 배포 가능
- ✅ 서비스 간 의존성 최소화

### 아키텍처

```
zero-base13week/ (Git 저장소)
├── 01-todo-api/          → Heroku App: zb-todo-api
├── 02-blog-api/          → Heroku App: zb-blog-api
├── 03-auth-system/       → Heroku App: zb-auth-system
├── 04-bookmark-service/  → Heroku App: zb-bookmark-service
├── 05-shopping-api/      → Heroku App: zb-shopping-api
├── 06-memo-backend/      → Heroku App: zb-memo-backend
├── 07-weather-service/   → Heroku App: zb-weather-service
├── 08-url-shortener/     → Heroku App: zb-url-shortener
├── 09-survey-system/     → Heroku App: zb-survey-system
└── 10-file-service/      → Heroku App: zb-file-service
```

---

## 🗂️ 서비스 목록

| #   | 서비스명         | 폴더                          | Heroku 앱 이름        | Java | 설명                       |
| --- | ---------------- | ----------------------------- | --------------------- | ---- | -------------------------- |
| 1   | Todo API         | `01-todo-api`                 | `zb-todo-api`         | 17   | 할 일 관리 CRUD            |
| 2   | Blog API         | `02-blog-api`                 | `zb-blog-api`         | 17   | 블로그 게시글 및 카테고리  |
| 3   | Auth System      | `03-auth-system`              | `zb-auth-system`      | 21   | JWT 인증/권한 관리         |
| 4   | Bookmark Service | `04-bookmark-service`         | `zb-bookmark-service` | 21   | URL 북마크 및 태그         |
| 5   | Shopping API     | `05-shopping-api`             | `zb-shopping-api`     | 17   | 쇼핑몰 상품/주문 관리      |
| 6   | Memo Backend     | `06-memo-backend`             | `zb-memo-backend`     | 21   | 텍스트 메모 및 검색        |
| 7   | Weather Service  | `07-weather-service`          | `zb-weather-service`  | 17   | 외부 API 연동 날씨 정보    |
| 8   | URL Shortener    | `08-url-shortener`            | `zb-url-shortener`    | 17   | URL 단축 및 클릭 통계      |
| 9   | Survey System    | `09-survey-system/survey-api` | `zb-survey-system`    | 21   | 설문조사 생성/응답 수집    |
| 10  | File Service     | `10-file-service`             | `zb-file-service`     | 21   | 파일 업로드 및 이미지 처리 |

### 데이터베이스

모든 서비스는 독립적인 **PostgreSQL 데이터베이스**를 사용합니다.

---

## 🛠️ 사전 준비

### 1. Heroku CLI 설치

Windows PowerShell에서 설치:

```powershell
# Chocolatey를 통한 설치
choco install heroku-cli

# 또는 다운로드
# https://devcenter.heroku.com/articles/heroku-cli
```

설치 확인:

```powershell
heroku --version
```

### 2. Heroku 로그인

```powershell
heroku login
```

브라우저가 열리면 로그인합니다.

### 3. Git 저장소 확인

프로젝트 루트에서:

```powershell
cd C:\zero-base13week
git status
```

Git이 초기화되지 않았다면:

```powershell
git init
git add .
git commit -m "Initial commit for multi-service deployment"
```

### 4. Java 및 Maven 확인

각 서비스별로 필요한 Java 버전이 다릅니다:

- Java 17: 서비스 1, 2, 5, 7, 8
- Java 21: 서비스 3, 4, 6, 9, 10

```powershell
java -version
mvn -version
```

---

## 🚀 배포 방법

### 방법 1: 전체 서비스 일괄 배포

모든 서비스를 한 번에 배포합니다:

```powershell
.\deploy-all-services.ps1
```

배포 모드 선택:

1. **전체 서비스 배포** - 10개 모두 배포
2. **개별 서비스 선택** - 특정 서비스만 선택 배포
3. **앱만 생성** - 배포 없이 Heroku 앱만 생성

### 방법 2: 단일 서비스 배포

특정 서비스 하나만 배포:

```powershell
.\deploy-single-service.ps1
```

서비스 번호를 입력하면 해당 서비스만 배포됩니다.

### 방법 3: 수동 배포

특정 서비스를 수동으로 배포:

```powershell
# 1. Heroku 앱 생성
heroku create zb-todo-api --region us

# 2. PostgreSQL 추가
heroku addons:create heroku-postgresql:essential-0 --app zb-todo-api

# 3. Java 버전 설정
heroku config:set JAVA_VERSION=17 --app zb-todo-api

# 4. Git remote 추가
git remote add heroku-todo https://git.heroku.com/zb-todo-api.git

# 5. Subtree를 사용해 특정 폴더만 배포
git subtree push --prefix 01-todo-api heroku-todo main
```

---

## 🔧 서비스 관리

### 배포된 서비스 URL 확인

각 서비스는 다음 형식의 URL을 갖습니다:

```
https://zb-todo-api.herokuapp.com
https://zb-blog-api.herokuapp.com
https://zb-auth-system.herokuapp.com
...
```

API 문서 (Swagger):

```
https://zb-todo-api.herokuapp.com/swagger-ui/index.html
```

### 로그 확인

실시간 로그 확인:

```powershell
heroku logs --tail --app zb-todo-api
```

최근 100줄 로그:

```powershell
heroku logs -n 100 --app zb-todo-api
```

### 앱 재시작

```powershell
heroku restart --app zb-todo-api
```

### 환경변수 관리

환경변수 확인:

```powershell
heroku config --app zb-todo-api
```

환경변수 설정:

```powershell
heroku config:set KEY=VALUE --app zb-todo-api
```

환경변수 삭제:

```powershell
heroku config:unset KEY --app zb-todo-api
```

### 데이터베이스 관리

데이터베이스 정보:

```powershell
heroku pg:info --app zb-todo-api
```

데이터베이스 접속:

```powershell
heroku pg:psql --app zb-todo-api
```

백업 생성:

```powershell
heroku pg:backups:capture --app zb-todo-api
```

백업 복원:

```powershell
heroku pg:backups:restore --app zb-todo-api
```

### 앱 스케일 조정

dyno 수 조정:

```powershell
heroku ps:scale web=1 --app zb-todo-api
```

dyno 타입 변경:

```powershell
heroku ps:resize web=basic --app zb-todo-api
```

### 앱 상태 확인

```powershell
heroku ps --app zb-todo-api
```

---

## 🔑 특별 환경변수

### JWT 인증 서비스 (Auth System, Survey System)

JWT_SECRET 설정:

```powershell
heroku config:set JWT_SECRET=your-secret-key --app zb-auth-system
heroku config:set JWT_SECRET=your-secret-key --app zb-survey-system
```

**보안**: 프로덕션 환경에서는 강력한 랜덤 키를 사용하세요.

### 날씨 서비스 (Weather Service)

OpenWeatherMap API 키 설정:

```powershell
heroku config:set WEATHER_API_KEY=your-api-key --app zb-weather-service
```

API 키 발급: https://openweathermap.org/api

---

## 🔄 서비스 업데이트

코드를 수정한 후 다시 배포:

### 전체 재배포

```powershell
git add .
git commit -m "Update services"
.\deploy-all-services.ps1
```

### 특정 서비스만 재배포

```powershell
git add .
git commit -m "Update todo-api"
git subtree push --prefix 01-todo-api heroku-todo main
```

---

## 🐛 문제 해결

### 1. 빌드 실패

**증상**: 배포 중 빌드 오류 발생

**해결**:

```powershell
# 로그 확인
heroku logs --tail --app zb-todo-api

# 로컬에서 빌드 테스트
cd 01-todo-api
mvn clean package
```

### 2. 데이터베이스 연결 오류

**증상**: Application failed to start - DATABASE_URL not found

**해결**:

```powershell
# PostgreSQL 애드온 확인
heroku addons --app zb-todo-api

# 없으면 추가
heroku addons:create heroku-postgresql:essential-0 --app zb-todo-api

# 데이터베이스 URL 확인
heroku config:get DATABASE_URL --app zb-todo-api
```

### 3. Java 버전 불일치

**증상**: Unsupported class file major version

**해결**:

```powershell
# Java 버전 확인
heroku config:get JAVA_VERSION --app zb-todo-api

# Java 버전 설정 (서비스에 맞게)
heroku config:set JAVA_VERSION=17 --app zb-todo-api
# 또는
heroku config:set JAVA_VERSION=21 --app zb-auth-system
```

### 4. Git Subtree Push 실패

**증상**: Updates were rejected because the remote contains work

**해결**:

```powershell
# Force push (주의: 원격의 변경사항을 덮어씁니다)
git push heroku-todo `git subtree split --prefix 01-todo-api main`:main --force
```

### 5. 앱이 시작되지 않음

**증상**: Application Error 또는 H10 Error

**해결**:

```powershell
# 로그 확인
heroku logs --tail --app zb-todo-api

# 재시작 시도
heroku restart --app zb-todo-api

# dyno 상태 확인
heroku ps --app zb-todo-api
```

### 6. 메모리 부족

**증상**: R14 - Memory quota exceeded

**해결**:

```powershell
# dyno 타입 업그레이드
heroku ps:resize web=standard-1x --app zb-todo-api
```

---

## 📊 모니터링

### Heroku Dashboard

웹 브라우저에서:

```
https://dashboard.heroku.com/apps/zb-todo-api
```

### 메트릭 확인

```powershell
heroku ps --app zb-todo-api
heroku pg:info --app zb-todo-api
```

### 알림 설정

Heroku Dashboard에서 알림을 설정할 수 있습니다:

- Dyno 재시작 알림
- 빌드 실패 알림
- 데이터베이스 알림

---

## 💰 비용 관리

### Free Tier 제한

- Eco Dyno: $5/월 (1000시간/월)
- Essential-0 PostgreSQL: $5/월
- 무활동 시 슬립 모드

### 비용 최적화

1. 사용하지 않는 앱 삭제
2. Dyno를 Eco 타입으로 유지
3. 데이터베이스 플랜 검토

### 앱 삭제

```powershell
heroku apps:destroy --app zb-todo-api --confirm zb-todo-api
```

---

## 📚 추가 리소스

### Heroku 문서

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Java on Heroku](https://devcenter.heroku.com/categories/java-support)
- [Heroku Postgres](https://devcenter.heroku.com/categories/heroku-postgres)

### Spring Boot 배포

- [Spring Boot Heroku 가이드](https://spring.io/guides/gs/spring-boot-heroku/)

---

## 🎯 체크리스트

배포 전 확인사항:

- [ ] Heroku CLI 설치 확인
- [ ] Heroku 계정 로그인
- [ ] Git 저장소 초기화
- [ ] 모든 서비스 로컬 빌드 테스트
- [ ] 환경변수 준비 (JWT_SECRET, WEATHER_API_KEY 등)
- [ ] Procfile 존재 확인
- [ ] system.properties 존재 확인

배포 후 확인사항:

- [ ] 모든 서비스 URL 접속 확인
- [ ] Swagger UI 동작 확인
- [ ] 데이터베이스 연결 확인
- [ ] API 엔드포인트 테스트
- [ ] 로그에 에러 없는지 확인

---

## 🆘 지원

문제가 발생하면:

1. 로그를 먼저 확인하세요: `heroku logs --tail --app <app-name>`
2. [Heroku Status](https://status.heroku.com/)에서 시스템 상태 확인
3. [Heroku Community](https://help.heroku.com/)에서 도움 요청

---

## 📝 라이선스

이 프로젝트는 Zero Base 13주차 과제입니다.

---

**작성일**: 2025년 10월 22일  
**버전**: 1.0.0
