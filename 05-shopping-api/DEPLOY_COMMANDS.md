# 🚀 Heroku 배포 명령어 (순서대로 실행)

아래 명령어를 **PowerShell 또는 CMD**에서 순서대로 복사하여 실행하세요.

## 1단계: Heroku 로그인

```bash
heroku login
```

→ 브라우저가 열리면 로그인하세요. 로그인 완료 후 터미널로 돌아옵니다.

---

## 2단계: 앱 생성

```bash
heroku create shopping-api-2024
```

> 💡 `shopping-api-2024` 부분을 원하는 이름으로 변경하거나, 이름을 생략하면 자동 생성됩니다.

---

## 3단계: PostgreSQL 추가

```bash
heroku addons:create heroku-postgresql:essential-0
```

---

## 4단계: Heroku에 배포

```bash
git push heroku master
```

> 💡 만약 기본 브랜치가 `main`이라면: `git push heroku main`

---

## 5단계: Swagger UI 열기

```bash
heroku open /swagger-ui.html
```

---

## ✅ 완료!

배포가 완료되면 다음 URL로 접속할 수 있습니다:

- **Swagger UI**: `https://your-app-name.herokuapp.com/swagger-ui.html`
- **API**: `https://your-app-name.herokuapp.com/api/`

---

## 🔍 유용한 명령어

```bash
# 로그 보기
heroku logs --tail

# 앱 상태 확인
heroku ps

# 환경 변수 확인
heroku config

# 앱 재시작
heroku restart
```
