# JWT Auth System - 원클릭 Heroku 배포
# 이 스크립트를 실행하면 자동으로 배포가 진행됩니다.

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "JWT Auth System - Heroku 자동 배포" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# Heroku 로그인 확인 및 로그인
Write-Host "[1/7] Heroku 로그인 중..." -ForegroundColor Yellow
$isLoggedIn = $false
$whoami = heroku whoami 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ 이미 로그인됨: $whoami" -ForegroundColor Green
    $isLoggedIn = $true
} else {
    Write-Host "로그인이 필요합니다. 브라우저가 열립니다..." -ForegroundColor Yellow
    Write-Host ""
    Write-Host "브라우저에서 로그인 완료 후 아무 키나 누르세요..." -ForegroundColor Cyan
    
    # 브라우저 로그인 시작
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "heroku login"
    
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
    
    # 로그인 확인
    Start-Sleep -Seconds 2
    $whoami = heroku whoami 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✓ 로그인 성공: $whoami" -ForegroundColor Green
        $isLoggedIn = $true
    } else {
        Write-Host "✗ 로그인 실패" -ForegroundColor Red
        Write-Host ""
        Write-Host "수동으로 로그인 후 다시 스크립트를 실행해주세요:" -ForegroundColor Yellow
        Write-Host "  heroku login" -ForegroundColor White
        Write-Host "  .\deploy-now.ps1" -ForegroundColor White
        exit 1
    }
}
Write-Host ""

# 앱 이름 생성
$timestamp = Get-Date -Format "yyyyMMddHHmmss"
$randomSuffix = -join ((97..122) | Get-Random -Count 4 | ForEach-Object {[char]$_})
$appName = "jwt-auth-$randomSuffix"

Write-Host "[2/7] Heroku 앱 생성 중..." -ForegroundColor Yellow
Write-Host "앱 이름: $appName" -ForegroundColor Cyan

$createOutput = heroku create $appName 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ 앱 생성 완료" -ForegroundColor Green
} else {
    # 이름이 중복되면 자동 생성
    Write-Host "이름 중복. 자동 생성 시도..." -ForegroundColor Yellow
    $createOutput = heroku create 2>&1
    if ($LASTEXITCODE -eq 0) {
        # 생성된 앱 이름 추출
        $appUrl = ($createOutput | Select-String -Pattern "https://(.+?).herokuapp.com").Matches.Groups[1].Value
        $appName = $appUrl
        Write-Host "✓ 앱 생성 완료: $appName" -ForegroundColor Green
    } else {
        Write-Host "✗ 앱 생성 실패" -ForegroundColor Red
        Write-Host $createOutput
        exit 1
    }
}
Write-Host ""

# PostgreSQL 추가
Write-Host "[3/7] PostgreSQL 데이터베이스 추가 중..." -ForegroundColor Yellow
$pgOutput = heroku addons:create heroku-postgresql:essential-0 -a $appName 2>&1

if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
} else {
    # Mini 플랜이 없으면 Essential 플랜 시도
    Write-Host "Essential 플랜 시도..." -ForegroundColor Yellow
    $pgOutput = heroku addons:create heroku-postgresql:mini -a $appName 2>&1
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "⚠ PostgreSQL 추가 실패 (무료 플랜을 사용할 수 없을 수 있음)" -ForegroundColor Yellow
        Write-Host "데이터베이스는 수동으로 추가해야 할 수 있습니다." -ForegroundColor Yellow
    } else {
        Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
    }
}
Write-Host ""

# JWT Secret 생성 및 설정
Write-Host "[4/7] 환경 변수 설정 중..." -ForegroundColor Yellow

# 강력한 JWT Secret 생성 (64자, 영문+숫자)
$jwtSecret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})

heroku config:set JWT_SECRET=$jwtSecret -a $appName | Out-Null
if ($LASTEXITCODE -eq 0) {
    Write-Host "✓ JWT_SECRET 설정 완료" -ForegroundColor Green
} else {
    Write-Host "✗ 환경 변수 설정 실패" -ForegroundColor Red
    exit 1
}
Write-Host ""

# Git 상태 확인
Write-Host "[5/7] Git 상태 확인 중..." -ForegroundColor Yellow
$gitStatus = git status 2>&1
if ($gitStatus -like "*nothing to commit*") {
    Write-Host "✓ Git 커밋 완료" -ForegroundColor Green
} else {
    Write-Host "변경사항 커밋 중..." -ForegroundColor Yellow
    git add . 2>&1 | Out-Null
    git commit -m "Deploy to Heroku" 2>&1 | Out-Null
    Write-Host "✓ 커밋 완료" -ForegroundColor Green
}
Write-Host ""

# Heroku에 배포
Write-Host "[6/7] Heroku에 배포 중..." -ForegroundColor Yellow
Write-Host "이 과정은 5-10분 정도 소요될 수 있습니다..." -ForegroundColor Cyan
Write-Host ""

git push heroku master

if ($LASTEXITCODE -ne 0) {
    # master 대신 main 브랜치 시도
    Write-Host "main 브랜치로 재시도..." -ForegroundColor Yellow
    git push heroku main
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "✗ 배포 실패" -ForegroundColor Red
        Write-Host ""
        Write-Host "오류 해결:" -ForegroundColor Yellow
        Write-Host "  1. heroku logs --tail -a $appName  # 로그 확인" -ForegroundColor White
        Write-Host "  2. git push heroku master -f        # 강제 푸시" -ForegroundColor White
        exit 1
    }
}

Write-Host ""
Write-Host "✓ 배포 완료!" -ForegroundColor Green
Write-Host ""

# 앱 정보 확인
Write-Host "[7/7] 배포 정보 확인 중..." -ForegroundColor Yellow
Start-Sleep -Seconds 3

$appUrl = "https://$appName.herokuapp.com"

Write-Host ""
Write-Host "==================================" -ForegroundColor Green
Write-Host "     배포 성공! 🎉" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Green
Write-Host ""
Write-Host "📱 앱 URL:" -ForegroundColor Cyan
Write-Host "   $appUrl" -ForegroundColor White
Write-Host ""
Write-Host "📖 Swagger UI:" -ForegroundColor Cyan
Write-Host "   $appUrl/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "🔍 API 문서:" -ForegroundColor Cyan
Write-Host "   $appUrl/api-docs" -ForegroundColor White
Write-Host ""
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "유용한 명령어:" -ForegroundColor Yellow
Write-Host "  heroku logs --tail -a $appName        # 실시간 로그" -ForegroundColor White
Write-Host "  heroku ps -a $appName                 # 앱 상태" -ForegroundColor White
Write-Host "  heroku config -a $appName             # 환경 변수" -ForegroundColor White
Write-Host "  heroku pg:info -a $appName            # 데이터베이스 정보" -ForegroundColor White
Write-Host "  heroku open -a $appName               # 브라우저에서 열기" -ForegroundColor White
Write-Host ""

# Swagger UI 열기
$openBrowser = Read-Host "Swagger UI를 브라우저에서 여시겠습니까? (y/n)"
if ($openBrowser -eq "y" -or $openBrowser -eq "Y") {
    Write-Host "Swagger UI를 여는 중..." -ForegroundColor Yellow
    Start-Process "$appUrl/swagger-ui.html"
    Write-Host "✓ 브라우저에서 열림" -ForegroundColor Green
}

Write-Host ""
Write-Host "배포 완료! API를 테스트해보세요! 🚀" -ForegroundColor Green
Write-Host ""

