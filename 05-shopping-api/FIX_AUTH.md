# 🔧 Heroku 인증 문제 해결

## 방법 1: Heroku 로그인 다시 하기

### PowerShell에서 실행:

```powershell
# 1. Heroku 로그인 (브라우저가 열립니다)
heroku login

# 브라우저에서 로그인 완료 후, 아무 키나 눌러 계속 진행

# 2. 인증 확인
heroku auth:whoami

# 3. Git credential 초기화
git config credential.helper store

# 4. 다시 배포
git push heroku master
```

---

## 방법 2: SSH 사용 (더 안정적)

```powershell
# 1. Heroku Git remote를 SSH로 변경
heroku git:remote -a shopping-api-2024

# 2. SSH 키 추가 (아직 안했다면)
heroku keys:add

# 3. 배포
git push heroku master
```

---

## 방법 3: 직접 배포 (가장 확실한 방법)

```powershell
# Maven 빌드
.\mvnw.cmd clean package -DskipTests

# Heroku에 JAR 직접 배포
heroku deploy:jar target/shopping-api-1.0.0.jar --app shopping-api-2024
```

---

## ✅ 추천: 방법 2 (SSH 사용)

가장 안정적입니다. PowerShell에서 다음을 실행하세요:

```powershell
heroku git:remote -a shopping-api-2024
git push heroku master
```

---

## 🆘 그래도 안되면

1. **Heroku 웹사이트에서 직접 배포**
   - https://dashboard.heroku.com/apps/shopping-api-2024/deploy/github
   - GitHub 연동 후 자동 배포

2. **또는 Heroku CLI 재설치**
   ```powershell
   # Heroku CLI 업데이트
   heroku update
   ```

---

## 🎯 빠른 해결

PowerShell에서 이것만 실행하세요:

```powershell
heroku git:remote -a shopping-api-2024 && git push heroku master
```

