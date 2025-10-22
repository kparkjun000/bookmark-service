# Heroku Dashboard를 통한 배포 가이드

## 방법 1: Heroku Dashboard에서 직접 배포 (추천)

이 방법은 Git 인증 문제를 우회할 수 있는 가장 쉬운 방법입니다.

### 1단계: Heroku Dashboard 접속

1. 브라우저에서 https://dashboard.heroku.com 접속
2. 로그인 (aparkjun@naver.com)

### 2단계: 앱 선택

1. Apps 목록에서 `survey-system-api` 클릭
2. "Deploy" 탭 클릭

### 3단계: GitHub 연동 (한 번만 설정)

1. **Deployment method** 섹션에서 "GitHub" 선택
2. "Connect to GitHub" 버튼 클릭
3. GitHub 계정 인증
4. Repository 검색: `survey-system` 또는 해당 저장소 이름 입력
5. "Connect" 버튼 클릭

### 4단계: GitHub에 코드 푸시

먼저 GitHub에 저장소를 만들고 코드를 푸시해야 합니다:

```powershell
# 1. GitHub에서 새 저장소 생성 (웹에서 생성)
#    예: https://github.com/your-username/survey-system

# 2. 로컬에서 GitHub remote 추가
git remote add origin https://github.com/your-username/survey-system.git

# 3. GitHub에 푸시
git branch -M main
git push -u origin main
```

### 5단계: Dashboard에서 배포

1. Heroku Dashboard의 Deploy 탭으로 돌아가기
2. "Manual deploy" 섹션에서

   - Branch: `main` 선택
   - "Deploy Branch" 버튼 클릭

3. 빌드 로그를 실시간으로 확인
4. 배포 완료 후 "View" 버튼 클릭

---

## 방법 2: 터미널에서 직접 로그인 후 배포

만약 Heroku CLI를 직접 사용하고 싶다면:

### 새 PowerShell 터미널을 열어서:

```powershell
# 1. 프로젝트 디렉토리로 이동
cd C:\zero-base13week\09-survey-system\survey-api

# 2. Heroku 로그인 (브라우저가 열립니다)
heroku login
# 아무 키나 누르면 브라우저가 열립니다. 브라우저에서 로그인하세요.

# 3. 인증 확인
heroku auth:whoami

# 4. 배포
git push heroku master

# 5. 로그 확인
heroku logs --tail -a survey-system-api
```

---

## 방법 3: Heroku CLI 재설치 (문제 지속 시)

인증 문제가 계속되면 Heroku CLI를 재설치:

```powershell
# 1. Heroku CLI 제거
# 제어판 > 프로그램 제거 > Heroku CLI 제거

# 2. 최신 Heroku CLI 다운로드
# https://devcenter.heroku.com/articles/heroku-cli

# 3. 설치 후 재시작

# 4. 새 터미널에서 로그인
heroku login
```

---

## 방법 4: Git Credentials 직접 설정

```powershell
# Heroku API Key를 Git credential로 설정
# (API Key는 Heroku Dashboard > Account Settings > API Key에서 확인)

git config --global credential.helper store
echo "https://heroku:YOUR_API_KEY@git.heroku.com" >> ~/.git-credentials
```

---

## 배포 확인

배포가 완료되면:

- **앱 URL**: https://survey-system-api-dd94bac93976.herokuapp.com
- **Swagger UI**: https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

---

## 빠른 테스트

배포 후 API가 작동하는지 확인:

```powershell
# 헬스 체크 (Swagger 접근)
curl https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

# 또는 브라우저에서
# https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html
```

---

## 추천 방법

**가장 쉬운 방법: GitHub 연동 배포**

1. GitHub에 저장소 생성
2. 코드 푸시
3. Heroku Dashboard에서 GitHub 연동
4. Dashboard에서 "Deploy Branch" 버튼 클릭

이 방법이 Git 인증 문제 없이 가장 안정적입니다!
