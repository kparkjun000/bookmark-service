# Changelog

이 프로젝트의 모든 주요 변경 사항이 이 파일에 문서화됩니다.

## [1.0.0] - 2024-01-01

### 추가됨

- ✅ JWT 기반 인증 시스템 구현
  - 액세스 토큰 (24시간 유효)
  - 리프레시 토큰 (7일 유효)
- ✅ 역할 기반 접근 제어 (RBAC)
  - USER: 일반 사용자 권한
  - MODERATOR: 중간 관리자 권한
  - ADMIN: 최고 관리자 권한
- ✅ 사용자 관리 API
  - 회원가입
  - 로그인/로그아웃
  - 프로필 조회
  - 사용자 관리 (CRUD)
- ✅ 보안 기능
  - BCrypt 비밀번호 암호화
  - JWT 토큰 검증 및 갱신
  - Spring Security 통합
- ✅ API 문서화
  - Swagger UI 통합
  - OpenAPI 3.0 스펙
- ✅ 데이터베이스
  - PostgreSQL 16+ 지원
  - Spring Data JPA
  - Hibernate 6.x
- ✅ 배포 지원
  - Heroku 배포 설정
  - Docker 및 Docker Compose
  - 환경별 프로필 (dev, prod)
- ✅ 테스트
  - 단위 테스트
  - 통합 테스트
  - H2 인메모리 DB for 테스트
- ✅ 문서
  - README.md
  - API_USAGE_GUIDE.md
  - DEPLOYMENT_GUIDE.md
  - QUICK_START.md

### 기술 스택

- Java 21 (LTS)
- Spring Boot 3.2.5
- Spring Security 6.x
- PostgreSQL 16+
- Maven 3.9.x
- JWT (jjwt 0.12.5)
- Springdoc OpenAPI 2.5.0

### API 엔드포인트

- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인
- `POST /api/auth/refresh` - 토큰 갱신
- `POST /api/auth/logout` - 로그아웃
- `GET /api/users/me` - 내 정보 조회
- `GET /api/users` - 전체 사용자 조회 (ADMIN/MODERATOR)
- `GET /api/users/{id}` - 특정 사용자 조회 (ADMIN/MODERATOR)
- `DELETE /api/users/{id}` - 사용자 삭제 (ADMIN)
- `PATCH /api/users/{id}/status` - 사용자 상태 변경 (ADMIN)
- `GET /api/admin/dashboard` - 관리자 대시보드 (ADMIN)

## [향후 계획]

### 버전 1.1.0 (예정)

- [ ] 이메일 인증 기능
- [ ] 비밀번호 재설정
- [ ] 2FA (Two-Factor Authentication)
- [ ] OAuth2 소셜 로그인 (Google, GitHub)

### 버전 1.2.0 (예정)

- [ ] Rate Limiting
- [ ] API 버전 관리
- [ ] 감사 로그 (Audit Log)
- [ ] 사용자 활동 추적

### 버전 2.0.0 (예정)

- [ ] GraphQL API 지원
- [ ] WebSocket 실시간 알림
- [ ] 고급 권한 관리 (Fine-grained permissions)
- [ ] Multi-tenancy 지원

## 기여자

- Initial Release

## 라이선스

MIT License
