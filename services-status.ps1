# Zero Base 13주차 - 모든 서비스 상태 확인 스크립트

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "전체 서비스 상태 확인" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""

$services = @(
    @{AppName="zb-todo-api"; Description="Todo 관리 API"},
    @{AppName="zb-blog-api"; Description="블로그 API"},
    @{AppName="zb-auth-system"; Description="JWT 인증 시스템"},
    @{AppName="zb-bookmark-service"; Description="북마크 서비스"},
    @{AppName="zb-shopping-api"; Description="쇼핑몰 API"},
    @{AppName="zb-memo-backend"; Description="메모 백엔드"},
    @{AppName="zb-weather-service"; Description="날씨 서비스"},
    @{AppName="zb-url-shortener"; Description="URL 단축 서비스"},
    @{AppName="zb-survey-system"; Description="설문조사 API"},
    @{AppName="zb-file-service"; Description="파일 업로드 서비스"}
)

foreach ($service in $services) {
    Write-Host "[$($service.Description)]" -ForegroundColor Magenta
    Write-Host "앱 이름: $($service.AppName)" -ForegroundColor White
    
    # 앱 존재 확인
    heroku apps:info --app $service.AppName 2>$null
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ 앱 존재" -ForegroundColor Green
        
        # URL
        $url = "https://$($service.AppName).herokuapp.com"
        Write-Host "🌐 URL: $url" -ForegroundColor Cyan
        
        # Dyno 상태
        Write-Host "📊 Dyno 상태:" -ForegroundColor Yellow
        heroku ps --app $service.AppName
        
        # 데이터베이스
        Write-Host "💾 데이터베이스:" -ForegroundColor Yellow
        heroku pg:info --app $service.AppName 2>$null
        
        # 환경변수 개수
        $configCount = (heroku config --app $service.AppName 2>$null | Measure-Object -Line).Lines - 1
        Write-Host "🔧 환경변수: $configCount 개" -ForegroundColor White
        
    } else {
        Write-Host "❌ 앱이 존재하지 않습니다" -ForegroundColor Red
    }
    
    Write-Host "---" -ForegroundColor Gray
    Write-Host ""
}

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "완료" -ForegroundColor Green
Write-Host "=====================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "💡 개별 서비스 관리:" -ForegroundColor Yellow
Write-Host "  - 로그: heroku logs --tail --app <app-name>" -ForegroundColor White
Write-Host "  - 재시작: heroku restart --app <app-name>" -ForegroundColor White
Write-Host "  - 환경변수: heroku config --app <app-name>" -ForegroundColor White
Write-Host ""

