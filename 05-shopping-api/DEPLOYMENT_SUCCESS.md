# 🎉 배포 성공! Shopping Mall API

## ✅ 배포 완료

귀하의 쇼핑몰 API가 성공적으로 Heroku에 배포되었습니다!

---

## 🌐 접속 URL

### Swagger UI (API 문서 및 테스트)
```
https://shopping-api-2024.herokuapp.com/swagger-ui.html
```

### API 엔드포인트
```
https://shopping-api-2024.herokuapp.com/api/
```

---

## 📱 주요 API 엔드포인트

### 카테고리 API
- `GET https://shopping-api-2024.herokuapp.com/api/categories` - 전체 카테고리 조회
- `POST https://shopping-api-2024.herokuapp.com/api/categories` - 카테고리 생성

### 상품 API
- `GET https://shopping-api-2024.herokuapp.com/api/products` - 전체 상품 조회 (페이징)
- `GET https://shopping-api-2024.herokuapp.com/api/products/active` - 활성 상품 조회
- `GET https://shopping-api-2024.herokuapp.com/api/products/search?keyword=laptop` - 상품 검색
- `POST https://shopping-api-2024.herokuapp.com/api/products` - 상품 생성

### 사용자 API
- `GET https://shopping-api-2024.herokuapp.com/api/users` - 전체 사용자 조회
- `POST https://shopping-api-2024.herokuapp.com/api/users` - 사용자 생성

### 장바구니 API
- `GET https://shopping-api-2024.herokuapp.com/api/carts/user/{userId}` - 장바구니 조회
- `POST https://shopping-api-2024.herokuapp.com/api/carts/user/{userId}/items` - 장바구니에 상품 추가

### 주문 API
- `GET https://shopping-api-2024.herokuapp.com/api/orders/user/{userId}` - 사용자 주문 조회
- `POST https://shopping-api-2024.herokuapp.com/api/orders` - 주문 생성

### 결제 API
- `POST https://shopping-api-2024.herokuapp.com/api/payments` - 결제 생성
- `POST https://shopping-api-2024.herokuapp.com/api/payments/{id}/process` - 결제 처리

---

## 🎯 Swagger UI에서 API 테스트하기

### 1단계: Categories API 테스트
1. Swagger UI에서 **Category** 섹션 열기
2. `GET /api/categories` 클릭
3. **Try it out** 클릭
4. **Execute** 클릭
5. ✅ 샘플 카테고리 데이터 확인 (Electronics, Clothing, Books)

### 2단계: Products API 테스트
1. **Product** 섹션 열기
2. `GET /api/products` 클릭
3. **Try it out** 클릭
4. **Execute** 클릭
5. ✅ 샘플 상품 데이터 확인 (Laptop, Smartphone 등)

### 3단계: 사용자 생성 테스트
1. **User** 섹션 열기
2. `POST /api/users` 클릭
3. **Try it out** 클릭
4. Request body 예시:
   ```json
   {
     "email": "test@example.com",
     "name": "Test User",
     "phone": "+82-10-1234-5678",
     "address": "서울시 강남구"
   }
   ```
5. **Execute** 클릭
6. ✅ 201 Created 응답 확인

---

## 🔍 유용한 명령어

### 로그 확인
```powershell
heroku logs --tail --app shopping-api-2024
```

### 앱 재시작
```powershell
heroku restart --app shopping-api-2024
```

### 앱 상태 확인
```powershell
heroku ps --app shopping-api-2024
```

### 데이터베이스 정보
```powershell
heroku pg:info --app shopping-api-2024
```

### 환경 변수 확인
```powershell
heroku config --app shopping-api-2024
```

---

## 📊 현재 상태

- ✅ **앱 상태**: 실행 중 (Running)
- ✅ **Dyno 타입**: Basic
- ✅ **PostgreSQL**: Essential-0
- ✅ **GitHub 연동**: 완료
- ✅ **Swagger UI**: 활성화

---

## 🔄 코드 업데이트 방법

코드를 수정한 후:

```powershell
git add .
git commit -m "Update: 변경 내용"
git push origin main
```

GitHub에 푸시하면 자동으로 Heroku에 배포됩니다! (자동 배포 활성화 시)

---

## 💰 비용 안내

- **Basic Dyno**: $7/월
- **Essential PostgreSQL**: $5/월
- **총 비용**: $12/월

---

## 📚 샘플 데이터

배포 시 자동으로 생성된 샘플 데이터:

### 카테고리
- Electronics
- Clothing
- Books

### 상품
- Laptop Pro 15 (Electronics)
- Smartphone X (Electronics)
- Classic T-Shirt (Clothing)
- Denim Jeans (Clothing)
- Java Programming Guide (Books)

### 사용자
- john.doe@example.com
- jane.smith@example.com

---

## 🎓 다음 단계

1. ✅ Swagger UI에서 모든 API 테스트
2. ✅ 실제 데이터로 CRUD 작업 연습
3. ✅ Postman으로 API 테스트
4. ✅ 프론트엔드 애플리케이션 연동

---

## 🆘 문제 발생 시

### Application Error가 나타나면
```powershell
heroku logs --tail --app shopping-api-2024
```
로그를 확인하여 문제를 파악하세요.

### 데이터베이스 연결 오류
```powershell
heroku config:get DATABASE_URL --app shopping-api-2024
```
DATABASE_URL이 올바르게 설정되어 있는지 확인하세요.

---

## 🎉 축하합니다!

성공적으로 쇼핑몰 API를 Heroku에 배포하셨습니다!

**GitHub 저장소**: https://github.com/kparkjun000/shopping-api
**Swagger UI**: https://shopping-api-2024.herokuapp.com/swagger-ui.html
**Heroku Dashboard**: https://dashboard.heroku.com/apps/shopping-api-2024

Happy Coding! 🚀

