# GitHub 저장소 생성 가이드

## 방법 1: 브라우저에서 생성 (가장 쉬움)

1. **GitHub 새 저장소 페이지 열기**

   브라우저에서 이 링크 클릭: https://github.com/new

2. **저장소 정보 입력**

   - Repository name: `survey-system-api` (또는 원하는 이름)
   - Description: `Survey Creation and Response Collection API`
   - Public 또는 Private 선택
   - **❌ Initialize this repository with a README 체크 해제** (이미 README가 있음)
   - "Create repository" 버튼 클릭

3. **생성된 저장소에 코드 푸시**

   GitHub에서 보여주는 명령어 중 "…or push an existing repository from the command line" 부분을 사용:

   ```powershell
   # 저장소 URL을 복사한 후 (예: https://github.com/username/survey-system-api.git)

   git remote add origin https://github.com/username/survey-system-api.git
   git branch -M main
   git push -u origin main
   ```

## 방법 2: 현재 터미널에서 직접 실행

아래 명령어를 순서대로 실행하세요:

```powershell
# GitHub 사용자 이름을 입력하세요 (예: aparkjun)
$GITHUB_USERNAME = "YOUR_GITHUB_USERNAME"

# 저장소 이름
$REPO_NAME = "survey-system-api"

# GitHub 저장소 URL
$REPO_URL = "https://github.com/$GITHUB_USERNAME/$REPO_NAME.git"

# 1. GitHub remote 추가
git remote add origin $REPO_URL

# 2. 브랜치를 main으로 변경
git branch -M main

# 3. GitHub에 푸시
git push -u origin main
```

푸시 시 GitHub 인증이 필요하면 사용자명과 Personal Access Token을 입력하세요.

## GitHub Personal Access Token 생성 (처음이라면)

1. https://github.com/settings/tokens 접속
2. "Generate new token (classic)" 클릭
3. Note: `survey-system-api`
4. Expiration: 원하는 기간 선택
5. Scopes:
   - ✅ repo (전체 선택)
6. "Generate token" 클릭
7. **토큰 복사** (한 번만 표시됨!)
8. Git push 시 비밀번호 대신 이 토큰 사용

## 푸시 후 Heroku 연동

GitHub에 푸시가 완료되면:

1. **Heroku Dashboard** 접속: https://dashboard.heroku.com
2. `survey-system-api` 앱 선택
3. **Deploy** 탭 클릭
4. **Deployment method** → "GitHub" 선택
5. "Connect to GitHub" 클릭
6. 저장소 검색: `survey-system-api`
7. "Connect" 버튼 클릭
8. **Manual deploy** 섹션:
   - Branch: `main` 선택
   - "Deploy Branch" 클릭

## 자동 배포 설정 (선택사항)

Heroku Dashboard의 Deploy 탭에서:

1. **Automatic deploys** 섹션
2. Branch: `main` 선택
3. "Enable Automatic Deploys" 클릭

이제 GitHub에 push할 때마다 자동으로 Heroku에 배포됩니다!

## 문제 해결

### "remote origin already exists" 오류

```powershell
git remote remove origin
git remote add origin https://github.com/username/survey-system-api.git
```

### 인증 실패

GitHub Personal Access Token을 사용하세요:

- Username: GitHub 사용자명
- Password: Personal Access Token (위에서 생성한 토큰)

### 브랜치 충돌

```powershell
git pull origin main --allow-unrelated-histories
git push -u origin main
```
