# 서비스별 명령어 참조

## 전체 서비스 관리

### 상태 확인

```powershell
.\services-status.ps1
```

### 전체 배포

```powershell
.\deploy-all-services.ps1
```

### 앱 생성

```powershell
.\create-all-apps.ps1
```

---

## 1. Todo API

### 기본 명령어

```powershell
# 로그
heroku logs --tail --app zb-todo-api

# 재시작
heroku restart --app zb-todo-api

# 환경변수
heroku config --app zb-todo-api

# DB 접속
heroku pg:psql --app zb-todo-api
```

### 배포

```powershell
git subtree push --prefix 01-todo-api heroku-todo main
```

### URL

- **API**: https://zb-todo-api.herokuapp.com
- **Swagger**: https://zb-todo-api.herokuapp.com/swagger-ui/index.html

---

## 2. Blog API

### 기본 명령어

```powershell
heroku logs --tail --app zb-blog-api
heroku restart --app zb-blog-api
heroku config --app zb-blog-api
heroku pg:psql --app zb-blog-api
```

### 배포

```powershell
git subtree push --prefix 02-blog-api heroku-blog main
```

### URL

- **API**: https://zb-blog-api.herokuapp.com
- **Swagger**: https://zb-blog-api.herokuapp.com/swagger-ui/index.html

---

## 3. Auth System

### 기본 명령어

```powershell
heroku logs --tail --app zb-auth-system
heroku restart --app zb-auth-system
heroku config --app zb-auth-system
heroku pg:psql --app zb-auth-system
```

### JWT Secret 설정

```powershell
heroku config:set JWT_SECRET=your-secret-key --app zb-auth-system
```

### 배포

```powershell
git subtree push --prefix 03-auth-system heroku-auth main
```

### URL

- **API**: https://zb-auth-system.herokuapp.com
- **Swagger**: https://zb-auth-system.herokuapp.com/swagger-ui/index.html

---

## 4. Bookmark Service

### 기본 명령어

```powershell
heroku logs --tail --app zb-bookmark-service
heroku restart --app zb-bookmark-service
heroku config --app zb-bookmark-service
heroku pg:psql --app zb-bookmark-service
```

### 배포

```powershell
git subtree push --prefix 04-bookmark-service heroku-bookmark main
```

### URL

- **API**: https://zb-bookmark-service.herokuapp.com
- **Swagger**: https://zb-bookmark-service.herokuapp.com/swagger-ui/index.html

---

## 5. Shopping API

### 기본 명령어

```powershell
heroku logs --tail --app zb-shopping-api
heroku restart --app zb-shopping-api
heroku config --app zb-shopping-api
heroku pg:psql --app zb-shopping-api
```

### 배포

```powershell
git subtree push --prefix 05-shopping-api heroku-shopping main
```

### URL

- **API**: https://zb-shopping-api.herokuapp.com
- **Swagger**: https://zb-shopping-api.herokuapp.com/swagger-ui/index.html

---

## 6. Memo Backend

### 기본 명령어

```powershell
heroku logs --tail --app zb-memo-backend
heroku restart --app zb-memo-backend
heroku config --app zb-memo-backend
heroku pg:psql --app zb-memo-backend
```

### 배포

```powershell
git subtree push --prefix 06-memo-backend heroku-memo main
```

### URL

- **API**: https://zb-memo-backend.herokuapp.com
- **Swagger**: https://zb-memo-backend.herokuapp.com/swagger-ui/index.html

---

## 7. Weather Service

### 기본 명령어

```powershell
heroku logs --tail --app zb-weather-service
heroku restart --app zb-weather-service
heroku config --app zb-weather-service
heroku pg:psql --app zb-weather-service
```

### Weather API Key 설정

```powershell
heroku config:set WEATHER_API_KEY=your-api-key --app zb-weather-service
```

### 배포

```powershell
git subtree push --prefix 07-weather-service heroku-weather main
```

### URL

- **API**: https://zb-weather-service.herokuapp.com
- **Swagger**: https://zb-weather-service.herokuapp.com/swagger-ui/index.html

---

## 8. URL Shortener

### 기본 명령어

```powershell
heroku logs --tail --app zb-url-shortener
heroku restart --app zb-url-shortener
heroku config --app zb-url-shortener
heroku pg:psql --app zb-url-shortener
```

### 배포

```powershell
git subtree push --prefix 08-url-shortener heroku-url main
```

### URL

- **API**: https://zb-url-shortener.herokuapp.com
- **Swagger**: https://zb-url-shortener.herokuapp.com/swagger-ui/index.html

---

## 9. Survey System

### 기본 명령어

```powershell
heroku logs --tail --app zb-survey-system
heroku restart --app zb-survey-system
heroku config --app zb-survey-system
heroku pg:psql --app zb-survey-system
```

### JWT Secret 설정

```powershell
heroku config:set JWT_SECRET=your-secret-key --app zb-survey-system
```

### 배포

```powershell
git subtree push --prefix 09-survey-system/survey-api heroku-survey main
```

### URL

- **API**: https://zb-survey-system.herokuapp.com
- **Swagger**: https://zb-survey-system.herokuapp.com/swagger-ui/index.html

---

## 10. File Service

### 기본 명령어

```powershell
heroku logs --tail --app zb-file-service
heroku restart --app zb-file-service
heroku config --app zb-file-service
heroku pg:psql --app zb-file-service
```

### 배포

```powershell
git subtree push --prefix 10-file-service heroku-file main
```

### URL

- **API**: https://zb-file-service.herokuapp.com
- **Swagger**: https://zb-file-service.herokuapp.com/swagger-ui/index.html

---

## 공통 작업

### 앱 삭제 (주의!)

```powershell
heroku apps:destroy --app <app-name> --confirm <app-name>
```

### 데이터베이스 백업

```powershell
heroku pg:backups:capture --app <app-name>
heroku pg:backups:download --app <app-name>
```

### 데이터베이스 복원

```powershell
heroku pg:backups:restore --app <app-name>
```

### Dyno 스케일 조정

```powershell
heroku ps:scale web=1 --app <app-name>
```

### Dyno 타입 변경

```powershell
heroku ps:resize web=basic --app <app-name>
```

### 환경변수 일괄 설정

```powershell
heroku config:set VAR1=value1 VAR2=value2 --app <app-name>
```

### 대시보드 열기

```powershell
heroku open --app <app-name>
```

---

## 유용한 단축 명령어

### Git Remote 추가 (전체)

```powershell
git remote add heroku-todo https://git.heroku.com/zb-todo-api.git
git remote add heroku-blog https://git.heroku.com/zb-blog-api.git
git remote add heroku-auth https://git.heroku.com/zb-auth-system.git
git remote add heroku-bookmark https://git.heroku.com/zb-bookmark-service.git
git remote add heroku-shopping https://git.heroku.com/zb-shopping-api.git
git remote add heroku-memo https://git.heroku.com/zb-memo-backend.git
git remote add heroku-weather https://git.heroku.com/zb-weather-service.git
git remote add heroku-url https://git.heroku.com/zb-url-shortener.git
git remote add heroku-survey https://git.heroku.com/zb-survey-system.git
git remote add heroku-file https://git.heroku.com/zb-file-service.git
```

---

## 모니터링

### 실시간 로그 (여러 앱 동시)

```powershell
# PowerShell에서 여러 창 열기
Start-Process powershell -ArgumentList "-NoExit", "-Command", "heroku logs --tail --app zb-todo-api"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "heroku logs --tail --app zb-blog-api"
```

### 메트릭 확인

```powershell
heroku ps --app <app-name>
heroku pg:info --app <app-name>
```

---

## 문제 해결 체크리스트

### 빌드 실패

1. 로그 확인: `heroku logs --tail --app <app-name>`
2. 로컬 빌드: `cd <service-folder> && mvn clean package`
3. Java 버전 확인: `heroku config:get JAVA_VERSION --app <app-name>`

### 앱 시작 실패

1. 로그 확인
2. Procfile 확인
3. 재시작: `heroku restart --app <app-name>`

### DB 연결 실패

1. PostgreSQL 확인: `heroku addons --app <app-name>`
2. DATABASE_URL 확인: `heroku config:get DATABASE_URL --app <app-name>`
3. 필요시 추가: `heroku addons:create heroku-postgresql:essential-0 --app <app-name>`

---

**더 많은 정보**: [HEROKU_MULTI_SERVICE_GUIDE.md](./HEROKU_MULTI_SERVICE_GUIDE.md)
