# 📋 GitHub 저장소 생성 및 Heroku 연동 - 단계별 가이드

## ✅ 현재 상태

- ✅ 브라우저에서 GitHub 저장소 생성 페이지가 열렸습니다
- ✅ 모든 코드가 커밋되어 있습니다
- ✅ 연결 스크립트가 준비되어 있습니다

---

## 📝 단계별 진행

### 1단계: GitHub에서 저장소 생성 (브라우저)

현재 브라우저에 열린 GitHub 페이지에서:

1. **Repository name**: `survey-system-api` (이미 입력됨)
2. **Description**: `Survey Creation and Response Collection API` (이미 입력됨)
3. **Public** 또는 **Private** 선택
4. **중요!** ❌ "Add a README file" 체크 해제 (이미 README가 있음)
5. **"Create repository"** 버튼 클릭

### 2단계: PowerShell에서 GitHub 연결

새 PowerShell 터미널을 열거나 현재 터미널에서:

```powershell
# 프로젝트 디렉토리로 이동
cd C:\zero-base13week\09-survey-system\survey-api

# 연결 스크립트 실행
.\connect-github.ps1
```

스크립트 실행 시:

- GitHub 사용자 이름 입력
- 저장소 이름 확인 (기본값: survey-system-api)
- GitHub 인증 (Personal Access Token 필요할 수 있음)

### 3단계: Personal Access Token 생성 (필요시)

GitHub 인증이 필요하면:

1. https://github.com/settings/tokens 접속
2. "Generate new token (classic)" 클릭
3. Note: `survey-system-api`
4. Scopes: **repo** 전체 선택
5. "Generate token" 클릭
6. **토큰 복사** (나중에 볼 수 없음!)
7. Git push 시:
   - Username: GitHub 사용자명
   - Password: 복사한 Token 붙여넣기

### 4단계: Heroku Dashboard에서 GitHub 연동

스크립트가 성공하면 Heroku Dashboard가 자동으로 열립니다.
또는 수동으로: https://dashboard.heroku.com/apps/survey-system-api/deploy/github

1. **Deployment method** 섹션에서 "GitHub" 클릭
2. "Connect to GitHub" 버튼 클릭
3. 저장소 검색창에 `survey-system-api` 입력
4. 검색 결과에서 저장소 옆의 "Connect" 버튼 클릭
5. **Manual deploy** 섹션:
   - Branch: `main` 선택
   - "Deploy Branch" 버튼 클릭

### 5단계: 배포 완료 확인

배포 로그를 실시간으로 확인하고, 완료되면:

- **앱 URL**: https://survey-system-api-dd94bac93976.herokuapp.com
- **Swagger UI**: https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

---

## 🚀 빠른 실행 (모든 단계)

```powershell
# 1. 브라우저에서 GitHub 저장소 생성
# (이미 열렸음 - 저장소 생성 후 다음 단계로)

# 2. PowerShell에서 연결
cd C:\zero-base13week\09-survey-system\survey-api
.\connect-github.ps1

# 3. Heroku Dashboard에서 배포
# (스크립트가 자동으로 열어줌)
```

---

## ❓ 문제 해결

### "remote origin already exists"

```powershell
git remote remove origin
.\connect-github.ps1
```

### 인증 실패

- Personal Access Token을 사용하세요
- Token 생성: https://github.com/settings/tokens
- Username: GitHub 사용자명
- Password: Token

### 푸시 오류

```powershell
# 강제 푸시 (주의!)
git push -u origin main --force
```

### Heroku에서 저장소가 안 보임

1. GitHub 저장소가 Public인지 확인
2. Heroku와 GitHub 연결 해제 후 재연결
3. 브라우저 새로고침

---

## 📞 도움말

문제가 계속되면:

1. `CREATE_GITHUB_REPO.md` 파일 참조
2. GitHub 저장소: https://github.com/USERNAME/survey-system-api 확인
3. Heroku 로그: `heroku logs --tail -a survey-system-api`

---

**현재 위치**: 1단계 (GitHub 저장소 생성) 완료 대기 중
**다음 단계**: 저장소 생성 후 `.\connect-github.ps1` 실행
