# 🚀 가장 쉬운 Heroku 배포 방법 (GitHub 연동)

## ⚠️ 현재 상황

Heroku CLI 로그인에 문제가 있습니다.
**웹 브라우저로 배포하는 것이 가장 쉽습니다!**

## 📌 방법 1: Heroku Dashboard에서 직접 배포 (추천!)

### 1️⃣ Heroku Dashboard 접속

브라우저에서 다음 URL로 이동:

```
https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/heroku-git
```

### 2️⃣ "Deploy using Heroku Git" 섹션 확인

다음 명령어들이 보일 것입니다:

```bash
heroku login
git remote add heroku https://git.heroku.com/zerobase-bookmark-service.git
git push heroku master
```

### 3️⃣ 새 PowerShell 창을 열고 실행

**중요: 새 PowerShell 창을 여세요!**

```powershell
# 1. 디렉토리 이동
cd C:\zero-base13week\04-bookmark-service

# 2. Heroku 로그인 (브라우저 열림)
heroku login

# 3. 브라우저에서 로그인 완료 후, 배포
git push heroku master
```

## 📌 방법 2: GitHub을 통한 자동 배포 (가장 쉬움!)

### 1️⃣ GitHub 저장소 생성

https://github.com/new 접속하여 새 저장소 생성:

- Repository name: `bookmark-service`
- Public 선택
- Create repository 클릭

### 2️⃣ GitHub에 코드 푸시

```bash
# GitHub 저장소 연결 (YOUR_USERNAME을 본인 GitHub 아이디로 변경)
git remote add origin https://github.com/YOUR_USERNAME/bookmark-service.git

# main 브랜치로 변경
git branch -M main

# GitHub에 푸시
git push -u origin main
```

### 3️⃣ Heroku에서 GitHub 연동

1. 브라우저에서 접속:

   ```
   https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github
   ```

2. **"Connect to GitHub"** 버튼 클릭

3. GitHub 로그인 및 권한 승인

4. 저장소 검색: `bookmark-service` 입력

5. **"Connect"** 버튼 클릭

6. **"Enable Automatic Deploys"** 클릭 (선택사항)

7. **"Deploy Branch"** 버튼 클릭 (main 브랜치)

8. 배포 진행 상황 실시간 확인 가능!

## 📌 방법 3: Heroku API Key 사용

### 1️⃣ API Key 가져오기

브라우저에서 접속:

```
https://dashboard.heroku.com/account
```

**"API Key"** 섹션에서 **"Reveal"** 클릭하여 키 복사

### 2️⃣ Git 인증 설정

```bash
# Heroku 원격 저장소를 API Key로 인증
git remote remove heroku
git remote add heroku https://heroku:YOUR_API_KEY@git.heroku.com/zerobase-bookmark-service.git

# 배포
git push heroku master
```

## 🎯 배포 완료 확인

배포가 완료되면 다음 URL로 접속:

### ✨ Swagger UI

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
```

### 📊 앱 대시보드

```
https://dashboard.heroku.com/apps/zerobase-bookmark-service
```

## 🔍 실시간 로그 확인

### 웹에서 확인

```
https://dashboard.heroku.com/apps/zerobase-bookmark-service/logs
```

### CLI로 확인 (로그인 후)

```bash
heroku logs --tail --app zerobase-bookmark-service
```

## ⚡ 빠른 테스트

배포 완료 후 브라우저에서 접속:

1. **Swagger UI**: https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
2. **GET /api/users** 엔드포인트 테스트
3. 샘플 데이터 확인!

## 💡 추천 방법

**가장 쉬운 순서:**

1. 🥇 **GitHub 연동 배포** (웹 브라우저만 사용, CLI 불필요)
2. 🥈 **새 PowerShell에서 heroku login 재시도**
3. 🥉 **API Key 직접 설정**

---

## 📞 도움이 필요하면

문제가 계속되면 다음을 확인하세요:

1. PostgreSQL이 준비되었는지 확인:

   ```
   https://dashboard.heroku.com/apps/zerobase-bookmark-service/resources
   ```

2. 앱 상태 확인:

   ```
   https://dashboard.heroku.com/apps/zerobase-bookmark-service
   ```

3. 로그 확인:
   ```
   https://dashboard.heroku.com/apps/zerobase-bookmark-service/logs
   ```
