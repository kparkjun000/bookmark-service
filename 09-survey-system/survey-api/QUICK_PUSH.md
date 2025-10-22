# 🚀 GitHub에 바로 푸시하기

## 문제

Heroku에서 "repository is empty" 오류 발생
→ GitHub 저장소에 코드가 없어서 발생

## 해결: 3단계로 간단히 푸시

### 1단계: GitHub 사용자 이름 확인

본인의 GitHub 사용자 이름을 확인하세요.
(예: aparkjun, username123 등)

### 2단계: 아래 명령어 실행

**PowerShell에서 복사하여 붙여넣기:**

```powershell
# GitHub 사용자 이름 설정 (본인 것으로 변경!)
$username = "YOUR_GITHUB_USERNAME"

# GitHub remote 추가
git remote add origin "https://github.com/$username/survey-system-api.git"

# 브랜치를 main으로 변경
git branch -M main

# 최신 변경사항 커밋
git add .
git commit -m "Add deployment guides" 2>$null

# GitHub에 푸시
git push -u origin main
```

### 3단계: 인증

푸시 시 인증 요구:

- **Username**: GitHub 사용자 이름
- **Password**: Personal Access Token (아래 참조)

## Personal Access Token 생성

토큰이 없다면:

1. https://github.com/settings/tokens 접속
2. **"Generate new token (classic)"** 클릭
3. Note: `survey-api`
4. Scopes: **✅ repo** 선택
5. **"Generate token"** 클릭
6. **토큰 복사** (한 번만 표시됨!)
7. Git push 시 Password로 입력

## 푸시 성공 후

1. Heroku Dashboard로 돌아가기
2. **페이지 새로고침 (F5)**
3. "Connect to GitHub" 다시 시도
4. `survey-system-api` 검색
5. "Connect" 버튼 클릭
6. "Deploy Branch" 버튼 클릭

## 한 줄씩 실행하기 (더 간단)

```powershell
# 1. GitHub 사용자 이름 입력 (본인 것으로!)
$username = "aparkjun"  # <-- 여기 수정!

# 2. 나머지 자동 실행
git remote add origin "https://github.com/$username/survey-system-api.git"
git branch -M main
git add .
git commit -m "Initial deployment" 2>$null
git push -u origin main
```

## 문제 해결

### "remote origin already exists"

```powershell
git remote remove origin
# 그리고 다시 시도
```

### 인증 실패

- Personal Access Token 사용 필수
- Username: GitHub 사용자명
- Password: Token (비밀번호 아님!)

### 저장소를 찾을 수 없음

- GitHub에서 `survey-system-api` 저장소가 만들어졌는지 확인
- https://github.com/YOUR_USERNAME/survey-system-api
