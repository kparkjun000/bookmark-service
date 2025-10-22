# 🚀 GitHub 연동 배포 (Git 인증 불필요!)

## ✅ 이 방법의 장점

- **Heroku Git 인증 필요 없음**
- **브라우저에서 간단히 연동**
- **자동 배포 가능**

---

## 📝 1단계: GitHub 저장소 생성 (1분)

브라우저에서 https://github.com/new 가 열려 있습니다.

1. **Repository name**: `bookmark-service` 입력
2. **Public** 선택 (또는 Private도 가능)
3. ❌ **Initialize this repository with:** 체크하지 마세요 (빈 저장소로 생성)
4. **Create repository** 버튼 클릭

---

## 📝 2단계: PowerShell에서 GitHub에 푸시

GitHub 저장소가 생성되면, **저장소 URL**이 보입니다.
예: `https://github.com/your-username/bookmark-service.git`

### PowerShell에서 실행:

```powershell
# GitHub 저장소 URL을 YOUR_GITHUB_URL에 넣으세요
$GITHUB_URL = "https://github.com/your-username/bookmark-service.git"

# GitHub에 푸시
git remote remove origin
git remote add origin $GITHUB_URL
git branch -M main
git push -u origin main
```

### GitHub 인증이 필요하면:

- Personal Access Token 생성: https://github.com/settings/tokens
- "Generate new token (classic)" 클릭
- **repo** 권한 체크
- Token 생성 후 복사
- 푸시 시 비밀번호 대신 Token 사용

---

## 📝 3단계: Heroku에서 GitHub 연동 (2분)

### PowerShell에서 실행:

```powershell
Start-Process "https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github"
```

또는 브라우저에서 직접 접속:

```
https://dashboard.heroku.com/apps/zerobase-bookmark-service/deploy/github
```

### Heroku Dashboard에서:

1. **"Connect to GitHub"** 버튼 클릭
2. GitHub 로그인 및 권한 승인
3. 저장소 검색: `bookmark-service` 입력
4. **"Connect"** 버튼 클릭
5. **"Enable Automatic Deploys"** 클릭 (선택사항)
6. **"Deploy Branch"** 버튼 클릭 (main 브랜치)

---

## 📝 4단계: 배포 진행 확인 (3-5분)

Heroku Dashboard에서 실시간으로 배포 로그를 볼 수 있습니다!

- ✅ Building
- ✅ Release
- ✅ Deploy

---

## 🎉 5단계: Swagger UI 접속!

배포가 완료되면:

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
```

### PowerShell에서 자동으로 열기:

```powershell
Start-Process "https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html"
```

---

## 📊 확인 사항

### 샘플 데이터 자동 생성:

- 사용자: demo@example.com
- 태그: 개발, 참고자료, 튜토리얼
- 북마크: Spring Boot, PostgreSQL, GitHub

### API 테스트:

1. Swagger UI에서 **GET /api/users** 클릭
2. "Try it out" 버튼 클릭
3. "Execute" 버튼 클릭
4. Response에서 샘플 데이터 확인 ✅

---

## 🔄 다음 배포부터는?

코드를 수정한 후:

```powershell
git add .
git commit -m "Update features"
git push origin main
```

GitHub에 푸시하면 Heroku가 **자동으로 배포**합니다! (Automatic Deploys 활성화한 경우)

---

## 🐛 문제 해결

### GitHub 푸시 실패:

```powershell
# Personal Access Token 생성
# https://github.com/settings/tokens

# Token으로 인증
$TOKEN = "your_github_token"
$GITHUB_URL = "https://${TOKEN}@github.com/your-username/bookmark-service.git"
git remote set-url origin $GITHUB_URL
git push -u origin main
```

### Heroku 배포 실패:

- Heroku Dashboard에서 로그 확인
- PostgreSQL이 준비되었는지 확인
- "Deploy Branch" 다시 클릭

---

## 📞 참고 링크

- **Heroku Dashboard**: https://dashboard.heroku.com/apps/zerobase-bookmark-service
- **GitHub Token 생성**: https://github.com/settings/tokens
- **Swagger UI**: https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html

---

**이 방법은 Heroku Git 인증 문제를 완전히 우회합니다!** ✅
