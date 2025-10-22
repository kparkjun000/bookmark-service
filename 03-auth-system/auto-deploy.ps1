# JWT Auth System - 자동 배포 (로그인 후 실행)

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Heroku 배포 시작" -ForegroundColor Cyan  
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# 로그인 확인
Write-Host "로그인 확인 중..." -ForegroundColor Yellow
$whoami = heroku whoami 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 로그인되지 않았습니다." -ForegroundColor Red
    Write-Host "먼저 'heroku login' 명령으로 로그인해주세요." -ForegroundColor Yellow
    exit 1
}
Write-Host "✓ 로그인 완료: $whoami" -ForegroundColor Green
Write-Host ""

# 앱 생성
$appName = "jwt-auth-" + (-join ((97..122) | Get-Random -Count 6 | ForEach-Object {[char]$_}))
Write-Host "앱 생성 중: $appName" -ForegroundColor Yellow
heroku create $appName
if ($LASTEXITCODE -ne 0) {
    Write-Host "앱 이름이 중복됩니다. 자동 생성 중..." -ForegroundColor Yellow
    $output = heroku create 2>&1 | Out-String
    if ($output -match "https://([^.]+).herokuapp.com") {
        $appName = $matches[1]
        Write-Host "✓ 앱 생성: $appName" -ForegroundColor Green
    }
}
Write-Host ""

# PostgreSQL 추가
Write-Host "PostgreSQL 추가 중..." -ForegroundColor Yellow
heroku addons:create heroku-postgresql:essential-0 -a $appName 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    heroku addons:create heroku-postgresql:mini -a $appName 2>&1 | Out-Null
}
Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
Write-Host ""

# JWT Secret 설정
Write-Host "환경 변수 설정 중..." -ForegroundColor Yellow
$secret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})
heroku config:set JWT_SECRET=$secret -a $appName | Out-Null
Write-Host "✓ 환경 변수 설정 완료" -ForegroundColor Green
Write-Host ""

# 배포
Write-Host "Heroku에 배포 중... (5-10분 소요)" -ForegroundColor Yellow
Write-Host ""
git push heroku master 2>&1
if ($LASTEXITCODE -ne 0) {
    git push heroku main 2>&1
}
Write-Host ""

if ($LASTEXITCODE -eq 0) {
    Write-Host "==================================" -ForegroundColor Green
    Write-Host "     배포 완료! 🎉" -ForegroundColor Green
    Write-Host "==================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "📱 앱 URL: https://$appName.herokuapp.com" -ForegroundColor Cyan
    Write-Host "📖 Swagger: https://$appName.herokuapp.com/swagger-ui.html" -ForegroundColor Cyan
    Write-Host ""
    
    $open = Read-Host "Swagger UI를 여시겠습니까? (y/n)"
    if ($open -eq "y") {
        Start-Process "https://$appName.herokuapp.com/swagger-ui.html"
    }
} else {
    Write-Host "✗ 배포 실패" -ForegroundColor Red
    Write-Host "로그 확인: heroku logs --tail -a $appName" -ForegroundColor Yellow
}

