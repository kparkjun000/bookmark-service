# Heroku API Key 설정 및 배포 스크립트

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "Heroku API Key 인증" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "브라우저가 열렸습니다!" -ForegroundColor Yellow
Write-Host ""
Write-Host "다음 단계를 따라주세요:" -ForegroundColor Cyan
Write-Host "1. Heroku 계정으로 로그인" -ForegroundColor White
Write-Host "2. 'API Key' 섹션 찾기" -ForegroundColor White
Write-Host "3. 'Reveal' 버튼 클릭" -ForegroundColor White
Write-Host "4. API Key 복사" -ForegroundColor White
Write-Host ""

$apiKey = Read-Host "복사한 API Key를 여기에 붙여넣고 Enter를 누르세요"

if ([string]::IsNullOrWhiteSpace($apiKey)) {
    Write-Host "✗ API Key가 입력되지 않았습니다." -ForegroundColor Red
    exit 1
}

# 환경 변수에 API Key 설정
$env:HEROKU_API_KEY = $apiKey

Write-Host ""
Write-Host "인증 확인 중..." -ForegroundColor Yellow

# 인증 테스트
$whoami = heroku auth:whoami 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 인증 실패" -ForegroundColor Red
    Write-Host "API Key를 다시 확인해주세요." -ForegroundColor Yellow
    exit 1
}

Write-Host "✓ 인증 성공: $whoami" -ForegroundColor Green
Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host ""

# 배포 시작 여부 확인
$deploy = Read-Host "지금 배포를 시작하시겠습니까? (y/n)"
if ($deploy -ne "y" -and $deploy -ne "Y") {
    Write-Host ""
    Write-Host "나중에 배포하려면 다음 명령어를 실행하세요:" -ForegroundColor Yellow
    Write-Host "  `$env:HEROKU_API_KEY='$apiKey'" -ForegroundColor White
    Write-Host "  .\auto-deploy.ps1" -ForegroundColor White
    exit 0
}

Write-Host ""
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "배포 시작" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# 앱 생성
$appName = "jwt-auth-" + (-join ((97..122) | Get-Random -Count 6 | ForEach-Object {[char]$_}))
Write-Host "[1/5] 앱 생성 중: $appName" -ForegroundColor Yellow

$createOutput = heroku create $appName 2>&1 | Out-String
if ($createOutput -match "https://([^.]+).herokuapp.com") {
    $appName = $matches[1]
    Write-Host "✓ 앱 생성 완료: $appName" -ForegroundColor Green
} else {
    # 이름 중복 시 자동 생성
    Write-Host "이름 중복. 자동 생성 중..." -ForegroundColor Yellow
    $createOutput = heroku create 2>&1 | Out-String
    if ($createOutput -match "https://([^.]+).herokuapp.com") {
        $appName = $matches[1]
        Write-Host "✓ 앱 생성 완료: $appName" -ForegroundColor Green
    } else {
        Write-Host "✗ 앱 생성 실패" -ForegroundColor Red
        Write-Host $createOutput
        exit 1
    }
}
Write-Host ""

# PostgreSQL 추가
Write-Host "[2/5] PostgreSQL 추가 중..." -ForegroundColor Yellow
$pgResult = heroku addons:create heroku-postgresql:essential-0 -a $appName 2>&1
if ($LASTEXITCODE -ne 0) {
    # Essential-0 실패 시 mini 시도
    $pgResult = heroku addons:create heroku-postgresql:mini -a $appName 2>&1
    if ($LASTEXITCODE -ne 0) {
        Write-Host "⚠ PostgreSQL 추가 실패 (무료 플랜을 사용할 수 없을 수 있음)" -ForegroundColor Yellow
        Write-Host "수동으로 추가: heroku addons:create heroku-postgresql:essential-0 -a $appName" -ForegroundColor White
    } else {
        Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
    }
} else {
    Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
}
Write-Host ""

# JWT Secret 설정
Write-Host "[3/5] 환경 변수 설정 중..." -ForegroundColor Yellow
$jwtSecret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})
heroku config:set JWT_SECRET=$jwtSecret -a $appName | Out-Null

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ JWT_SECRET 설정 완료" -ForegroundColor Green
} else {
    Write-Host "✗ 환경 변수 설정 실패" -ForegroundColor Red
}
Write-Host ""

# Git remote 확인
Write-Host "[4/5] Git remote 설정 확인..." -ForegroundColor Yellow
$remotes = git remote -v 2>&1
if ($remotes -notmatch "heroku") {
    Write-Host "Heroku remote 추가 중..." -ForegroundColor Yellow
    heroku git:remote -a $appName
}
Write-Host "✓ Git remote 설정 완료" -ForegroundColor Green
Write-Host ""

# 배포
Write-Host "[5/5] Heroku에 배포 중..." -ForegroundColor Yellow
Write-Host "이 과정은 5-10분 정도 소요될 수 있습니다..." -ForegroundColor Cyan
Write-Host ""

git push heroku master 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "master 브랜치 푸시 실패. main 브랜치 시도..." -ForegroundColor Yellow
    git push heroku main 2>&1
    
    if ($LASTEXITCODE -ne 0) {
        # HEAD를 master로 푸시 시도
        git push heroku HEAD:master 2>&1
    }
}

Write-Host ""

if ($LASTEXITCODE -eq 0) {
    Write-Host "==================================" -ForegroundColor Green
    Write-Host "     배포 완료! 🎉" -ForegroundColor Green
    Write-Host "==================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "📱 앱 URL:" -ForegroundColor Cyan
    Write-Host "   https://$appName.herokuapp.com" -ForegroundColor White
    Write-Host ""
    Write-Host "📖 Swagger UI:" -ForegroundColor Cyan
    Write-Host "   https://$appName.herokuapp.com/swagger-ui.html" -ForegroundColor White
    Write-Host ""
    Write-Host "🔍 API 문서:" -ForegroundColor Cyan
    Write-Host "   https://$appName.herokuapp.com/api-docs" -ForegroundColor White
    Write-Host ""
    Write-Host "==================================" -ForegroundColor Cyan
    Write-Host ""
    
    Write-Host "유용한 명령어:" -ForegroundColor Yellow
    Write-Host "  heroku logs --tail -a $appName        # 실시간 로그" -ForegroundColor White
    Write-Host "  heroku ps -a $appName                 # 앱 상태" -ForegroundColor White
    Write-Host "  heroku open -a $appName               # 브라우저에서 열기" -ForegroundColor White
    Write-Host ""
    
    $open = Read-Host "Swagger UI를 브라우저에서 여시겠습니까? (y/n)"
    if ($open -eq "y" -or $open -eq "Y") {
        Write-Host "Swagger UI를 여는 중..." -ForegroundColor Yellow
        Start-Sleep -Seconds 5  # 앱 시작 대기
        Start-Process "https://$appName.herokuapp.com/swagger-ui.html"
        Write-Host "✓ 브라우저에서 열림" -ForegroundColor Green
    }
    
    Write-Host ""
    Write-Host "배포 완료! API를 테스트해보세요! 🚀" -ForegroundColor Green
} else {
    Write-Host "==================================" -ForegroundColor Red
    Write-Host "     배포 실패" -ForegroundColor Red
    Write-Host "==================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "로그 확인:" -ForegroundColor Yellow
    Write-Host "  heroku logs --tail -a $appName" -ForegroundColor White
    Write-Host ""
    Write-Host "수동 재배포:" -ForegroundColor Yellow
    Write-Host "  git push heroku master -f" -ForegroundColor White
}

Write-Host ""

