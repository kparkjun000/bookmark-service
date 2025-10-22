# PostgreSQL 16+ 업그레이드 가이드

## 현재 문제

PostgreSQL 버전이 오래되어 호환성 문제가 발생할 수 있습니다.

## 해결: PostgreSQL 16으로 업그레이드

### 방법 1: Heroku Dashboard 사용 (추천)

1. **기존 PostgreSQL 제거**

   - https://dashboard.heroku.com/apps/survey-system-api/resources
   - Heroku Postgres 애드온 옆의 ⋯ (점 3개) 클릭
   - "Remove Add-on" 선택
   - 확인

2. **새 PostgreSQL 16 추가**
   - 같은 페이지에서 "Find more add-ons" 클릭
   - "Heroku Postgres" 검색
   - 플랜 선택:
     - **Essential 0** ($5/month, 10M rows)
     - **Mini** ($5/month, 10M rows) - PostgreSQL 16 지원
   - "Submit Order Form" 클릭

### 방법 2: PowerShell 명령어 사용

```powershell
cd C:\zero-base13week\09-survey-system\survey-api

# 현재 애드온 확인
heroku addons -a survey-system-api

# 기존 PostgreSQL 제거 (애드온 이름 확인 후)
heroku addons:destroy postgresql-animate-15072 -a survey-system-api --confirm survey-system-api

# 새 PostgreSQL 추가 (자동으로 최신 버전 사용)
heroku addons:create heroku-postgresql:essential-0 -a survey-system-api

# 또는 Mini 플랜 사용
heroku addons:create heroku-postgresql:mini -a survey-system-api

# 앱 재시작
heroku restart -a survey-system-api
```

### 주의사항

⚠️ **기존 데이터베이스를 제거하면 모든 데이터가 삭제됩니다!**

- 현재는 개발 단계이므로 데이터가 없어도 됩니다.
- 운영 환경에서는 백업 후 마이그레이션이 필요합니다.

### 데이터베이스 생성 대기

PostgreSQL 애드온 생성 후:

1. 상태가 "creating..."에서 "available"로 변경될 때까지 대기 (1-2분)
2. DATABASE_URL이 자동으로 Config Vars에 추가됨
3. 앱 자동 재시작

### 확인

```powershell
# PostgreSQL 정보 확인
heroku pg:info -a survey-system-api

# 버전 확인
heroku pg:psql -a survey-system-api -c "SELECT version();"

# DATABASE_URL 확인
heroku config:get DATABASE_URL -a survey-system-api
```
