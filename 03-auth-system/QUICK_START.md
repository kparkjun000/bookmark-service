# 빠른 시작 가이드

JWT 인증 시스템을 5분 안에 실행해보세요!

## ⚡ 빠른 시작

### 방법 1: Docker Compose (권장)

```bash
# 1. 저장소 클론
git clone <repository-url>
cd 03-auth-system

# 2. Docker Compose로 실행 (PostgreSQL 포함)
docker-compose up -d

# 3. 로그 확인
docker-compose logs -f app

# 4. Swagger UI 접속
# http://localhost:8080/swagger-ui.html
```

완료! 🎉

### 방법 2: 로컬 실행

**사전 요구사항**: Java 21, Maven, PostgreSQL

```bash
# 1. PostgreSQL 데이터베이스 생성
createdb authdb

# 2. 애플리케이션 실행
mvn spring-boot:run

# 3. Swagger UI 접속
# http://localhost:8080/swagger-ui.html
```

## 🧪 첫 번째 API 호출

### 1️⃣ 회원가입

**Swagger UI에서**:

- `/api/auth/signup` 엔드포인트 찾기
- "Try it out" 클릭
- 아래 데이터 입력:

```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "password123",
  "roles": ["USER"]
}
```

- "Execute" 클릭

**curl 사용**:

```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123",
    "roles": ["USER"]
  }'
```

### 2️⃣ 로그인

**Swagger UI에서**:

- `/api/auth/login` 엔드포인트 찾기
- "Try it out" 클릭
- 아래 데이터 입력:

```json
{
  "username": "testuser",
  "password": "password123"
}
```

- "Execute" 클릭
- **Response에서 `accessToken` 복사!**

**curl 사용**:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### 3️⃣ 인증된 API 호출

**Swagger UI에서**:

- 페이지 상단의 🔓 "Authorize" 버튼 클릭
- `Bearer {your_access_token}` 형식으로 토큰 입력
  - 예: `Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`
- "Authorize" 클릭
- 이제 모든 보호된 엔드포인트를 호출할 수 있습니다!
- `/api/users/me`로 내 정보 조회해보기

**curl 사용**:

```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## 🎯 다음 단계

### 관리자 계정 만들기

```json
{
  "username": "admin",
  "email": "admin@example.com",
  "password": "admin123",
  "roles": ["ADMIN"]
}
```

관리자로 로그인 후:

- `/api/users` - 모든 사용자 조회
- `/api/users/{id}/status` - 사용자 상태 변경
- `/api/admin/dashboard` - 관리자 대시보드 접근

### 권한 테스트

1. **USER 계정 생성** → 일반 API만 접근 가능
2. **MODERATOR 계정 생성** → 사용자 조회 가능
3. **ADMIN 계정 생성** → 모든 API 접근 가능

각 계정으로 로그인하여 `/api/admin/dashboard` 접근 시도해보세요!

## 📚 더 알아보기

- **전체 API 문서**: [API_USAGE_GUIDE.md](API_USAGE_GUIDE.md)
- **배포 가이드**: [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)
- **프로젝트 개요**: [README.md](README.md)

## 🛠️ 문제 해결

### 포트 충돌

```bash
# 다른 포트로 실행
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Dserver.port=8090"
```

### 데이터베이스 연결 오류

```bash
# PostgreSQL 실행 확인
psql -U postgres -l

# 데이터베이스 재생성
dropdb authdb
createdb authdb
```

### Docker 문제

```bash
# 모든 컨테이너 중지 및 재시작
docker-compose down -v
docker-compose up -d --build
```

## ⏱️ 소요 시간

- ✅ Docker 방식: **2분**
- ✅ 로컬 방식: **5분**
- ✅ Heroku 배포: **10분**

## 🎓 학습 리소스

### API 엔드포인트 목록

| 엔드포인트               | 메서드 | 설명        | 권한            |
| ------------------------ | ------ | ----------- | --------------- |
| `/api/auth/signup`       | POST   | 회원가입    | Public          |
| `/api/auth/login`        | POST   | 로그인      | Public          |
| `/api/auth/refresh`      | POST   | 토큰 갱신   | Public          |
| `/api/auth/logout`       | POST   | 로그아웃    | USER            |
| `/api/users/me`          | GET    | 내 정보     | USER            |
| `/api/users`             | GET    | 전체 사용자 | ADMIN/MODERATOR |
| `/api/users/{id}`        | GET    | 특정 사용자 | ADMIN/MODERATOR |
| `/api/users/{id}`        | DELETE | 사용자 삭제 | ADMIN           |
| `/api/users/{id}/status` | PATCH  | 상태 변경   | ADMIN           |
| `/api/admin/dashboard`   | GET    | 대시보드    | ADMIN           |

### 주요 기능

- ✨ JWT 액세스 토큰 (24시간)
- 🔄 리프레시 토큰 (7일)
- 🔐 BCrypt 비밀번호 암호화
- 👥 역할 기반 접근 제어 (RBAC)
- 📖 Swagger/OpenAPI 문서
- 🐘 PostgreSQL 데이터베이스
- 🐳 Docker 지원
- ☁️ Heroku 배포 준비 완료

## 💡 팁

1. **Swagger UI 활용**: API를 직접 테스트하기 가장 쉬운 방법
2. **Postman 사용**: 복잡한 시나리오 테스트에 유용
3. **로그 확인**: `docker-compose logs -f` 또는 `heroku logs --tail`
4. **환경 변수**: 프로덕션에서는 반드시 JWT_SECRET 변경

즐거운 개발 되세요! 🚀
