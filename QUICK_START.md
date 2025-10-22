# 빠른 시작 가이드 ⚡

## 5분 안에 배포하기

### 1단계: Heroku CLI 설치 및 로그인 (1분)

```powershell
# Chocolatey로 설치
choco install heroku-cli

# 설치 확인
heroku --version

# 로그인
heroku login
```

### 2단계: Git 저장소 확인 (30초)

```powershell
cd C:\zero-base13week

# Git 초기화 (아직 안 했다면)
git init
git add .
git commit -m "Initial commit for multi-service deployment"
```

### 3단계: Heroku 앱 생성 (1분)

```powershell
# 모든 앱을 한 번에 생성
.\create-all-apps.ps1
```

### 4단계: 배포 (2-3분)

```powershell
# 전체 배포
.\deploy-all-services.ps1

# 배포 모드 선택:
# 1번: 전체 서비스 배포 (10개 모두)
# 2번: 개별 서비스 선택
# 3번: 앱만 생성 (배포 안함)
```

### 5단계: 확인 (30초)

```powershell
# 모든 서비스 상태 확인
.\services-status.ps1

# 브라우저에서 확인
# https://zb-todo-api.herokuapp.com/swagger-ui/index.html
```

---

## 자주 사용하는 명령어

### 로그 확인

```powershell
heroku logs --tail --app zb-todo-api
```

### 재시작

```powershell
heroku restart --app zb-todo-api
```

### 환경변수 확인

```powershell
heroku config --app zb-todo-api
```

### 데이터베이스 접속

```powershell
heroku pg:psql --app zb-todo-api
```

---

## 개별 서비스 배포

특정 서비스만 배포하려면:

```powershell
.\deploy-single-service.ps1

# 또는 수동으로:
git remote add heroku-todo https://git.heroku.com/zb-todo-api.git
git subtree push --prefix 01-todo-api heroku-todo main
```

---

## 서비스 URL

배포 후 각 서비스는 다음 URL에서 접속 가능합니다:

| 서비스           | URL                                       |
| ---------------- | ----------------------------------------- |
| Todo API         | https://zb-todo-api.herokuapp.com         |
| Blog API         | https://zb-blog-api.herokuapp.com         |
| Auth System      | https://zb-auth-system.herokuapp.com      |
| Bookmark Service | https://zb-bookmark-service.herokuapp.com |
| Shopping API     | https://zb-shopping-api.herokuapp.com     |
| Memo Backend     | https://zb-memo-backend.herokuapp.com     |
| Weather Service  | https://zb-weather-service.herokuapp.com  |
| URL Shortener    | https://zb-url-shortener.herokuapp.com    |
| Survey System    | https://zb-survey-system.herokuapp.com    |
| File Service     | https://zb-file-service.herokuapp.com     |

### Swagger UI

각 서비스의 API 문서는 `/swagger-ui/index.html` 경로에서 확인할 수 있습니다.

예: https://zb-todo-api.herokuapp.com/swagger-ui/index.html

---

## 문제 해결

### 빌드 실패 시

```powershell
# 로그 확인
heroku logs --tail --app zb-todo-api

# 로컬 빌드 테스트
cd 01-todo-api
mvn clean package
```

### 앱이 시작되지 않을 때

```powershell
# 재시작
heroku restart --app zb-todo-api

# dyno 상태 확인
heroku ps --app zb-todo-api
```

### 데이터베이스 문제

```powershell
# DB 정보 확인
heroku pg:info --app zb-todo-api

# DB가 없다면 추가
heroku addons:create heroku-postgresql:essential-0 --app zb-todo-api
```

---

## 다음 단계

✅ 배포 완료!

이제 다음을 시도해보세요:

1. **API 테스트**: Swagger UI에서 API를 직접 테스트
2. **로그 모니터링**: 실시간 로그를 확인하며 서비스 동작 관찰
3. **환경변수 설정**: 필요한 API 키나 설정값 추가
4. **데이터베이스 확인**: PostgreSQL에 접속해 데이터 확인

---

## 추가 리소스

- [상세 배포 가이드](./HEROKU_MULTI_SERVICE_GUIDE.md)
- [README](./README.md)
- [Heroku Documentation](https://devcenter.heroku.com/)

---

**문제가 있나요?**

`heroku logs --tail --app <app-name>`으로 로그를 먼저 확인해보세요!
