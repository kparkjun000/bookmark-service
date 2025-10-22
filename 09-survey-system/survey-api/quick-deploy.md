# 🚀 빠른 배포 가이드

## 현재 문제

Heroku login의 인증 토큰이 만료되었습니다.

## 해결: 새 터미널에서 배포

**Ctrl+C를 눌러 현재 프로세스를 종료하고** 새 PowerShell을 열어서:

```powershell
# 프로젝트 디렉토리로 이동
cd C:\zero-base13week\09-survey-system\survey-api

# Heroku 로그인 (아무 키나 누르면 브라우저가 열립니다)
heroku login

# 배포
git push heroku master
```

## 또는: Heroku Dashboard 사용 (더 쉬움)

1. https://dashboard.heroku.com 접속
2. `survey-system-api` 선택
3. Settings > "Reveal Config Vars" 에서 설정 확인
4. Deploy > Manual Deploy 사용

## 배포 완료 확인

https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

## 아직 배포가 안 됐다면?

모든 준비는 완료되었습니다! 마지막 단계만 실행하면 됩니다:

**새 PowerShell 터미널**을 열고:

```powershell
cd C:\zero-base13week\09-survey-system\survey-api
heroku login
git push heroku master
```
