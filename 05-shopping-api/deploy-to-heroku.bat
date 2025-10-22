@echo off
echo ========================================
echo Shopping Mall API - Heroku 배포 스크립트
echo ========================================
echo.

REM Heroku 로그인 확인
echo [1/6] Heroku 로그인 확인 중...
heroku auth:whoami
if %errorlevel% neq 0 (
    echo.
    echo Heroku 로그인이 필요합니다.
    echo 브라우저가 열리면 로그인해주세요.
    echo.
    heroku login
)

echo.
echo [2/6] Heroku 앱 생성 중...
set /p APP_NAME="Heroku 앱 이름을 입력하세요 (예: my-shopping-api): "
heroku create %APP_NAME%

echo.
echo [3/6] PostgreSQL 애드온 추가 중...
heroku addons:create heroku-postgresql:essential-0 --app %APP_NAME%

echo.
echo [4/6] 환경 변수 확인 중...
heroku config --app %APP_NAME%

echo.
echo [5/6] Git 원격 저장소 추가...
git remote add heroku https://git.heroku.com/%APP_NAME%.git

echo.
echo [6/6] Heroku에 배포 중...
echo 이 과정은 몇 분 정도 걸릴 수 있습니다...
git push heroku master

echo.
echo ========================================
echo 배포 완료!
echo ========================================
echo.
echo 애플리케이션 URL: https://%APP_NAME%.herokuapp.com
echo Swagger UI: https://%APP_NAME%.herokuapp.com/swagger-ui.html
echo.
echo 로그 확인: heroku logs --tail --app %APP_NAME%
echo 앱 열기: heroku open --app %APP_NAME%
echo.

pause

