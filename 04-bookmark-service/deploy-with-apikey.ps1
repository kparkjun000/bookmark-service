# Heroku API Key를 사용한 배포 스크립트

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Heroku API Key 배포 스크립트" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "API Key 가져오기:" -ForegroundColor Yellow
Write-Host "1. 브라우저에서 접속: https://dashboard.heroku.com/account" -ForegroundColor White
Write-Host "2. 'API Key' 섹션 찾기" -ForegroundColor White
Write-Host "3. 'Reveal' 버튼 클릭" -ForegroundColor White
Write-Host "4. API Key 복사" -ForegroundColor White
Write-Host ""

# API Key 입력 받기
$apiKey = Read-Host "Heroku API Key를 붙여넣으세요"

if ([string]::IsNullOrWhiteSpace($apiKey)) {
    Write-Host "API Key가 입력되지 않았습니다!" -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "[1/5] API Key 설정 중..." -ForegroundColor Green
$env:HEROKU_API_KEY = $apiKey

Write-Host "[2/5] 로그인 확인 중..." -ForegroundColor Green
$result = heroku auth:whoami 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "로그인 성공: $result" -ForegroundColor Green
} else {
    Write-Host "로그인 실패!" -ForegroundColor Red
    Write-Host $result -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "[3/5] Git 저장소 설정 중..." -ForegroundColor Green
git remote remove heroku 2>$null
$herokuUrl = "https://heroku:$apiKey@git.heroku.com/zerobase-bookmark-service.git"
git remote add heroku $herokuUrl
Write-Host "Heroku remote 추가 완료" -ForegroundColor Green

Write-Host ""
Write-Host "[4/5] PostgreSQL 상태 확인 중..." -ForegroundColor Green
heroku addons:info postgresql-clean-34059 --app zerobase-bookmark-service
if ($LASTEXITCODE -ne 0) {
    Write-Host "PostgreSQL이 아직 준비 중일 수 있습니다. 잠시 대기합니다..." -ForegroundColor Yellow
    Start-Sleep -Seconds 30
}

Write-Host ""
Write-Host "[5/5] Heroku에 배포 중..." -ForegroundColor Green
Write-Host "이 과정은 3-5분 정도 소요됩니다..." -ForegroundColor Yellow
Write-Host ""

git push heroku master --force

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "배포 완료!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Swagger UI 접속:" -ForegroundColor Cyan
    Write-Host "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html" -ForegroundColor White
    Write-Host ""
    Write-Host "OpenAPI Docs:" -ForegroundColor Cyan
    Write-Host "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api-docs" -ForegroundColor White
    Write-Host ""
    Write-Host "로그 확인:" -ForegroundColor Cyan
    Write-Host "heroku logs --tail --app zerobase-bookmark-service" -ForegroundColor White
    Write-Host ""
    
    # 브라우저에서 Swagger UI 열기
    $openBrowser = Read-Host "브라우저에서 Swagger UI를 여시겠습니까? (Y/N)"
    if ($openBrowser -eq "Y" -or $openBrowser -eq "y") {
        Start-Process "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
    }
} else {
    Write-Host ""
    Write-Host "배포 실패!" -ForegroundColor Red
    Write-Host "로그를 확인하세요: heroku logs --tail --app zerobase-bookmark-service" -ForegroundColor Yellow
}

Write-Host ""
pause

