# Quick Start Guide - Shopping Mall API

빠르게 시작하기 위한 간단한 가이드입니다.

## 🚀 빠른 시작

### 1단계: 필수 요구사항 확인

- Java 17 이상 설치
- PostgreSQL 16 이상 설치
- Maven 3.9.x 이상 설치 (또는 프로젝트의 Maven Wrapper 사용)

### 2단계: PostgreSQL 데이터베이스 생성

```sql
CREATE DATABASE shopping_db;
```

### 3단계: 애플리케이션 실행

#### Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

#### Mac/Linux:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

#### 또는 일반 Maven:

```bash
mvn spring-boot:run
```

### 4단계: Swagger UI 접속

브라우저에서 다음 주소로 접속:

```
http://localhost:8080/swagger-ui.html
```

## 🎯 API 테스트

### Swagger UI에서 테스트하기

1. **카테고리 생성**

   - `POST /api/categories` 엔드포인트 선택
   - "Try it out" 클릭
   - Request body:
     ```json
     {
       "name": "Electronics",
       "description": "Electronic devices"
     }
     ```
   - "Execute" 클릭

2. **상품 생성**

   - `POST /api/products` 엔드포인트 선택
   - Request body:
     ```json
     {
       "name": "Laptop",
       "description": "High-performance laptop",
       "price": 1299.99,
       "stockQuantity": 50,
       "categoryId": 1,
       "isActive": true
     }
     ```

3. **사용자 생성**

   - `POST /api/users` 엔드포인트 선택
   - Request body:
     ```json
     {
       "email": "user@example.com",
       "name": "Test User",
       "phone": "+1-555-0000",
       "address": "123 Main St"
     }
     ```

4. **장바구니에 상품 추가**

   - `POST /api/carts/user/{userId}/items` 엔드포인트 선택
   - userId에 1 입력
   - Request body:
     ```json
     {
       "productId": 1,
       "quantity": 2
     }
     ```

5. **주문 생성**
   - `POST /api/orders` 엔드포인트 선택
   - Request body:
     ```json
     {
       "userId": 1,
       "shippingAddress": "123 Main St, City"
     }
     ```

## 📊 샘플 데이터

애플리케이션을 처음 실행하면 자동으로 샘플 데이터가 생성됩니다:

- **카테고리**: Electronics, Clothing, Books
- **상품**: 각 카테고리별 샘플 상품들
- **사용자**: 2명의 테스트 사용자

## 🔧 설정 변경

### 데이터베이스 설정 변경

`src/main/resources/application.yml` 파일에서 데이터베이스 설정을 변경할 수 있습니다:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/shopping_db
    username: your_username
    password: your_password
```

### 포트 변경

기본 포트는 8080입니다. 변경하려면:

```yaml
server:
  port: 8090
```

또는 실행 시:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8090
```

## 📱 주요 엔드포인트

### 카테고리

- `GET /api/categories` - 전체 카테고리 조회
- `POST /api/categories` - 카테고리 생성

### 상품

- `GET /api/products` - 전체 상품 조회 (페이징)
- `GET /api/products/search?keyword=laptop` - 상품 검색
- `POST /api/products` - 상품 생성

### 장바구니

- `GET /api/carts/user/{userId}` - 장바구니 조회
- `POST /api/carts/user/{userId}/items` - 장바구니에 상품 추가

### 주문

- `GET /api/orders/user/{userId}` - 사용자 주문 조회
- `POST /api/orders` - 주문 생성

### 결제

- `POST /api/payments` - 결제 생성
- `POST /api/payments/{id}/process` - 결제 처리

## 🐛 문제 해결

### 애플리케이션이 시작되지 않는 경우

1. **Java 버전 확인**

   ```bash
   java -version
   ```

   Java 17 이상이어야 합니다.

2. **PostgreSQL 실행 확인**

   ```bash
   # Windows
   Get-Service postgresql*

   # Mac
   brew services list | grep postgresql

   # Linux
   sudo systemctl status postgresql
   ```

3. **데이터베이스 연결 확인**
   - PostgreSQL이 실행 중인지 확인
   - 데이터베이스 이름, 사용자명, 비밀번호가 올바른지 확인

### 포트 충돌

8080 포트가 이미 사용 중인 경우:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8090
```

## 📚 더 알아보기

- [README.md](README.md) - 전체 문서
- [API_GUIDE.md](API_GUIDE.md) - 상세 API 가이드
- [DEPLOYMENT.md](DEPLOYMENT.md) - Heroku 배포 가이드

## 🎓 학습 순서 추천

1. ✅ Swagger UI에서 API 탐색
2. ✅ 카테고리와 상품 생성 연습
3. ✅ 사용자 생성 및 장바구니 사용
4. ✅ 주문 프로세스 전체 흐름 이해
5. ✅ 결제 처리 테스트

## 💡 팁

- Swagger UI의 "Try it out" 기능을 활용하여 쉽게 API 테스트 가능
- 샘플 데이터가 자동 생성되므로 바로 테스트 가능
- 모든 API는 JSON 형식으로 통신
- 에러 발생 시 응답 메시지에 상세한 정보 포함

즐거운 개발 되세요! 🚀
