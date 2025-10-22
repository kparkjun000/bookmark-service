# Zero Base 13주차 - 모든 Heroku 앱 생성 스크립트
# 배포 없이 앱만 생성하고 기본 설정을 완료합니다

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Heroku 앱 일괄 생성" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

# Heroku CLI 확인
if (!(Get-Command heroku -ErrorAction SilentlyContinue)) {
    Write-Host "❌ Heroku CLI가 설치되어 있지 않습니다." -ForegroundColor Red
    exit 1
}

# 로그인 확인
heroku auth:whoami 2>$null
if ($LASTEXITCODE -ne 0) {
    Write-Host "Heroku 로그인을 진행하세요..." -ForegroundColor Yellow
    heroku login
}

$services = @(
    @{AppName="zb-todo-api"; Description="Todo 관리 API"; JavaVersion="17"},
    @{AppName="zb-blog-api"; Description="블로그 API"; JavaVersion="17"},
    @{AppName="zb-auth-system"; Description="JWT 인증 시스템"; JavaVersion="21"; NeedJWT=$true},
    @{AppName="zb-bookmark-service"; Description="북마크 서비스"; JavaVersion="21"},
    @{AppName="zb-shopping-api"; Description="쇼핑몰 API"; JavaVersion="17"},
    @{AppName="zb-memo-backend"; Description="메모 백엔드"; JavaVersion="21"},
    @{AppName="zb-weather-service"; Description="날씨 서비스"; JavaVersion="17"; NeedWeatherAPI=$true},
    @{AppName="zb-url-shortener"; Description="URL 단축 서비스"; JavaVersion="17"},
    @{AppName="zb-survey-system"; Description="설문조사 API"; JavaVersion="21"; NeedJWT=$true},
    @{AppName="zb-file-service"; Description="파일 업로드 서비스"; JavaVersion="21"}
)

$results = @()

foreach ($service in $services) {
    Write-Host ""
    Write-Host "[$($service.Description)]" -ForegroundColor Magenta
    Write-Host "앱 이름: $($service.AppName)" -ForegroundColor White
    
    # 앱 존재 확인
    heroku apps:info --app $service.AppName 2>$null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ 앱이 이미 존재합니다" -ForegroundColor Green
        $results += @{Service=$service.Description; AppName=$service.AppName; Status="이미 존재"; URL="https://$($service.AppName).herokuapp.com"}
    } else {
        Write-Host "📦 앱 생성 중..." -ForegroundColor Yellow
        heroku create $service.AppName --region us
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ 앱 생성 완료" -ForegroundColor Green
            
            # PostgreSQL 추가
            Write-Host "💾 PostgreSQL 추가 중..." -ForegroundColor Yellow
            heroku addons:create heroku-postgresql:essential-0 --app $service.AppName
            Start-Sleep -Seconds 3
            
            # Java 버전 설정
            Write-Host "☕ Java $($service.JavaVersion) 설정..." -ForegroundColor Yellow
            heroku config:set JAVA_VERSION=$($service.JavaVersion) --app $service.AppName
            
            # 특별 환경변수
            if ($service.NeedJWT) {
                Write-Host "🔑 JWT Secret 생성..." -ForegroundColor Yellow
                $jwtSecret = -join ((48..57) + (65..90) + (97..122) | Get-Random -Count 64 | ForEach-Object {[char]$_})
                heroku config:set JWT_SECRET=$jwtSecret --app $service.AppName
            }
            
            if ($service.NeedWeatherAPI) {
                Write-Host "⚠️  Weather API Key가 필요합니다" -ForegroundColor Yellow
                Write-Host "   설정: heroku config:set WEATHER_API_KEY=your-key --app $($service.AppName)" -ForegroundColor Cyan
            }
            
            $results += @{Service=$service.Description; AppName=$service.AppName; Status="생성 완료"; URL="https://$($service.AppName).herokuapp.com"}
        } else {
            Write-Host "❌ 앱 생성 실패" -ForegroundColor Red
            $results += @{Service=$service.Description; AppName=$service.AppName; Status="생성 실패"; URL="N/A"}
        }
    }
    
    Write-Host "---" -ForegroundColor Gray
}

Write-Host ""
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "생성 결과" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

foreach ($result in $results) {
    $statusColor = if ($result.Status -like "*완료*") { "Green" } elseif ($result.Status -like "*실패*") { "Red" } else { "Yellow" }
    Write-Host "[$($result.Service)]" -ForegroundColor White
    Write-Host "  앱 이름: $($result.AppName)" -ForegroundColor Cyan
    Write-Host "  상태: $($result.Status)" -ForegroundColor $statusColor
    Write-Host "  URL: $($result.URL)" -ForegroundColor Cyan
    Write-Host ""
}

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "완료!" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "다음 단계:" -ForegroundColor Yellow
Write-Host "1. 배포: .\deploy-all-services.ps1" -ForegroundColor White
Write-Host "2. 개별 배포: .\deploy-single-service.ps1" -ForegroundColor White
Write-Host "3. 상태 확인: .\services-status.ps1" -ForegroundColor White
Write-Host ""

