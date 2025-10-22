# Heroku 배포 스크립트 (PowerShell)
# JWT 인증 시스템을 Heroku에 배포합니다.

Write-Host "==================================" -ForegroundColor Cyan
Write-Host "JWT Auth System - Heroku 배포" -ForegroundColor Cyan
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# 1. Heroku 로그인 확인
Write-Host "Step 1: Heroku 로그인 확인..." -ForegroundColor Yellow
$whoami = heroku whoami 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Heroku에 로그인이 필요합니다." -ForegroundColor Red
    Write-Host "브라우저가 열리면 로그인해주세요..." -ForegroundColor Yellow
    heroku login
    if ($LASTEXITCODE -ne 0) {
        Write-Host "로그인 실패. 스크립트를 종료합니다." -ForegroundColor Red
        exit 1
    }
}
Write-Host "✓ 로그인 완료: $whoami" -ForegroundColor Green
Write-Host ""

# 2. 앱 이름 입력
Write-Host "Step 2: Heroku 앱 생성" -ForegroundColor Yellow
$appName = Read-Host "앱 이름을 입력하세요 (예: my-jwt-auth-system, 비워두면 자동 생성)"

if ([string]::IsNullOrWhiteSpace($appName)) {
    Write-Host "자동으로 앱 이름을 생성합니다..." -ForegroundColor Yellow
    heroku create
} else {
    Write-Host "앱 '$appName'을 생성합니다..." -ForegroundColor Yellow
    heroku create $appName
}

if ($LASTEXITCODE -ne 0) {
    Write-Host "앱 생성 실패. 이미 존재하는 이름일 수 있습니다." -ForegroundColor Red
    $useExisting = Read-Host "기존 앱을 사용하시겠습니까? (y/n)"
    if ($useExisting -eq "y") {
        $existingApp = Read-Host "기존 앱 이름을 입력하세요"
        heroku git:remote -a $existingApp
    } else {
        exit 1
    }
}
Write-Host "✓ Heroku 앱 생성 완료" -ForegroundColor Green
Write-Host ""

# 3. PostgreSQL 추가
Write-Host "Step 3: PostgreSQL 데이터베이스 추가" -ForegroundColor Yellow
Write-Host "Mini 플랜 (무료)을 추가합니다..." -ForegroundColor Yellow
heroku addons:create heroku-postgresql:mini

if ($LASTEXITCODE -ne 0) {
    Write-Host "⚠ PostgreSQL 추가 실패 (이미 존재할 수 있음)" -ForegroundColor Yellow
} else {
    Write-Host "✓ PostgreSQL 추가 완료" -ForegroundColor Green
}
Write-Host ""

# 4. 환경 변수 설정
Write-Host "Step 4: 환경 변수 설정" -ForegroundColor Yellow

# JWT Secret 생성 (256비트 랜덤 문자열)
$jwtSecret = -join ((65..90) + (97..122) + (48..57) | Get-Random -Count 64 | ForEach-Object {[char]$_})
Write-Host "JWT Secret을 생성합니다..." -ForegroundColor Yellow
heroku config:set JWT_SECRET=$jwtSecret

Write-Host "✓ 환경 변수 설정 완료" -ForegroundColor Green
Write-Host ""

# 5. 배포
Write-Host "Step 5: Heroku에 배포" -ForegroundColor Yellow
Write-Host "코드를 Heroku에 푸시합니다... (몇 분 소요될 수 있습니다)" -ForegroundColor Yellow
git push heroku master

if ($LASTEXITCODE -ne 0) {
    Write-Host "배포 실패. 오류를 확인하세요." -ForegroundColor Red
    exit 1
}
Write-Host "✓ 배포 완료!" -ForegroundColor Green
Write-Host ""

# 6. 결과 확인
Write-Host "==================================" -ForegroundColor Cyan
Write-Host "배포가 성공적으로 완료되었습니다!" -ForegroundColor Green
Write-Host "==================================" -ForegroundColor Cyan
Write-Host ""

# 앱 정보 가져오기
$appInfo = heroku apps:info --json | ConvertFrom-Json
$appUrl = $appInfo.app.web_url

Write-Host "📱 앱 URL: $appUrl" -ForegroundColor Cyan
Write-Host "📖 Swagger UI: ${appUrl}swagger-ui.html" -ForegroundColor Cyan
Write-Host ""

Write-Host "유용한 명령어:" -ForegroundColor Yellow
Write-Host "  heroku logs --tail           # 로그 실시간 확인" -ForegroundColor White
Write-Host "  heroku open                  # 브라우저에서 앱 열기" -ForegroundColor White
Write-Host "  heroku open /swagger-ui.html # Swagger UI 열기" -ForegroundColor White
Write-Host "  heroku ps                    # 앱 상태 확인" -ForegroundColor White
Write-Host "  heroku config                # 환경 변수 확인" -ForegroundColor White
Write-Host ""

# Swagger UI 열기
$openSwagger = Read-Host "Swagger UI를 브라우저에서 여시겠습니까? (y/n)"
if ($openSwagger -eq "y") {
    heroku open /swagger-ui.html
}

Write-Host ""
Write-Host "배포 완료! 🎉" -ForegroundColor Green

