# API 사용 가이드

이 문서는 JWT 인증 시스템 API의 상세한 사용 방법을 설명합니다.

## 📋 목차
- [인증 흐름](#인증-흐름)
- [API 상세 가이드](#api-상세-가이드)
- [에러 코드](#에러-코드)
- [Postman 컬렉션](#postman-컬렉션)

## 🔐 인증 흐름

```
1. 회원가입 (Signup)
   POST /api/auth/signup
   ↓
2. 로그인 (Login)
   POST /api/auth/login
   ↓
3. Access Token + Refresh Token 발급
   ↓
4. 인증이 필요한 API 호출
   Header: Authorization: Bearer {access_token}
   ↓
5. Token 만료 시
   POST /api/auth/refresh
   ↓
6. 새로운 Access Token 발급
```

## 📡 API 상세 가이드

### 1. 회원가입

**Endpoint**: `POST /api/auth/signup`

**Request Body**:
```json
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "password123",
  "roles": ["USER"]
}
```

**Response** (201 Created):
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": null,
  "timestamp": "2024-01-01T12:00:00"
}
```

**가능한 Roles**:
- `USER` - 일반 사용자 (기본값)
- `MODERATOR` - 중간 관리자
- `ADMIN` - 최고 관리자

### 2. 로그인

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "username": "johndoe",
  "password": "password123"
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDEwODE2MDB9.xxx",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjQwOTk1MjAwLCJleHAiOjE2NDE2MDAwMDB9.yyy",
    "tokenType": "Bearer",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["ROLE_USER"]
  },
  "timestamp": "2024-01-01T12:00:00"
}
```

### 3. 토큰 갱신

**Endpoint**: `POST /api/auth/refresh`

**Request Body**:
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Token refreshed successfully",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.new_token...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.same_refresh_token...",
    "tokenType": "Bearer",
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["ROLE_USER"]
  },
  "timestamp": "2024-01-01T12:30:00"
}
```

### 4. 로그아웃

**Endpoint**: `POST /api/auth/logout`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Logout successful",
  "data": null,
  "timestamp": "2024-01-01T13:00:00"
}
```

### 5. 내 정보 조회

**Endpoint**: `GET /api/users/me`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER"],
    "enabled": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T13:00:00"
}
```

### 6. 모든 사용자 조회 (ADMIN/MODERATOR)

**Endpoint**: `GET /api/users`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Required Role**: ADMIN 또는 MODERATOR

**Response** (200 OK):
```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "username": "johndoe",
      "email": "john@example.com",
      "roles": ["USER"],
      "enabled": true,
      "createdAt": "2024-01-01T10:00:00",
      "updatedAt": "2024-01-01T10:00:00"
    },
    {
      "id": 2,
      "username": "janedoe",
      "email": "jane@example.com",
      "roles": ["ADMIN"],
      "enabled": true,
      "createdAt": "2024-01-01T11:00:00",
      "updatedAt": "2024-01-01T11:00:00"
    }
  ],
  "timestamp": "2024-01-01T13:00:00"
}
```

### 7. 특정 사용자 조회 (ADMIN/MODERATOR)

**Endpoint**: `GET /api/users/{id}`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Required Role**: ADMIN 또는 MODERATOR

**Response** (200 OK):
```json
{
  "success": true,
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER"],
    "enabled": true,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T10:00:00"
  },
  "timestamp": "2024-01-01T13:00:00"
}
```

### 8. 사용자 삭제 (ADMIN)

**Endpoint**: `DELETE /api/users/{id}`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Required Role**: ADMIN

**Response** (200 OK):
```json
{
  "success": true,
  "message": "User deleted successfully",
  "data": null,
  "timestamp": "2024-01-01T13:00:00"
}
```

### 9. 사용자 상태 변경 (ADMIN)

**Endpoint**: `PATCH /api/users/{id}/status?enabled=false`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Query Parameters**:
- `enabled` (boolean): true 또는 false

**Required Role**: ADMIN

**Response** (200 OK):
```json
{
  "success": true,
  "message": "User status updated successfully",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "roles": ["USER"],
    "enabled": false,
    "createdAt": "2024-01-01T10:00:00",
    "updatedAt": "2024-01-01T13:00:00"
  },
  "timestamp": "2024-01-01T13:00:00"
}
```

### 10. 관리자 대시보드 (ADMIN)

**Endpoint**: `GET /api/admin/dashboard`

**Headers**:
```
Authorization: Bearer {access_token}
```

**Required Role**: ADMIN

**Response** (200 OK):
```json
{
  "success": true,
  "message": "Admin dashboard data",
  "data": "Welcome to Admin Dashboard",
  "timestamp": "2024-01-01T13:00:00"
}
```

## ⚠️ 에러 코드

### 400 Bad Request
```json
{
  "success": false,
  "message": "Username is already taken",
  "timestamp": "2024-01-01T13:00:00"
}
```

**발생 케이스**:
- 중복된 사용자명 또는 이메일
- 유효하지 않은 요청 데이터
- 유효성 검사 실패

### 401 Unauthorized
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/users/me"
}
```

**발생 케이스**:
- 토큰이 없거나 유효하지 않음
- 토큰이 만료됨
- 잘못된 사용자명 또는 비밀번호

### 403 Forbidden
```json
{
  "success": false,
  "message": "Access Denied",
  "timestamp": "2024-01-01T13:00:00"
}
```

**발생 케이스**:
- 필요한 권한이 없음
- USER가 ADMIN 전용 API에 접근

### 404 Not Found
```json
{
  "success": false,
  "message": "User not found with id: 999",
  "timestamp": "2024-01-01T13:00:00"
}
```

**발생 케이스**:
- 존재하지 않는 리소스 요청

### 422 Validation Error
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Email should be valid",
    "password": "Password must be between 6 and 100 characters"
  },
  "timestamp": "2024-01-01T13:00:00"
}
```

### 500 Internal Server Error
```json
{
  "success": false,
  "message": "Internal server error: ...",
  "timestamp": "2024-01-01T13:00:00"
}
```

## 🧪 Postman 컬렉션

### 환경 변수 설정

```json
{
  "baseUrl": "http://localhost:8080",
  "accessToken": "",
  "refreshToken": ""
}
```

### 사용 시나리오

#### 시나리오 1: 일반 사용자 플로우

1. **회원가입**
   ```
   POST {{baseUrl}}/api/auth/signup
   Body: { "username": "user1", "email": "user1@test.com", "password": "pass123", "roles": ["USER"] }
   ```

2. **로그인**
   ```
   POST {{baseUrl}}/api/auth/login
   Body: { "username": "user1", "password": "pass123" }
   
   → accessToken과 refreshToken을 환경 변수에 저장
   ```

3. **내 정보 조회**
   ```
   GET {{baseUrl}}/api/users/me
   Headers: Authorization: Bearer {{accessToken}}
   ```

4. **로그아웃**
   ```
   POST {{baseUrl}}/api/auth/logout
   Headers: Authorization: Bearer {{accessToken}}
   ```

#### 시나리오 2: 관리자 플로우

1. **관리자 계정으로 회원가입**
   ```
   POST {{baseUrl}}/api/auth/signup
   Body: { "username": "admin", "email": "admin@test.com", "password": "admin123", "roles": ["ADMIN"] }
   ```

2. **로그인**
   ```
   POST {{baseUrl}}/api/auth/login
   Body: { "username": "admin", "password": "admin123" }
   ```

3. **모든 사용자 조회**
   ```
   GET {{baseUrl}}/api/users
   Headers: Authorization: Bearer {{accessToken}}
   ```

4. **사용자 비활성화**
   ```
   PATCH {{baseUrl}}/api/users/1/status?enabled=false
   Headers: Authorization: Bearer {{accessToken}}
   ```

5. **관리자 대시보드 접근**
   ```
   GET {{baseUrl}}/api/admin/dashboard
   Headers: Authorization: Bearer {{accessToken}}
   ```

## 💡 팁

### 1. 토큰 자동 갱신
Access Token이 만료되기 전에 Refresh Token을 사용하여 새로운 토큰을 발급받으세요.

### 2. 보안 모범 사례
- JWT Secret은 최소 256비트 이상의 강력한 키를 사용하세요
- HTTPS를 통해서만 API를 제공하세요
- 토큰을 localStorage가 아닌 httpOnly 쿠키에 저장하는 것을 고려하세요

### 3. 권한 테스트
다양한 권한 레벨의 계정을 생성하여 접근 제어가 올바르게 동작하는지 확인하세요.

## 🔗 추가 리소스

- [Swagger UI](http://localhost:8080/swagger-ui.html) - 인터랙티브 API 문서
- [README.md](README.md) - 프로젝트 개요 및 설치 가이드


