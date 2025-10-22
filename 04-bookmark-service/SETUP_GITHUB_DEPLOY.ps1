# GitHub 연동 배포 스크립트 (Git 인증 불필요!)

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "GitHub 연동 배포 (Heroku Git 인증 우회)" -ForegroundColor Green
Write-Host "========================================`n" -ForegroundColor Cyan

Write-Host "이 방법은 Heroku Git 인증 문제를 완전히 우회합니다!" -ForegroundColor Yellow
Write-Host ""

# 1단계: GitHub 저장소 URL 입력
Write-Host "[1/4] GitHub 저장소 설정" -ForegroundColor Green
Write-Host ""
Write-Host "먼저 GitHub에서 새 저장소를 만드세요:" -ForegroundColor Yellow
Write-Host "  https://github.com/new" -ForegroundColor White
Write-Host ""
Write-Host "저장소 이름 예: bookmark-service" -ForegroundColor White
Write-Host "Public 또는 Private 선택 (둘 다 가능)" -ForegroundColor White
Write-Host ""

$githubUrl = Read-Host "GitHub 저장소 URL을 입력하세요 (예: https://github.com/username/bookmark-service.git)"

if ([string]::IsNullOrWhiteSpace($githubUrl)) {
    Write-Host "`n저장소 URL이 입력되지 않았습니다!" -ForegroundColor Red
    Write-Host "GitHub 저장소를 먼저 만들어주세요: https://github.com/new" -ForegroundColor Yellow
    pause
    exit 1
}

# 2단계: GitHub에 푸시
Write-Host "`n[2/4] GitHub에 코드 푸시 중..." -ForegroundColor Green

git remote remove origin 2>$null
git remote add origin $githubUrl
git branch -M main

Write-Host "GitHub에 푸시 중..." -ForegroundColor Yellow
git push -u origin main --force

if ($LASTEXITCODE -ne 0) {
    Write-Host "`nGitHub 푸시 실패!" -ForegroundColor Red
    Write-Host "GitHub 인증이 필요할 수 있습니다." -ForegroundColor Yellow
    Write-Host "Personal Access Token을 생성하세요: https://github.com/settings/tokens" -ForegroundColor White
    pause
    exit 1
}

Write-Host "`nGitHub 푸시 완료! ✅" -ForegroundColor Green

# 3단계: Heroku Dashboard 열기
Write-Host "`n[3/4] Heroku Dashboard에서 GitHub 연동" -ForegroundColor Green
Write-Host ""
Write-Host "이제 브라우저에서 다음 작업을 수행하세요:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Heroku Dashboard 열기 (자동으로 열립니다)" -ForegroundColor White
Write-Host "2. 'Connect to GitHub' 버튼 클릭" -ForegroundColor White
Write-Host "3. GitHub 계정 연동 및 권한 승인" -ForegroundColor White
Write-Host "4. 저장소 검색하여 선택" -ForegroundColor White
Write-Host "5. 'Connect' 버튼 클릭" -ForegroundColor White
Write-Host "6. 'Deploy Branch' 버튼 클릭 (main 브랜치)" -ForegroundColor White
Write-Host ""

$openBrowser = Read-Host "Heroku Dashboard를 여시겠습니까? (Y/N)"
if ($openBrowser -eq "Y" -or $openBrowser -eq "y" -or $openBrowser -eq "") {
    Start-Process "https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github"
    Write-Host "`n브라우저가 열렸습니다!" -ForegroundColor Green
} else {
    Write-Host "`n수동으로 접속하세요:" -ForegroundColor Yellow
    Write-Host "https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github" -ForegroundColor White
}

# 4단계: 완료
Write-Host "`n[4/4] 배포 완료 후 확인" -ForegroundColor Green
Write-Host ""
Write-Host "배포가 완료되면 다음 URL로 접속하세요:" -ForegroundColor Yellow
Write-Host ""
Write-Host "✨ Swagger UI:" -ForegroundColor Cyan
Write-Host "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html" -ForegroundColor White
Write-Host ""
Write-Host "📊 Heroku Dashboard:" -ForegroundColor Cyan
Write-Host "https://dashboard.heroku.com/apps/zerobase-bookmark-service" -ForegroundColor White
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "이 방법은 Heroku Git 인증이 필요 없습니다!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

pause

