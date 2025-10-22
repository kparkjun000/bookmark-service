# GitHub 저장소 연결 스크립트

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "GitHub 저장소 연결 스크립트" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# GitHub 사용자 이름 입력
Write-Host "GitHub 사용자 이름을 입력하세요:" -ForegroundColor Yellow
$username = Read-Host
Write-Host ""

# 저장소 이름 (기본값: survey-system-api)
$repoName = "survey-system-api"
Write-Host "저장소 이름 (기본값: $repoName):" -ForegroundColor Yellow
$inputRepoName = Read-Host
if ($inputRepoName) {
    $repoName = $inputRepoName
}
Write-Host ""

# GitHub 저장소 URL
$repoUrl = "https://github.com/$username/$repoName.git"

Write-Host "연결할 저장소: $repoUrl" -ForegroundColor Cyan
Write-Host ""

# 기존 origin 제거 (있다면)
Write-Host "1. 기존 remote 확인 중..." -ForegroundColor Yellow
$existingRemote = git remote get-url origin 2>$null
if ($existingRemote) {
    Write-Host "   기존 origin 발견: $existingRemote" -ForegroundColor Yellow
    Write-Host "   origin을 제거합니다..." -ForegroundColor Yellow
    git remote remove origin
}
Write-Host "   ✓ 완료" -ForegroundColor Green
Write-Host ""

# GitHub remote 추가
Write-Host "2. GitHub remote 추가 중..." -ForegroundColor Yellow
git remote add origin $repoUrl
if ($LASTEXITCODE -eq 0) {
    Write-Host "   ✓ 완료" -ForegroundColor Green
} else {
    Write-Host "   ✗ 실패" -ForegroundColor Red
    exit 1
}
Write-Host ""

# 브랜치를 main으로 변경
Write-Host "3. 브랜치를 main으로 변경 중..." -ForegroundColor Yellow
git branch -M main
Write-Host "   ✓ 완료" -ForegroundColor Green
Write-Host ""

# GitHub에 푸시
Write-Host "4. GitHub에 푸시 중..." -ForegroundColor Yellow
Write-Host "   (GitHub 인증이 필요하면 Personal Access Token을 입력하세요)" -ForegroundColor Cyan
Write-Host ""
git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "✓ GitHub 푸시 완료!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "저장소 URL: https://github.com/$username/$repoName" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "다음 단계:" -ForegroundColor Yellow
    Write-Host "1. Heroku Dashboard 접속: https://dashboard.heroku.com" -ForegroundColor White
    Write-Host "2. survey-system-api 앱 선택" -ForegroundColor White
    Write-Host "3. Deploy 탭 > GitHub 연결" -ForegroundColor White
    Write-Host "4. '$repoName' 저장소 검색 및 연결" -ForegroundColor White
    Write-Host "5. 'Deploy Branch' 버튼 클릭" -ForegroundColor White
    Write-Host ""
    
    # Heroku Dashboard 자동 열기
    Write-Host "Heroku Dashboard를 여시겠습니까? (y/n)" -ForegroundColor Yellow
    $response = Read-Host
    if ($response -match '^[yY]') {
        Start-Process "https://dashboard.heroku.com/apps/survey-system-api/deploy/github"
    }
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "✗ GitHub 푸시 실패" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "문제 해결:" -ForegroundColor Yellow
    Write-Host "1. GitHub에서 저장소를 만들었는지 확인" -ForegroundColor White
    Write-Host "2. Personal Access Token이 필요할 수 있습니다:" -ForegroundColor White
    Write-Host "   https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host "3. 인증 시 사용자명과 Token을 입력하세요" -ForegroundColor White
    exit 1
}

