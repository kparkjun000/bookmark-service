@echo off
REM Heroku 배포 스크립트 (Windows)
REM Weather Service API를 Heroku에 배포합니다

echo ==================================
echo Weather Service - Heroku 배포 스크립트
echo ==================================
echo.

REM Heroku CLI 설치 확인
echo [1/8] Heroku CLI 설치 확인...
where heroku >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Heroku CLI가 설치되어 있지 않습니다.
    echo 다음 링크에서 설치하세요:
    echo https://devcenter.heroku.com/articles/heroku-cli
    pause
    exit /b 1
)
echo [OK] Heroku CLI 설치됨
echo.

REM Heroku 로그인 확인
echo [2/8] Heroku 로그인 확인...
heroku auth:whoami >nul 2>&1
if %errorlevel% neq 0 (
    echo [X] Heroku에 로그인되어 있지 않습니다.
    echo 로그인을 시작합니다...
    heroku login
    if %errorlevel% neq 0 (
        echo [X] 로그인 실패
        pause
        exit /b 1
    )
)
for /f "tokens=*" %%i in ('heroku auth:whoami') do set HEROKU_USER=%%i
echo [OK] 로그인됨: %HEROKU_USER%
echo.

REM 앱 이름 입력
echo [3/8] Heroku 앱 설정...
set /p APP_NAME="Heroku 앱 이름을 입력하세요 (예: my-weather-service): "

if "%APP_NAME%"=="" (
    echo [X] 앱 이름이 필요합니다.
    pause
    exit /b 1
)

REM 앱 존재 여부 확인
heroku apps:info --app %APP_NAME% >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] 기존 앱 사용: %APP_NAME%
) else (
    echo 새 앱을 생성합니다: %APP_NAME%
    heroku create %APP_NAME%
    if %errorlevel% neq 0 (
        echo [X] 앱 생성 실패
        pause
        exit /b 1
    )
    echo [OK] 앱 생성 완료
)
echo.

REM PostgreSQL 애드온 확인/추가
echo [4/8] PostgreSQL 데이터베이스 설정...
heroku addons --app %APP_NAME% | findstr "heroku-postgresql" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] PostgreSQL이 이미 설치되어 있습니다
) else (
    echo PostgreSQL 애드온을 추가합니다...
    heroku addons:create heroku-postgresql:essential-0 --app %APP_NAME%
    if %errorlevel% neq 0 (
        echo [X] PostgreSQL 추가 실패
        pause
        exit /b 1
    )
    echo [OK] PostgreSQL 추가 완료
    echo 데이터베이스 초기화를 기다립니다 (약 30초)...
    timeout /t 30 /nobreak >nul
)
echo.

REM 환경 변수 설정
echo [5/8] 환경 변수 설정...
set /p API_KEY="OpenWeatherMap API Key를 입력하세요: "

if "%API_KEY%"=="" (
    echo [X] API Key가 필요합니다.
    echo OpenWeatherMap API Key는 https://openweathermap.org/api 에서 발급받을 수 있습니다.
    pause
    exit /b 1
)

echo 환경 변수를 설정합니다...
heroku config:set OPENWEATHER_API_KEY=%API_KEY% --app %APP_NAME%
heroku config:set SPRING_PROFILE=prod --app %APP_NAME%
heroku config:set JAVA_OPTS="-Xmx300m -Xss512k -XX:CICompilerCount=2" --app %APP_NAME%

echo [OK] 환경 변수 설정 완료
echo.

REM Git 저장소 확인
echo [6/8] Git 저장소 확인...
if not exist .git (
    echo Git 저장소를 초기화합니다...
    git init
    git add .
    git commit -m "Initial commit for Heroku deployment"
    echo [OK] Git 저장소 생성 완료
) else (
    echo [OK] Git 저장소 존재
    
    REM 변경사항 커밋
    git diff-index --quiet HEAD -- >nul 2>&1
    if %errorlevel% neq 0 (
        echo 변경사항을 커밋합니다...
        git add .
        git commit -m "Deploy to Heroku"
    )
)

REM Heroku remote 추가
git remote | findstr "heroku" >nul 2>&1
if %errorlevel% neq 0 (
    echo Heroku remote를 추가합니다...
    heroku git:remote --app %APP_NAME%
    echo [OK] Heroku remote 추가됨
)
echo.

REM 배포
echo [7/8] Heroku에 배포 중...
echo 이 과정은 몇 분이 걸릴 수 있습니다...
echo.

git push heroku main 2>nul
if %errorlevel% neq 0 (
    git push heroku master 2>nul
    if %errorlevel% neq 0 (
        echo [X] 배포 실패
        echo 로그를 확인하세요: heroku logs --tail --app %APP_NAME%
        pause
        exit /b 1
    )
)
echo [OK] 배포 완료!
echo.

REM 배포 확인
echo [8/8] 배포 확인...
timeout /t 5 /nobreak >nul

set APP_URL=https://%APP_NAME%.herokuapp.com

echo.
echo ==================================
echo 배포 성공!
echo ==================================
echo.
echo 앱 정보:
echo   - 앱 이름: %APP_NAME%
echo   - URL: %APP_URL%
echo   - Swagger: %APP_URL%/swagger-ui.html
echo   - Health Check: %APP_URL%/api/v1/health
echo.
echo 유용한 명령어:
echo   - 로그 확인: heroku logs --tail --app %APP_NAME%
echo   - 앱 열기: heroku open --app %APP_NAME%
echo   - 앱 재시작: heroku restart --app %APP_NAME%
echo   - 환경 변수 확인: heroku config --app %APP_NAME%
echo   - DB 접속: heroku pg:psql --app %APP_NAME%
echo.
set /p OPEN_BROWSER="브라우저에서 앱을 여시겠습니까? (y/n): "

if /i "%OPEN_BROWSER%"=="y" (
    heroku open --app %APP_NAME%
)

echo.
echo 배포가 완료되었습니다! 🎉
pause

