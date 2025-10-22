# 🚀 GitHub 연동으로 Heroku 배포하기

## 1단계: GitHub 저장소 생성

1. GitHub (https://github.com) 접속 후 로그인
2. 오른쪽 상단 **+** 클릭 → **New repository**
3. Repository name: `shopping-api` 입력
4. Public 선택
5. **Create repository** 클릭

## 2단계: PowerShell에서 GitHub에 푸시

```powershell
# GitHub 저장소 추가 (your-username을 본인 GitHub 아이디로 변경!)
git remote add origin https://github.com/your-username/shopping-api.git

# 브랜치 이름 변경
git branch -M main

# GitHub에 푸시
git push -u origin main
```

**예시:**
만약 GitHub 아이디가 `johndoe`라면:

```powershell
git remote add origin https://github.com/johndoe/shopping-api.git
git branch -M main
git push -u origin main
```

## 3단계: Heroku Dashboard에서 연동

1. Heroku Dashboard 접속: https://dashboard.heroku.com/apps/shopping-api-2024

2. **Deploy** 탭 클릭

3. **Deployment method** 섹션에서 **GitHub** 선택

4. **Connect to GitHub** 버튼 클릭

   - GitHub 로그인 및 권한 승인

5. **repo-name** 검색창에 `shopping-api` 입력

6. **Search** 클릭 후 저장소 옆의 **Connect** 클릭

7. **Manual deploy** 섹션에서:
   - Branch: `main` 선택
   - **Deploy Branch** 클릭

## 4단계: 배포 완료 확인

배포가 시작되면 로그가 표시됩니다. 완료되면:

```
✅ Your app was successfully deployed
```

**View** 버튼을 클릭하거나 다음 URL로 접속:

### Swagger UI

```
https://shopping-api-2024.herokuapp.com/swagger-ui.html
```

### API Endpoints

```
https://shopping-api-2024.herokuapp.com/api/categories
https://shopping-api-2024.herokuapp.com/api/products
https://shopping-api-2024.herokuapp.com/api/orders
```

## 5단계: 자동 배포 설정 (선택사항)

**Automatic deploys** 섹션에서:

- **Enable Automatic Deploys** 클릭
- 이제 GitHub에 푸시할 때마다 자동으로 Heroku에 배포됩니다!

---

## 🎉 완료!

이제 Swagger UI가 포함된 쇼핑몰 API가 Heroku에 배포되었습니다!

### 테스트해보기

1. Swagger UI 접속
2. Categories API 섹션 열기
3. `GET /api/categories` 클릭
4. **Try it out** 클릭
5. **Execute** 클릭

샘플 데이터가 표시됩니다!

---

## 💡 추가 정보

### 로그 보기

```powershell
heroku logs --tail --app shopping-api-2024
```

### 앱 재시작

```powershell
heroku restart --app shopping-api-2024
```

### 데이터베이스 확인

```powershell
heroku pg:info --app shopping-api-2024
```

Happy Coding! 🚀
