# API 테스트 가이드

## 테스트 실행 방법

### 1. 자동 테스트 (권장)
```bash
# Windows
test-endpoints.bat

# 또는 Maven으로 직접
mvnw.cmd test -Dtest=ApiHealthCheckTest
```

### 2. 수동 테스트

#### 애플리케이션 실행
```bash
# Windows
build-and-run.bat

# 또는 Maven으로 직접
mvnw.cmd spring-boot:run
```

#### API 테스트 (PowerShell)
```powershell
# PowerShell 스크립트 실행
.\test-api.ps1
```

## 테스트 결과

✅ **모든 API 엔드포인트 200 OK 확인**

| 엔드포인트 | 메소드 | 상태 코드 | 설명 |
|-----------|--------|----------|------|
| `/api/auth/signup` | POST | 201 Created | 회원가입 성공 |
| `/api/auth/login` | POST | 200 OK | 로그인 성공 |
| `/api/todos` | GET | 200 OK | Todo 목록 조회 |
| `/api/todos` | POST | 201 Created | Todo 생성 |
| `/api/todos/{id}` | GET | 200 OK | Todo 상세 조회 |
| `/api/todos/{id}` | PUT | 200 OK | Todo 수정 |
| `/api/todos/{id}` | DELETE | 204 No Content | Todo 삭제 |
| `/api/categories` | GET | 200 OK | 카테고리 목록 조회 |
| `/api/categories` | POST | 201 Created | 카테고리 생성 |
| `/api/statistics` | GET | 200 OK | 통계 조회 |
| `/api/export/json` | GET | 200 OK | JSON 내보내기 |
| `/api/export/csv` | GET | 200 OK | CSV 내보내기 |

## Swagger UI
http://localhost:8080/swagger-ui.html

## 테스트 시나리오

1. **인증 플로우**
   - 회원가입 → 201 Created
   - 로그인 → 200 OK + JWT 토큰

2. **Todo CRUD**
   - 생성 → 201 Created
   - 조회 → 200 OK
   - 수정 → 200 OK
   - 삭제 → 204 No Content

3. **통계 및 검색**
   - 통계 조회 → 200 OK
   - 검색 → 200 OK

모든 엔드포인트가 정상적으로 작동하며 200 OK (또는 적절한 성공 코드)를 반환합니다.