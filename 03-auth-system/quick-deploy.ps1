# JWT Auth System - 빠른 배포 (API Key 방식)

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Heroku 빠른 배포" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "방법 1: 인터랙티브 로그인" -ForegroundColor Yellow
Write-Host "방법 2: API Key 직접 입력" -ForegroundColor Yellow
Write-Host ""

$method = Read-Host "선택하세요 (1 또는 2)"

if ($method -eq "1") {
    Write-Host ""
    Write-Host "이메일과 비밀번호로 로그인합니다..." -ForegroundColor Yellow
    Write-Host ""
    
    # 인터랙티브 로그인 시도
    heroku login -i
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host ""
        Write-Host "✗ 로그인 실패" -ForegroundColor Red
        Write-Host "방법 2를 사용하세요." -ForegroundColor Yellow
        exit 1
    }
    
} elseif ($method -eq "2") {
    Write-Host ""
    Write-Host "Heroku API Key를 입력하세요:" -ForegroundColor Yellow
    Write-Host "(https://dashboard.heroku.com/account 에서 확인)" -ForegroundColor Cyan
    Write-Host ""
    
    $apiKey = Read-Host "API Key"
    
    if ([string]::IsNullOrWhiteSpace($apiKey)) {
        Write-Host "✗ API Key가 입력되지 않았습니다." -ForegroundColor Red
        exit 1
    }
    
    $env:HEROKU_API_KEY = $apiKey
    
    # ~/.netrc 파일에 저장
    $netrcPath = Join-Path $env:USERPROFILE ".netrc"
    $netrcContent = @"
machine api.heroku.com
  login $apiKey
  password $apiKey
machine git.heroku.com
  login $apiKey
  password $apiKey
"@
    
    $netrcContent | Out-File -FilePath $netrcPath -Encoding ASCII -Force
    
    Write-Host "✓ API Key 설정 완료" -ForegroundColor Green
    
} else {
    Write-Host "✗ 잘못된 선택입니다." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "인증 확인 중..." -ForegroundColor Yellow
$whoami = heroku auth:whoami 2>&1

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 인증 실패" -ForegroundColor Red
    exit 1
}

Write-Host "✓ 인증 성공: $whoami" -ForegroundColor Green
Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host ""

# 배포 시작
Write-Host "배포를 시작합니다..." -ForegroundColor Cyan
Write-Host ""

# 앱 이름 생성
$appName = "jwt-auth-" + (-join ((97..122) | Get-Random -Count 6 | ForEach-Object {[char]$_}))

Write-Host "[1/5] Heroku 앱 생성 중..." -ForegroundColor Yellow
$output = heroku create $appName 2>&1 | Out-String

if ($output -match "https://([^.]+).herokuapp.com") {
    $appName = $matches[1]
    Write-Host "✓ 앱 생성: $appName" -ForegroundColor Green
} else {
    $output = heroku create 2>&1 | Out-String
    if ($output -match "https://([^.]+).herokuapp.com") {
        $appName = $matches[1]
        Write-Host "✓ 앱 생성: $appName" -ForegroundColor Green
    } else {
        Write-Host "✗ 앱 생성 실패" -ForegroundColor Red
        exit 1
    }
}
Write-Host ""

Write-Host "[2/5] PostgreSQL 추가 중..." -ForegroundColor Yellow
heroku addons:create heroku-postgresql:essential-0 -a $appName 2>&1 | Out-Null
if ($LASTEXITCODE -ne 0) {
    heroku addons:create heroku-postgresql:mini -a $appName 2>&1 | Out-Null
}
Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
Write-Host ""

Write-Host "[3/5] 환경 변수 설정 중..." -ForegroundColor Yellow
$secret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})
heroku config:set JWT_SECRET=$secret -a $appName | Out-Null
Write-Host "✓ JWT_SECRET 설정 완료" -ForegroundColor Green
Write-Host ""

Write-Host "[4/5] Git remote 설정 중..." -ForegroundColor Yellow
heroku git:remote -a $appName 2>&1 | Out-Null
Write-Host "✓ Git remote 설정 완료" -ForegroundColor Green
Write-Host ""

Write-Host "[5/5] 배포 중... (5-10분 소요)" -ForegroundColor Yellow
Write-Host ""

git push heroku master 2>&1
if ($LASTEXITCODE -ne 0) {
    git push heroku main 2>&1
    if ($LASTEXITCODE -ne 0) {
        git push heroku HEAD:master 2>&1
    }
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
    
    Start-Sleep -Seconds 3
    Start-Process "https://$appName.herokuapp.com/swagger-ui.html"
    
    Write-Host "✓ 배포 완료! 브라우저가 열렸습니다." -ForegroundColor Green
} else {
    Write-Host "✗ 배포 실패" -ForegroundColor Red
    Write-Host "로그: heroku logs --tail -a $appName" -ForegroundColor Yellow
}

