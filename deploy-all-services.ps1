# Zero Base 13주차 - 전체 서비스 Heroku 배포 스크립트
# 각 서비스를 독립적으로 Heroku에 배포합니다

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Zero Base 13주차 멀티서비스 배포" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Heroku CLI 설치 확인
if (!(Get-Command heroku -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Heroku CLI가 설치되어 있지 않습니다." -ForegroundColor Red
    Write-Host "다음 링크에서 Heroku CLI를 설치하세요: https://devcenter.heroku.com/articles/heroku-cli" -ForegroundColor Yellow
    exit 1
}

# Heroku 로그인 확인
Write-Host "🔐 Heroku 로그인 확인 중..." -ForegroundColor Yellow
heroku auth:whoami
if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Heroku에 로그인되어 있지 않습니다. 로그인을 진행하세요." -ForegroundColor Red
    heroku login
    if ($LASTEXITCODE -ne 0) {
        Write-Host "❌ Heroku 로그인 실패" -ForegroundColor Red
        exit 1
    }
}

Write-Host "✅ Heroku 로그인 성공" -ForegroundColor Green
Write-Host ""

# 서비스 정의 (폴더명, 앱 이름, 설명)
$services = @(
    @{Path="01-todo-api"; AppName="zb-todo-api"; Description="Todo 관리 API"; JavaVersion="17"},
    @{Path="02-blog-api"; AppName="zb-blog-api"; Description="블로그 API"; JavaVersion="17"},
    @{Path="03-auth-system"; AppName="zb-auth-system"; Description="JWT 인증 시스템"; JavaVersion="21"},
    @{Path="04-bookmark-service"; AppName="zb-bookmark-service"; Description="북마크 서비스"; JavaVersion="21"},
    @{Path="05-shopping-api"; AppName="zb-shopping-api"; Description="쇼핑몰 API"; JavaVersion="17"},
    @{Path="06-memo-backend"; AppName="zb-memo-backend"; Description="메모 백엔드"; JavaVersion="21"},
    @{Path="07-weather-service"; AppName="zb-weather-service"; Description="날씨 서비스"; JavaVersion="17"},
    @{Path="08-url-shortener"; AppName="zb-url-shortener"; Description="URL 단축 서비스"; JavaVersion="17"},
    @{Path="09-survey-system/survey-api"; AppName="zb-survey-system"; Description="설문조사 API"; JavaVersion="21"},
    @{Path="10-file-service"; AppName="zb-file-service"; Description="파일 업로드 서비스"; JavaVersion="21"}
)

# 배포 모드 선택
Write-Host "배포 모드를 선택하세요:" -ForegroundColor Cyan
Write-Host "1. 전체 서비스 배포 (10개 모두)" -ForegroundColor White
Write-Host "2. 개별 서비스 선택 배포" -ForegroundColor White
Write-Host "3. 앱만 생성 (배포 안함)" -ForegroundColor White
$mode = Read-Host "선택 (1-3)"

$deployList = @()

if ($mode -eq "1") {
    $deployList = $services
} elseif ($mode -eq "2") {
    Write-Host ""
    Write-Host "배포할 서비스를 선택하세요:" -ForegroundColor Cyan
    for ($i=0; $i -lt $services.Count; $i++) {
        Write-Host "$($i+1). $($services[$i].Description) [$($services[$i].AppName)]" -ForegroundColor White
    }
    $selection = Read-Host "번호를 쉼표로 구분해서 입력 (예: 1,3,5)"
    $indices = $selection -split "," | ForEach-Object { [int]$_.Trim() - 1 }
    $deployList = $indices | ForEach-Object { $services[$_] }
} elseif ($mode -eq "3") {
    Write-Host ""
    Write-Host "앱만 생성합니다 (배포 안함)" -ForegroundColor Yellow
} else {
    Write-Host "❌ 잘못된 선택입니다." -ForegroundColor Red
    exit 1
}

# Git 저장소 확인
if (!(Test-Path ".git")) {
    Write-Host ""
    Write-Host "⚠️  Git 저장소가 초기화되지 않았습니다. 초기화할까요? (y/n)" -ForegroundColor Yellow
    $initGit = Read-Host
    if ($initGit -eq "y") {
        git init
        git add .
        git commit -m "Initial commit for multi-service deployment"
        Write-Host "✅ Git 저장소 초기화 완료" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "배포 시작" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

$results = @()

foreach ($service in $services) {
    Write-Host ""
    Write-Host "[$($service.Description)]" -ForegroundColor Magenta
    Write-Host "앱 이름: $($service.AppName)" -ForegroundColor White
    
    # Heroku 앱 생성 (이미 존재하면 스킵)
    Write-Host "📦 Heroku 앱 확인/생성 중..." -ForegroundColor Yellow
    heroku apps:info --app $service.AppName 2>$null
    
    if ($LASTEXITCODE -ne 0) {
        Write-Host "앱이 존재하지 않습니다. 새로 생성합니다..." -ForegroundColor Yellow
        heroku create $service.AppName --region us
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ 앱 생성 완료: $($service.AppName)" -ForegroundColor Green
            
            # PostgreSQL 데이터베이스 추가
            Write-Host "💾 PostgreSQL 데이터베이스 추가 중..." -ForegroundColor Yellow
            heroku addons:create heroku-postgresql:essential-0 --app $service.AppName
            Start-Sleep -Seconds 5
            
            # Java 버전 설정
            Write-Host "☕ Java 버전 설정: $($service.JavaVersion)" -ForegroundColor Yellow
            heroku config:set JAVA_VERSION=$($service.JavaVersion) --app $service.AppName
            
        } else {
            Write-Host "❌ 앱 생성 실패: $($service.AppName)" -ForegroundColor Red
            $results += @{Service=$service.Description; Status="앱 생성 실패"; URL="N/A"}
            continue
        }
    } else {
        Write-Host "✅ 앱이 이미 존재합니다: $($service.AppName)" -ForegroundColor Green
    }
    
    # 특별 환경변수 설정
    if ($service.AppName -eq "zb-auth-system" -or $service.AppName -eq "zb-survey-system") {
        Write-Host "🔑 JWT Secret 설정 중..." -ForegroundColor Yellow
        $jwtSecret = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | ForEach-Object {[char]$_})
        heroku config:set JWT_SECRET=$jwtSecret --app $service.AppName
    }
    
    if ($service.AppName -eq "zb-weather-service") {
        Write-Host "🌤️  Weather API Key 설정 필요" -ForegroundColor Yellow
        Write-Host "나중에 다음 명령어로 설정하세요:" -ForegroundColor Cyan
        Write-Host "heroku config:set WEATHER_API_KEY=your-key --app $($service.AppName)" -ForegroundColor White
    }
    
    # 배포 모드가 3이면 배포 스킵
    if ($mode -eq "3") {
        Write-Host "⏭️  배포 스킵 (앱만 생성)" -ForegroundColor Yellow
        $results += @{Service=$service.Description; Status="앱만 생성"; URL="https://$($service.AppName).herokuapp.com"}
        continue
    }
    
    # 배포 리스트에 없으면 스킵
    if ($mode -eq "2" -and $deployList -notcontains $service) {
        Write-Host "⏭️  배포 스킵" -ForegroundColor Yellow
        continue
    }
    
    # Git subtree를 사용한 배포
    Write-Host "🚀 배포 중..." -ForegroundColor Yellow
    
    # Heroku remote 추가/업데이트
    $remoteName = "heroku-$($service.AppName)"
    git remote remove $remoteName 2>$null
    git remote add $remoteName "https://git.heroku.com/$($service.AppName).git"
    
    # Subtree push로 해당 폴더만 배포
    $pushCommand = "git subtree push --prefix $($service.Path) $remoteName main"
    Write-Host "실행: $pushCommand" -ForegroundColor Gray
    
    Invoke-Expression $pushCommand
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ 배포 성공: $($service.AppName)" -ForegroundColor Green
        $url = "https://$($service.AppName).herokuapp.com"
        Write-Host "🌐 URL: $url" -ForegroundColor Cyan
        $results += @{Service=$service.Description; Status="배포 성공"; URL=$url}
    } else {
        Write-Host "❌ 배포 실패: $($service.AppName)" -ForegroundColor Red
        $results += @{Service=$service.Description; Status="배포 실패"; URL="N/A"}
    }
    
    Write-Host "---" -ForegroundColor Gray
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "배포 결과 요약" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

foreach ($result in $results) {
    $statusColor = if ($result.Status -like "*성공*") { "Green" } elseif ($result.Status -like "*실패*") { "Red" } else { "Yellow" }
    Write-Host "[$($result.Service)]" -ForegroundColor White
    Write-Host "  상태: $($result.Status)" -ForegroundColor $statusColor
    Write-Host "  URL: $($result.URL)" -ForegroundColor Cyan
    Write-Host ""
}

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "완료!" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "💡 각 서비스 관리 명령어:" -ForegroundColor Yellow
Write-Host "  - 로그 확인: heroku logs --tail --app <app-name>" -ForegroundColor White
Write-Host "  - 재시작: heroku restart --app <app-name>" -ForegroundColor White
Write-Host "  - 환경변수 확인: heroku config --app <app-name>" -ForegroundColor White
Write-Host "  - 데이터베이스 정보: heroku pg:info --app <app-name>" -ForegroundColor White
Write-Host ""

