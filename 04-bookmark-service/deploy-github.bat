@echo off
echo ========================================
echo GitHub 연동 배포 도우미
echo ========================================
echo.
echo 이 스크립트는 GitHub 저장소 설정을 도와줍니다.
echo Heroku 배포는 웹 브라우저에서 진행하세요!
echo.
pause
echo.

echo [1/3] Git 상태 확인...
git status
echo.

echo [2/3] GitHub 저장소 URL을 입력하세요.
echo 예: https://github.com/username/bookmark-service.git
echo.
set /p GITHUB_URL="GitHub URL: "
echo.

if "%GITHUB_URL%"=="" (
    echo URL이 입력되지 않았습니다!
    pause
    exit
)

echo [3/3] GitHub에 푸시 중...
git remote remove origin 2>nul
git remote add origin %GITHUB_URL%
git branch -M main
git push -u origin main
echo.

echo ========================================
echo GitHub 푸시 완료!
echo ========================================
echo.
echo 다음 단계:
echo 1. 브라우저에서 Heroku Dashboard 열기
echo    https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github
echo.
echo 2. "Connect to GitHub" 클릭
echo 3. 저장소 선택 및 연결
echo 4. "Deploy Branch" 클릭
echo.
echo 배포 완료 후 접속:
echo https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
echo.
pause

