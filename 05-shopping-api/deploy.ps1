# Shopping Mall API - Heroku 자동 배포 스크립트
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Shopping Mall API - Heroku 자동 배포" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Heroku 로그인
Write-Host "[1/6] Heroku 로그인..." -ForegroundColor Yellow
heroku login

# 2. 앱 이름 입력
Write-Host ""
Write-Host "[2/6] Heroku 앱 생성..." -ForegroundColor Yellow
$appName = Read-Host "앱 이름을 입력하세요 (예: my-shopping-api)"
if ([string]::IsNullOrWhiteSpace($appName)) {
    Write-Host "이름이 입력되지 않았습니다. 자동 생성합니다..." -ForegroundColor Yellow
    heroku create
} else {
    heroku create $appName
}

# 앱 이름 가져오기
$herokuApp = heroku apps:info --json | ConvertFrom-Json
$finalAppName = $herokuApp.app.name

Write-Host "앱 이름: $finalAppName" -ForegroundColor Green

# 3. PostgreSQL 추가
Write-Host ""
Write-Host "[3/6] PostgreSQL 데이터베이스 추가..." -ForegroundColor Yellow
heroku addons:create heroku-postgresql:essential-0 --app $finalAppName

# 4. 환경 변수 확인
Write-Host ""
Write-Host "[4/6] 환경 변수 확인..." -ForegroundColor Yellow
heroku config --app $finalAppName

# 5. Git 커밋 확인
Write-Host ""
Write-Host "[5/6] Git 상태 확인..." -ForegroundColor Yellow
$gitStatus = git status --porcelain
if ($gitStatus) {
    Write-Host "변경사항을 커밋합니다..." -ForegroundColor Yellow
    git add .
    git commit -m "Deploy to Heroku"
}

# 6. 배포
Write-Host ""
Write-Host "[6/6] Heroku에 배포 중..." -ForegroundColor Yellow
Write-Host "이 과정은 5-10분 정도 걸릴 수 있습니다..." -ForegroundColor Yellow
git push heroku master

# 완료
Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "배포 완료!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "애플리케이션 URL: https://$finalAppName.herokuapp.com" -ForegroundColor Cyan
Write-Host "Swagger UI: https://$finalAppName.herokuapp.com/swagger-ui.html" -ForegroundColor Cyan
Write-Host ""
Write-Host "유용한 명령어:" -ForegroundColor Yellow
Write-Host "  heroku logs --tail --app $finalAppName  # 로그 확인" -ForegroundColor White
Write-Host "  heroku open --app $finalAppName         # 앱 열기" -ForegroundColor White
Write-Host "  heroku ps --app $finalAppName            # 상태 확인" -ForegroundColor White
Write-Host ""

# 브라우저로 Swagger 열기
$openBrowser = Read-Host "Swagger UI를 브라우저로 여시겠습니까? (y/n)"
if ($openBrowser -eq 'y' -or $openBrowser -eq 'Y') {
    Start-Process "https://$finalAppName.herokuapp.com/swagger-ui.html"
}

