# Zero Base 13주차 - 단일 서비스 Heroku 배포 스크립트
# 특정 서비스 하나만 선택해서 배포합니다

param(
    [string]$ServiceNumber,
    [string]$AppName
)

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "단일 서비스 배포 스크립트" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# 서비스 매핑
$serviceMap = @{
    "1" = @{Path="01-todo-api"; AppName="zb-todo-api"; Description="Todo 관리 API"; JavaVersion="17"}
    "2" = @{Path="02-blog-api"; AppName="zb-blog-api"; Description="블로그 API"; JavaVersion="17"}
    "3" = @{Path="03-auth-system"; AppName="zb-auth-system"; Description="JWT 인증 시스템"; JavaVersion="21"}
    "4" = @{Path="04-bookmark-service"; AppName="zb-bookmark-service"; Description="북마크 서비스"; JavaVersion="21"}
    "5" = @{Path="05-shopping-api"; AppName="zb-shopping-api"; Description="쇼핑몰 API"; JavaVersion="17"}
    "6" = @{Path="06-memo-backend"; AppName="zb-memo-backend"; Description="메모 백엔드"; JavaVersion="21"}
    "7" = @{Path="07-weather-service"; AppName="zb-weather-service"; Description="날씨 서비스"; JavaVersion="17"}
    "8" = @{Path="08-url-shortener"; AppName="zb-url-shortener"; Description="URL 단축 서비스"; JavaVersion="17"}
    "9" = @{Path="09-survey-system/survey-api"; AppName="zb-survey-system"; Description="설문조사 API"; JavaVersion="21"}
    "10" = @{Path="10-file-service"; AppName="zb-file-service"; Description="파일 업로드 서비스"; JavaVersion="21"}
}

# 서비스 선택
if (-not $ServiceNumber) {
    Write-Host "배포할 서비스를 선택하세요:" -ForegroundColor Cyan
    Write-Host ""
    Write-Host " 1. Todo 관리 API" -ForegroundColor White
    Write-Host " 2. 블로그 API" -ForegroundColor White
    Write-Host " 3. JWT 인증 시스템" -ForegroundColor White
    Write-Host " 4. 북마크 서비스" -ForegroundColor White
    Write-Host " 5. 쇼핑몰 API" -ForegroundColor White
    Write-Host " 6. 메모 백엔드" -ForegroundColor White
    Write-Host " 7. 날씨 서비스" -ForegroundColor White
    Write-Host " 8. URL 단축 서비스" -ForegroundColor White
    Write-Host " 9. 설문조사 API" -ForegroundColor White
    Write-Host "10. 파일 업로드 서비스" -ForegroundColor White
    Write-Host ""
    $ServiceNumber = Read-Host "번호 입력 (1-10)"
}

if (-not $serviceMap.ContainsKey($ServiceNumber)) {
    Write-Host "❌ 잘못된 서비스 번호입니다." -ForegroundColor Red
    exit 1
}

$service = $serviceMap[$ServiceNumber]

# 앱 이름 커스터마이즈 옵션
if (-not $AppName) {
    Write-Host ""
    Write-Host "기본 앱 이름: $($service.AppName)" -ForegroundColor Yellow
    Write-Host "다른 이름을 사용하시겠습니까? (Enter = 기본 이름 사용)" -ForegroundColor Yellow
    $customName = Read-Host "커스텀 앱 이름"
    if ($customName) {
        $service.AppName = $customName
    }
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "배포 정보" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "서비스: $($service.Description)" -ForegroundColor White
Write-Host "폴더: $($service.Path)" -ForegroundColor White
Write-Host "앱 이름: $($service.AppName)" -ForegroundColor White
Write-Host "Java 버전: $($service.JavaVersion)" -ForegroundColor White
Write-Host ""

# Heroku CLI 확인
if (!(Get-Command heroku -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Heroku CLI가 설치되어 있지 않습니다." -ForegroundColor Red
    Write-Host "다음 링크에서 설치하세요: https://devcenter.heroku.com/articles/heroku-cli" -ForegroundColor Yellow
    exit 1
}

# Heroku 로그인 확인
Write-Host "🔐 Heroku 로그인 확인 중..." -ForegroundColor Yellow
heroku auth:whoami 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Heroku에 로그인하세요..." -ForegroundColor Yellow
    heroku login
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ 로그인 실패" -ForegroundColor Red
        exit 1
    }
}
Write-Host "✅ 로그인 완료" -ForegroundColor Green

# Git 확인
if (!(Test-Path ".git")) {
    Write-Host ""
    Write-Host "⚠️  Git 저장소가 없습니다. 초기화할까요? (y/n)" -ForegroundColor Yellow
    $initGit = Read-Host
    if ($initGit -eq "y") {
        git init
        git add .
        git commit -m "Initial commit"
        Write-Host "✅ Git 저장소 초기화 완료" -ForegroundColor Green
    } else {
        Write-Host "❌ Git 저장소가 필요합니다." -ForegroundColor Red
        exit 1
    }
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "배포 시작" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Heroku 앱 생성/확인
Write-Host "📦 Heroku 앱 확인 중..." -ForegroundColor Yellow
heroku apps:info --app $service.AppName 2>$null

if ($LASTEXITCODE -ne 0) {
    Write-Host "앱이 없습니다. 새로 생성합니다..." -ForegroundColor Yellow
    heroku create $service.AppName --region us
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ 앱 생성 완료" -ForegroundColor Green
        
        # PostgreSQL 추가
        Write-Host "💾 PostgreSQL 추가 중..." -ForegroundColor Yellow
        heroku addons:create heroku-postgresql:essential-0 --app $service.AppName
        Start-Sleep -Seconds 5
        Write-Host "✅ 데이터베이스 추가 완료" -ForegroundColor Green
        
        # Java 버전 설정
        Write-Host "☕ Java $($service.JavaVersion) 설정 중..." -ForegroundColor Yellow
        heroku config:set JAVA_VERSION=$($service.JavaVersion) --app $service.AppName
        Write-Host "✅ Java 버전 설정 완료" -ForegroundColor Green
        
        # 특별 환경변수
        if ($service.AppName -like "*auth*" -or $service.AppName -like "*survey*") {
            Write-Host "🔑 JWT Secret 생성 중..." -ForegroundColor Yellow
            $jwtSecret = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | ForEach-Object {[char]$_})
            heroku config:set JWT_SECRET=$jwtSecret --app $service.AppName
            Write-Host "✅ JWT Secret 설정 완료" -ForegroundColor Green
        }
        
        if ($service.AppName -like "*weather*") {
            Write-Host "⚠️  Weather API Key가 필요합니다!" -ForegroundColor Yellow
            Write-Host "나중에 설정: heroku config:set WEATHER_API_KEY=your-key --app $($service.AppName)" -ForegroundColor Cyan
        }
    } else {
        Write-Host "❌ 앱 생성 실패" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "✅ 앱이 이미 존재합니다" -ForegroundColor Green
}

Write-Host ""
Write-Host "🚀 배포 중..." -ForegroundColor Yellow
Write-Host "이 작업은 몇 분 정도 걸릴 수 있습니다..." -ForegroundColor Gray
Write-Host ""

# Git remote 설정
$remoteName = "heroku-$($service.AppName)"
git remote remove $remoteName 2>$null
git remote add $remoteName "https://git.heroku.com/$($service.AppName).git"

# Subtree push
$pushCommand = "git subtree push --prefix $($service.Path) $remoteName main"
Write-Host "실행: $pushCommand" -ForegroundColor Gray
Invoke-Expression $pushCommand

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host "✅ 배포 성공!" -ForegroundColor Green
    Write-Host "=====================================" -ForegroundColor Green
    Write-Host ""
    $url = "https://$($service.AppName).herokuapp.com"
    Write-Host "🌐 서비스 URL: $url" -ForegroundColor Cyan
    Write-Host "📊 대시보드: https://dashboard.heroku.com/apps/$($service.AppName)" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "유용한 명령어:" -ForegroundColor Yellow
    Write-Host "  - 로그 보기: heroku logs --tail --app $($service.AppName)" -ForegroundColor White
    Write-Host "  - 재시작: heroku restart --app $($service.AppName)" -ForegroundColor White
    Write-Host "  - 환경변수 확인: heroku config --app $($service.AppName)" -ForegroundColor White
    Write-Host "  - DB 접속: heroku pg:psql --app $($service.AppName)" -ForegroundColor White
    Write-Host ""
} else {
    Write-Host ""
    Write-Host "=====================================" -ForegroundColor Red
    Write-Host "❌ 배포 실패" -ForegroundColor Red
    Write-Host "=====================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "로그를 확인하세요:" -ForegroundColor Yellow
    Write-Host "heroku logs --tail --app $($service.AppName)" -ForegroundColor White
    Write-Host ""
    exit 1
}

