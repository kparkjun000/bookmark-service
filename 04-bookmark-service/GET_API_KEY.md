# 🔑 Heroku API Key 가져오기

## 방법 1: 웹 브라우저 (가장 쉬움)

### 1단계: Heroku Account 페이지 접속

```
https://dashboard.heroku.com/account
```

### 2단계: API Key 섹션 찾기

페이지 중간에 **"API Key"** 섹션이 있습니다.

### 3단계: Reveal 버튼 클릭

**"Reveal"** 버튼을 클릭하면 API Key가 표시됩니다.

### 4단계: API Key 복사

표시된 긴 문자열을 복사하세요.

---

## 방법 2: CLI로 가져오기 (로그인 후)

브라우저 로그인이 성공한 경우:

```bash
heroku auth:token
```

---

## API Key 사용 방법

### PowerShell에서 사용:

```powershell
# API Key 설정
$env:HEROKU_API_KEY = "YOUR_API_KEY_HERE"

# 로그인 확인
heroku auth:whoami

# 배포
git push heroku master
```

### 또는 자동 스크립트 사용:

```powershell
.\deploy-with-apikey.ps1
```

이 스크립트를 실행하면:

1. API Key 입력 프롬프트가 나타남
2. 자동으로 인증 설정
3. 배포 진행
4. Swagger UI 자동 열기

---

## 보안 주의사항

⚠️ **중요**: API Key는 패스워드처럼 보안이 필요합니다!

- ❌ Git에 커밋하지 마세요
- ❌ 공개 저장소에 올리지 마세요
- ❌ 다른 사람과 공유하지 마세요
- ✅ 환경 변수로만 사용하세요
- ✅ 필요시 재생성하세요 (Regenerate 버튼)

---

## 빠른 배포 명령어

API Key를 얻은 후:

```powershell
# PowerShell에서 실행
.\deploy-with-apikey.ps1
```

또는:

```powershell
# 수동으로 설정
$env:HEROKU_API_KEY = "YOUR_API_KEY"
git push heroku master
```

---

## 문제 해결

### "Invalid credentials" 오류

- API Key가 정확한지 확인
- https://dashboard.heroku.com/account 에서 Regenerate 후 재시도

### Git push 실패

```powershell
# Remote 재설정
git remote remove heroku
$apiKey = "YOUR_API_KEY"
git remote add heroku "https://heroku:$apiKey@git.heroku.com/zerobase-bookmark-service.git"
git push heroku master
```
