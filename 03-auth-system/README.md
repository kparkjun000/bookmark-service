# JWT Authentication & Authorization System

Spring Boot 3.2.x와 Java 21을 사용한 JWT 기반 인증 및 권한 관리 RESTful API 시스템입니다.

## 📋 목차
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [환경 변수](#-환경-변수)
- [Heroku 배포](#-heroku-배포)
- [API 엔드포인트](#-api-엔드포인트)
- [보안](#-보안)

## 🚀 주요 기능

- ✅ **회원가입 및 로그인**: 사용자 등록 및 인증
- ✅ **JWT 토큰 관리**: Access Token 및 Refresh Token 발급/갱신
- ✅ **역할 기반 접근 제어 (RBAC)**: USER, MODERATOR, ADMIN 권한
- ✅ **사용자 관리**: 사용자 조회, 수정, 삭제
- ✅ **Swagger UI**: API 문서 자동 생성 및 테스트
- ✅ **PostgreSQL**: 데이터 영속성
- ✅ **예외 처리**: 글로벌 예외 핸들러

## 🛠 기술 스택

- **언어**: Java 21 (LTS)
- **프레임워크**: Spring Boot 3.2.5
- **보안**: Spring Security 6.x + JWT (jjwt 0.12.5)
- **빌드 도구**: Maven 3.9.x
- **데이터베이스**: PostgreSQL 16+
- **ORM**: Spring Data JPA + Hibernate 6.x
- **API 문서**: Springdoc OpenAPI 3 (Swagger UI)
- **배포**: Heroku

## 🏁 시작하기

### 사전 요구사항

- Java 21 이상
- Maven 3.9.x 이상
- PostgreSQL 16 이상

### 로컬 실행

1. **저장소 클론**
```bash
git clone <repository-url>
cd 03-auth-system
```

2. **PostgreSQL 데이터베이스 생성**
```sql
CREATE DATABASE authdb;
```

3. **환경 변수 설정** (선택 사항)
```bash
export DATABASE_URL=jdbc:postgresql://localhost:5432/authdb
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=your_password
export JWT_SECRET=your_secret_key_here
```

4. **애플리케이션 빌드**
```bash
mvn clean package
```

5. **애플리케이션 실행**
```bash
mvn spring-boot:run
```

또는

```bash
java -jar target/auth-system-1.0.0.jar
```

애플리케이션은 `http://localhost:8080`에서 실행됩니다.

## 📚 API 문서

애플리케이션 실행 후 Swagger UI에 접속하여 API 문서를 확인하고 테스트할 수 있습니다.

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## 🔐 환경 변수

| 변수명 | 설명 | 기본값 |
|--------|------|--------|
| `DATABASE_URL` | PostgreSQL 연결 URL | `jdbc:postgresql://localhost:5432/authdb` |
| `DATABASE_USERNAME` | 데이터베이스 사용자명 | `postgres` |
| `DATABASE_PASSWORD` | 데이터베이스 비밀번호 | `postgres` |
| `JWT_SECRET` | JWT 서명 키 (최소 256비트) | 기본값 제공 |
| `PORT` | 서버 포트 | `8080` |

## 🚢 Heroku 배포

### 1. Heroku CLI 설치
```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# https://devcenter.heroku.com/articles/heroku-cli 에서 설치
```

### 2. Heroku 로그인
```bash
heroku login
```

### 3. Heroku 앱 생성
```bash
heroku create your-app-name
```

### 4. PostgreSQL 추가
```bash
heroku addons:create heroku-postgresql:mini
```

### 5. 환경 변수 설정
```bash
heroku config:set JWT_SECRET=your_very_secure_secret_key_here
```

### 6. 배포
```bash
git push heroku main
```

### 7. 로그 확인
```bash
heroku logs --tail
```

### 8. 앱 열기
```bash
heroku open
```

Swagger UI URL: `https://your-app-name.herokuapp.com/swagger-ui.html`

## 📡 API 엔드포인트

### 인증 API (`/api/auth`)

| Method | Endpoint | 설명 | 인증 필요 |
|--------|----------|------|-----------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |
| POST | `/api/auth/refresh` | 토큰 갱신 | ❌ |
| POST | `/api/auth/logout` | 로그아웃 | ✅ |

### 사용자 API (`/api/users`)

| Method | Endpoint | 설명 | 권한 |
|--------|----------|------|------|
| GET | `/api/users/me` | 내 정보 조회 | USER |
| GET | `/api/users` | 모든 사용자 조회 | ADMIN, MODERATOR |
| GET | `/api/users/{id}` | 특정 사용자 조회 | ADMIN, MODERATOR |
| DELETE | `/api/users/{id}` | 사용자 삭제 | ADMIN |
| PATCH | `/api/users/{id}/status` | 사용자 상태 변경 | ADMIN |

### 관리자 API (`/api/admin`)

| Method | Endpoint | 설명 | 권한 |
|--------|----------|------|------|
| GET | `/api/admin/dashboard` | 관리자 대시보드 | ADMIN |

### 사용 예시

#### 1. 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "email": "john@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
```

#### 2. 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "johndoe",
    "password": "password123"
  }'
```

응답:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["ROLE_USER"]
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

#### 3. 인증이 필요한 API 호출
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

#### 4. 토큰 갱신
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

## 🔒 보안

### JWT 토큰
- **Access Token**: 24시간 유효
- **Refresh Token**: 7일 유효
- HMAC-SHA256 알고리즘 사용

### 비밀번호
- BCrypt 해싱 알고리즘 사용
- 최소 6자 이상

### 권한 레벨
1. **USER**: 기본 사용자 권한
   - 자신의 정보 조회
   
2. **MODERATOR**: 중간 관리자 권한
   - 모든 사용자 조회
   
3. **ADMIN**: 최고 관리자 권한
   - 사용자 생성, 수정, 삭제
   - 사용자 상태 변경
   - 관리자 대시보드 접근

## 📁 프로젝트 구조

```
src/main/java/com/auth/
├── config/              # 설정 클래스
│   └── OpenApiConfig.java
├── controller/          # REST 컨트롤러
│   ├── AuthController.java
│   ├── UserController.java
│   └── AdminController.java
├── dto/                 # 데이터 전송 객체
│   ├── ApiResponse.java
│   ├── SignupRequest.java
│   ├── LoginRequest.java
│   ├── JwtResponse.java
│   ├── RefreshTokenRequest.java
│   └── UserResponse.java
├── exception/           # 예외 처리
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── TokenRefreshException.java
├── model/               # 엔티티 모델
│   ├── User.java
│   ├── Role.java
│   └── RefreshToken.java
├── repository/          # JPA 레포지토리
│   ├── UserRepository.java
│   └── RefreshTokenRepository.java
├── security/            # 보안 설정
│   ├── JwtUtil.java
│   ├── JwtAuthenticationFilter.java
│   ├── UserDetailsServiceImpl.java
│   ├── SecurityConfig.java
│   └── JwtAuthenticationEntryPoint.java
├── service/             # 비즈니스 로직
│   ├── AuthService.java
│   └── UserService.java
└── AuthSystemApplication.java
```

## 🧪 테스트

```bash
mvn test
```

## 📝 라이선스

This project is licensed under the MIT License.

## 👥 기여

이슈와 풀 리퀘스트를 환영합니다!

## 📞 문의

문제가 발생하거나 질문이 있으시면 이슈를 등록해주세요.


#   C O R S   F i x  
 