# 간단한 Heroku 배포 스크립트

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Heroku 배포 스크립트" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "배포 방법을 선택하세요:" -ForegroundColor Yellow
Write-Host "1. 브라우저 로그인 (heroku login)" -ForegroundColor White
Write-Host "2. API Key 사용 (./deploy-with-apikey.ps1)" -ForegroundColor White
Write-Host "3. GitHub 연동 (웹 브라우저)" -ForegroundColor White
Write-Host ""

$choice = Read-Host "선택 (1-3)"

switch ($choice) {
    "1" {
        Write-Host ""
        Write-Host "[1/3] Heroku 로그인 중..." -ForegroundColor Green
        Write-Host "브라우저가 열리면 로그인하세요." -ForegroundColor Yellow
        heroku login
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host ""
            Write-Host "[2/3] Git remote 확인 중..." -ForegroundColor Green
            git remote -v | Select-String "heroku"
            
            Write-Host ""
            Write-Host "[3/3] 배포 중..." -ForegroundColor Green
            git push heroku master
            
            if ($LASTEXITCODE -eq 0) {
                Write-Host ""
                Write-Host "배포 완료!" -ForegroundColor Green
                Write-Host "Swagger UI: https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html" -ForegroundColor Cyan
                Start-Process "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
            }
        }
    }
    
    "2" {
        Write-Host ""
        Write-Host "API Key 스크립트를 실행합니다..." -ForegroundColor Green
        & .\deploy-with-apikey.ps1
    }
    
    "3" {
        Write-Host ""
        Write-Host "GitHub 연동 배포:" -ForegroundColor Green
        Write-Host ""
        Write-Host "1단계: GitHub 저장소 생성" -ForegroundColor Yellow
        Write-Host "   https://github.com/new" -ForegroundColor White
        Write-Host ""
        Write-Host "2단계: Heroku Dashboard에서 연동" -ForegroundColor Yellow
        Write-Host "   https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github" -ForegroundColor White
        Write-Host ""
        
        $openDashboard = Read-Host "Heroku Dashboard를 여시겠습니까? (Y/N)"
        if ($openDashboard -eq "Y" -or $openDashboard -eq "y") {
            Start-Process "https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github"
        }
    }
    
    default {
        Write-Host "잘못된 선택입니다." -ForegroundColor Red
    }
}

Write-Host ""
pause

