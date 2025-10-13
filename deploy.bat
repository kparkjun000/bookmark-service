@echo off
echo ========================================
echo Heroku 배포 스크립트
echo ========================================
echo.

echo [1/5] Heroku 로그인 확인...
heroku auth:whoami
if %errorlevel% neq 0 (
    echo 로그인이 필요합니다!
    heroku login
)
echo.

echo [2/5] Git 상태 확인...
git status
echo.

echo [3/5] 변경사항 커밋 (있는 경우)...
git add .
git commit -m "Deploy to Heroku" 2>nul || echo "커밋할 변경사항이 없습니다."
echo.

echo [4/5] PostgreSQL 상태 확인...
heroku addons:info postgresql-clean-34059 2>nul || heroku addons:create heroku-postgresql:essential-0
echo.

echo [5/5] Heroku에 배포 중...
git push heroku master
echo.

echo ========================================
echo 배포 완료!
echo ========================================
echo.
echo Swagger UI URL:
echo https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
echo.
echo API Docs URL:
echo https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api-docs
echo.
echo 앱 로그 확인:
echo heroku logs --tail --app zerobase-bookmark-service
echo.
pause

