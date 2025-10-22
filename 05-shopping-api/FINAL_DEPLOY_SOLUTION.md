# 🎯 최종 배포 해결 방법

Git 인증 문제로 인해 직접 `git push heroku` 방식이 작동하지 않습니다.
아래 두 가지 확실한 방법 중 하나를 선택하세요.

---

## ✅ 방법 1: GitHub 연동 (가장 쉬움! 추천)

### 1단계: GitHub에 저장소 생성

1. GitHub (https://github.com) 접속
2. 새 저장소 생성: **shopping-api**
3. Public 또는 Private 선택

### 2단계: GitHub에 푸시

PowerShell에서 실행:

```powershell
# GitHub 원격 저장소 추가 (your-username을 본인 GitHub 아이디로 변경)
git remote add origin https://github.com/your-username/shopping-api.git

# GitHub에 푸시
git branch -M main
git push -u origin main
```

### 3단계: Heroku Dashboard에서 연동

1. Heroku Dashboard 접속: https://dashboard.heroku.com/apps/shopping-api-2024
2. **Deploy** 탭 클릭
3. **Deployment method**에서 **GitHub** 선택
4. **Connect to GitHub** 클릭하여 GitHub 계정 연결
5. 저장소 이름 **shopping-api** 검색 후 **Connect** 클릭
6. **Enable Automatic Deploys** 클릭 (선택사항)
7. **Deploy Branch**에서 `main` 선택 후 **Deploy Branch** 클릭

### 4단계: 배포 완료!

배포가 완료되면:
- ✅ **Swagger UI**: https://shopping-api-2024.herokuapp.com/swagger-ui.html

---

## 🐳 방법 2: Docker Container 배포

### 1단계: Dockerfile 생성 (이미 있음)

파일이 이미 생성되어 있는지 확인:

```dockerfile
# Dockerfile
FROM eclipse-temurin:17-jre-alpine
COPY target/shopping-api-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### 2단계: Container Registry로 배포

```powershell
# Heroku Container Registry 로그인
heroku container:login

# Docker 이미지 빌드 및 푸시
heroku container:push web --app shopping-api-2024

# 릴리스
heroku container:release web --app shopping-api-2024
```

---

## 🌟 방법 3: Heroku CLI 업데이트 후 재시도

```powershell
# Heroku CLI 업데이트
heroku update

# 로그아웃
heroku logout

# 재로그인
heroku login

# 다시 시도
git push heroku master
```

---

## 📊 현재 상태 확인

```powershell
# 앱 상태
heroku ps --app shopping-api-2024

# 로그
heroku logs --tail --app shopping-api-2024

# 설정 확인
heroku config --app shopping-api-2024
```

---

## 🎯 추천 순서

1. ⭐ **방법 1 (GitHub 연동)** - 가장 쉽고 확실함
2. 🐳 **방법 2 (Docker)** - 기술적으로 깔끔함
3. 🔄 **방법 3 (CLI 업데이트)** - 운이 좋으면 성공

---

## ✅ 배포 완료 후

### Swagger UI 접속

```
https://shopping-api-2024.herokuapp.com/swagger-ui.html
```

### API 테스트

```bash
# 카테고리 조회
curl https://shopping-api-2024.herokuapp.com/api/categories

# 상품 조회
curl https://shopping-api-2024.herokuapp.com/api/products
```

---

## 🆘 도움이 필요하면

1. **Heroku Status**: https://status.heroku.com/
2. **로그 확인**: `heroku logs --tail --app shopping-api-2024`
3. **Dashboard**: https://dashboard.heroku.com/apps/shopping-api-2024

---

## 💰 비용 안내

- **Eco Dyno**: $5/월
- **PostgreSQL Essential**: $5/월
- **총 $10/월**

첫 달은 무료 크레딧이 있을 수 있습니다.

---

Happy Coding! 🚀

