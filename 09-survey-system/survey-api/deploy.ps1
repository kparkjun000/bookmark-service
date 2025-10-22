# Heroku 배포 스크립트 (PowerShell)

Write-Host "===================================" -ForegroundColor Cyan
Write-Host "Survey API - Heroku 배포 스크립트" -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

# 앱 이름
$APP_NAME = "survey-system-api"

# 1. Heroku 로그인 상태 확인
Write-Host "1. Heroku 로그인 상태 확인..." -ForegroundColor Yellow
try {
    $null = heroku auth:whoami 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "Heroku에 로그인되어 있지 않습니다." -ForegroundColor Red
        Write-Host "heroku login 명령을 실행하세요." -ForegroundColor Red
        exit 1
    }
    Write-Host "✓ 로그인됨" -ForegroundColor Green
} catch {
    Write-Host "Heroku CLI가 설치되어 있지 않습니다." -ForegroundColor Red
    exit 1
}
Write-Host ""

# 2. Git 변경사항 확인
Write-Host "2. Git 변경사항 확인..." -ForegroundColor Yellow
$gitStatus = git status -s
if ($gitStatus) {
    Write-Host "변경사항이 있습니다. 커밋하시겠습니까? (y/n)" -ForegroundColor Yellow
    $response = Read-Host
    if ($response -match '^[yY]') {
        git add .
        git commit -m "Update for deployment"
        Write-Host "✓ 커밋 완료" -ForegroundColor Green
    }
}
Write-Host ""

# 3. Heroku 앱 확인
Write-Host "3. Heroku 앱 확인..." -ForegroundColor Yellow
try {
    $null = heroku apps:info -a $APP_NAME 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "앱이 존재하지 않습니다." -ForegroundColor Red
        exit 1
    }
    Write-Host "✓ 앱 확인됨: $APP_NAME" -ForegroundColor Green
} catch {
    Write-Host "앱 확인 실패" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 4. Heroku에 배포
Write-Host "4. Heroku에 배포 중..." -ForegroundColor Yellow
git push heroku master

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "===================================" -ForegroundColor Green
    Write-Host "✓ 배포 완료!" -ForegroundColor Green
    Write-Host "===================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "앱 URL: https://$APP_NAME.herokuapp.com" -ForegroundColor Cyan
    Write-Host "Swagger UI: https://$APP_NAME.herokuapp.com/swagger-ui.html" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "로그 확인: heroku logs --tail -a $APP_NAME" -ForegroundColor Yellow
    Write-Host "앱 열기: heroku open -a $APP_NAME" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "===================================" -ForegroundColor Red
    Write-Host "✗ 배포 실패" -ForegroundColor Red
    Write-Host "===================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "오류 로그를 확인하세요: heroku logs --tail -a $APP_NAME" -ForegroundColor Yellow
    exit 1
}

