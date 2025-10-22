@echo off
echo ========================================
echo Heroku 직접 배포
echo ========================================
echo.
echo 새 터미널 창에서 Heroku 로그인 후 배포
echo.

echo [1/2] Heroku 로그인을 시작합니다...
echo 브라우저가 열리면 로그인하세요.
echo.
call heroku login
echo.

if %errorlevel% neq 0 (
    echo 로그인 실패!
    echo.
    echo 대안:
    echo 1. GitHub 연동: deploy-github.bat 실행
    echo 2. 웹 브라우저: https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github
    pause
    exit
)

echo 로그인 성공!
echo.

echo [2/2] Heroku에 배포 중...
git push heroku master
echo.

if %errorlevel% neq 0 (
    echo 배포 실패!
    echo.
    echo PostgreSQL이 준비될 때까지 1분 대기 후 재시도...
    timeout /t 60 /nobreak
    git push heroku master
)

echo.
echo ========================================
echo 배포 완료!
echo ========================================
echo.
echo Swagger UI:
echo https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
echo.
echo 로그 확인:
echo heroku logs --tail --app zerobase-bookmark-service
echo.
pause

