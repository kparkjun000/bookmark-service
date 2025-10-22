# GitHub에 코드 푸시하는 간단한 스크립트

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "GitHub 코드 푸시" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# GitHub 사용자 이름 입력
Write-Host "GitHub 사용자 이름을 입력하세요:" -ForegroundColor Yellow
Write-Host "(예: aparkjun 또는 실제 GitHub username)" -ForegroundColor Gray
$username = Read-Host "사용자 이름"

if (-not $username) {
    Write-Host "사용자 이름이 필요합니다!" -ForegroundColor Red
    exit 1
}

Write-Host ""

# 저장소 이름
$repoName = "survey-system-api"
$repoUrl = "https://github.com/$username/$repoName.git"

Write-Host "저장소 URL: $repoUrl" -ForegroundColor Cyan
Write-Host ""

# 기존 origin 제거 (있다면)
$existingOrigin = git remote get-url origin 2>$null
if ($existingOrigin) {
    Write-Host "기존 origin 제거 중..." -ForegroundColor Yellow
    git remote remove origin
}

# GitHub remote 추가
Write-Host "GitHub remote 추가 중..." -ForegroundColor Yellow
git remote add origin $repoUrl
Write-Host "✓ 완료" -ForegroundColor Green
Write-Host ""

# 브랜치를 main으로 변경
Write-Host "브랜치를 main으로 변경 중..." -ForegroundColor Yellow
git branch -M main
Write-Host "✓ 완료" -ForegroundColor Green
Write-Host ""

# 최신 변경사항 커밋
Write-Host "최신 파일 커밋 중..." -ForegroundColor Yellow
git add .
git commit -m "Add GitHub deployment guides and scripts" 2>$null
Write-Host "✓ 완료" -ForegroundColor Green
Write-Host ""

# GitHub에 푸시
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "GitHub에 푸시 중..." -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "인증 정보 입력:" -ForegroundColor Yellow
Write-Host "- Username: $username" -ForegroundColor Gray
Write-Host "- Password: Personal Access Token 입력" -ForegroundColor Gray
Write-Host ""
Write-Host "Personal Access Token이 없다면:" -ForegroundColor Cyan
Write-Host "1. https://github.com/settings/tokens 접속" -ForegroundColor Gray
Write-Host "2. 'Generate new token (classic)' 클릭" -ForegroundColor Gray
Write-Host "3. 'repo' 권한 선택" -ForegroundColor Gray
Write-Host "4. 생성된 토큰 복사하여 아래 Password에 입력" -ForegroundColor Gray
Write-Host ""

git push -u origin main

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "✓ GitHub 푸시 성공!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "저장소: https://github.com/$username/$repoName" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "다음 단계:" -ForegroundColor Yellow
    Write-Host "1. Heroku Dashboard로 돌아가기" -ForegroundColor White
    Write-Host "2. GitHub 연결 페이지 새로고침 (F5)" -ForegroundColor White
    Write-Host "3. 저장소 다시 검색: '$repoName'" -ForegroundColor White
    Write-Host "4. 'Connect' 버튼 클릭" -ForegroundColor White
    Write-Host "5. 'Deploy Branch' 버튼 클릭" -ForegroundColor White
    Write-Host ""
    
    # 저장소 열기
    Write-Host "GitHub 저장소를 여시겠습니까? (y/n)" -ForegroundColor Yellow
    $response = Read-Host
    if ($response -match '^[yY]') {
        Start-Process "https://github.com/$username/$repoName"
    }
} else {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "✗ 푸시 실패" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "문제 해결:" -ForegroundColor Yellow
    Write-Host "1. Personal Access Token 생성:" -ForegroundColor White
    Write-Host "   https://github.com/settings/tokens" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "2. Token 생성 시 'repo' 권한 선택" -ForegroundColor White
    Write-Host ""
    Write-Host "3. 다시 시도:" -ForegroundColor White
    Write-Host "   .\push-to-github.ps1" -ForegroundColor Cyan
    exit 1
}

