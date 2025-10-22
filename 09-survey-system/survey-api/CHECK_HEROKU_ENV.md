# Heroku 환경 변수 확인

## 문제 진단

Swagger UI에서 "Internal Server Error /api-docs" 오류가 발생하는 경우, 주로 데이터베이스 연결 문제입니다.

## 1단계: Heroku Dashboard에서 환경 변수 확인

1. https://dashboard.heroku.com/apps/survey-system-api/settings 접속
2. **"Config Vars"** 섹션에서 **"Reveal Config Vars"** 클릭
3. 다음 변수들이 있는지 확인:
   - ✅ `DATABASE_URL` - PostgreSQL 연결 URL (자동 생성)
   - ✅ `JWT_SECRET` - JWT 시크릿 키

## 2단계: DATABASE_URL 형식 확인

DATABASE_URL은 다음과 같은 형식이어야 합니다:

```
postgres://username:password@host:port/database
```

예시:

```
postgres://abcdefgh:xyz123@ec2-xxx.compute.amazonaws.com:5432/d1234
```

## 3단계: PostgreSQL 애드온 상태 확인

```powershell
# PowerShell에서 실행 (Heroku 로그인 후)
heroku addons -a survey-system-api
```

또는 Dashboard에서:

1. https://dashboard.heroku.com/apps/survey-system-api/resources
2. **Add-ons** 섹션에서 `postgresql` 확인
3. 상태가 "creating" 또는 "available"인지 확인

## 4단계: DATABASE_URL이 없는 경우

PostgreSQL 애드온을 다시 추가:

```powershell
heroku addons:create heroku-postgresql:essential-0 -a survey-system-api
```

## 5단계: 로그 확인

Heroku Dashboard에서:

1. https://dashboard.heroku.com/apps/survey-system-api/logs
2. 또는 PowerShell에서: `heroku logs --tail -a survey-system-api`

찾아야 할 로그 메시지:

- ✅ "Detected Heroku DATABASE_URL, parsing..."
- ✅ "Parsed JDBC URL: jdbc:postgresql://..."
- ✅ "HikariCP DataSource configured successfully"
- ❌ "Failed to parse DATABASE_URL"
- ❌ "Connection refused"
- ❌ "Authentication failed"

## 6단계: 문제 해결

### DATABASE_URL이 없음

```powershell
heroku addons:create heroku-postgresql:essential-0 -a survey-system-api
heroku restart -a survey-system-api
```

### DATABASE_URL이 있지만 연결 실패

```powershell
# PostgreSQL 상태 확인
heroku pg:info -a survey-system-api

# 앱 재시작
heroku restart -a survey-system-api
```

### 로그에 "Failed to parse DATABASE_URL" 오류

DATABASE_URL 형식이 잘못되었을 수 있습니다. Dashboard에서 확인하세요.

## 현재 진행 상황

1. ✅ DatabaseConfig.java 개선 (로깅 추가)
2. ⏳ GitHub에 푸시 필요
3. ⏳ Heroku에 배포 필요
4. ⏳ 환경 변수 확인 필요
5. ⏳ 로그 확인 필요

## 다음 단계

1. 변경사항 커밋 및 푸시
2. Heroku Dashboard에서 환경 변수 확인
3. PostgreSQL 애드온 상태 확인
4. 재배포
5. 로그 확인
