# Heroku 배포 자동화 스크립트
# PowerShell 스크립트로 실행: .\deploy-to-heroku.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  File Upload Service - Heroku 배포" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# 1. Git 초기화 및 커밋
Write-Host "[1/8] Git 저장소 초기화 중..." -ForegroundColor Yellow
if (-not (Test-Path .git)) {
    git init
    Write-Host "✓ Git 저장소 초기화 완료" -ForegroundColor Green
} else {
    Write-Host "✓ Git 저장소가 이미 존재합니다" -ForegroundColor Green
}

Write-Host "[2/8] 파일 추가 및 커밋 중..." -ForegroundColor Yellow
git add .
git commit -m "Deploy to Heroku - File Upload Service"
Write-Host "✓ 커밋 완료" -ForegroundColor Green

# 2. Heroku 로그인 확인
Write-Host "[3/8] Heroku 로그인 확인 중..." -ForegroundColor Yellow
Write-Host "브라우저가 열리면 Heroku에 로그인해주세요." -ForegroundColor Cyan
heroku login

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ Heroku 로그인 실패" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Heroku 로그인 완료" -ForegroundColor Green

# 3. Heroku 앱 생성
Write-Host "[4/8] Heroku 앱 생성 중..." -ForegroundColor Yellow
$appName = Read-Host "앱 이름을 입력하세요 (엔터: 자동 생성)"

if ([string]::IsNullOrWhiteSpace($appName)) {
    heroku create
} else {
    heroku create $appName
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 앱 생성 실패 (이름이 이미 사용 중일 수 있습니다)" -ForegroundColor Red
    exit 1
}
Write-Host "✓ Heroku 앱 생성 완료" -ForegroundColor Green

# 4. PostgreSQL 추가
Write-Host "[5/8] PostgreSQL 데이터베이스 추가 중..." -ForegroundColor Yellow
Write-Host "경고: PostgreSQL Essential 플랜은 월 $5 비용이 발생합니다" -ForegroundColor Yellow
$confirm = Read-Host "계속하시겠습니까? (y/n)"

if ($confirm -eq 'y' -or $confirm -eq 'Y') {
    heroku addons:create heroku-postgresql:essential-0
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "✗ PostgreSQL 추가 실패" -ForegroundColor Red
        Write-Host "무료 플랜을 원하시면 다른 PostgreSQL 제공업체를 사용하세요" -ForegroundColor Yellow
        exit 1
    }
    Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
} else {
    Write-Host "PostgreSQL 추가를 건너뜁니다" -ForegroundColor Yellow
    Write-Host "나중에 수동으로 추가하세요: heroku addons:create heroku-postgresql:essential-0" -ForegroundColor Cyan
}

# 5. 환경 변수 설정
Write-Host "[6/8] 환경 변수 설정 중..." -ForegroundColor Yellow
heroku config:set SPRING_PROFILES_ACTIVE=prod
heroku config:set FILE_UPLOAD_DIR=/tmp/uploads
Write-Host "✓ 환경 변수 설정 완료" -ForegroundColor Green

# 6. 배포
Write-Host "[7/8] Heroku에 배포 중..." -ForegroundColor Yellow
Write-Host "이 단계는 수분이 소요될 수 있습니다..." -ForegroundColor Cyan
git push heroku main

if ($LASTEXITCODE -ne 0) {
    Write-Host "✗ 배포 실패" -ForegroundColor Red
    Write-Host "로그를 확인하세요: heroku logs --tail" -ForegroundColor Yellow
    exit 1
}
Write-Host "✓ 배포 완료!" -ForegroundColor Green

# 7. 배포 확인
Write-Host "[8/8] 배포 확인 중..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "  🎉 배포 완료!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# 앱 URL 가져오기
$appUrl = heroku apps:info --json | ConvertFrom-Json | Select-Object -ExpandProperty app | Select-Object -ExpandProperty web_url

Write-Host "앱 URL: $appUrl" -ForegroundColor Cyan
Write-Host "Health Check: ${appUrl}api/health" -ForegroundColor Cyan
Write-Host ""

Write-Host "다음 명령어로 앱을 열 수 있습니다:" -ForegroundColor Yellow
Write-Host "  heroku open" -ForegroundColor White
Write-Host ""

Write-Host "로그를 보려면:" -ForegroundColor Yellow
Write-Host "  heroku logs --tail" -ForegroundColor White
Write-Host ""

# 앱 열기
$openApp = Read-Host "브라우저에서 앱을 여시겠습니까? (y/n)"
if ($openApp -eq 'y' -or $openApp -eq 'Y') {
    heroku open
}

Write-Host ""
Write-Host "배포 완료! 🚀" -ForegroundColor Green
Write-Host ""
Write-Host "추가 정보는 DEPLOY_NOW.md를 참고하세요" -ForegroundColor Cyan

