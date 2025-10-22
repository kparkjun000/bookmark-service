# 프로젝트 요약 - Zero Base 13주차 멀티서비스

## 📊 프로젝트 개요

**프로젝트명**: Zero Base 13주차 Heroku 멀티서비스  
**구조**: 모노레포 (Monorepo)  
**서비스 수**: 10개  
**배포 플랫폼**: Heroku  
**데이터베이스**: PostgreSQL

---

## 🏗️ 아키텍처

```
┌─────────────────────────────────────────────────────────────┐
│                    Git Repository (Monorepo)                │
│                     C:\zero-base13week                      │
└─────────────────────────────────────────────────────────────┘
                              │
                 ┌────────────┴────────────┐
                 │                         │
        Git Subtree Push          Git Subtree Push
                 │                         │
     ┌───────────▼──────────┐   ┌─────────▼──────────┐
     │  Heroku App 1        │   │  Heroku App 2      │
     │  zb-todo-api         │   │  zb-blog-api       │
     │  ├─ Web Dyno         │   │  ├─ Web Dyno       │
     │  └─ PostgreSQL DB    │   │  └─ PostgreSQL DB  │
     └──────────────────────┘   └────────────────────┘
                 │                         │
        ┌────────┴────────┐       ┌────────┴────────┐
        │                 │       │                 │
     HTTPS URL       Swagger UI  HTTPS URL    Swagger UI
```

### 핵심 특징

- **독립 배포**: 각 서비스는 독립적으로 배포 및 운영
- **독립 DB**: 각 서비스는 자체 PostgreSQL 데이터베이스 보유
- **독립 URL**: 각 서비스는 고유한 Heroku URL 보유
- **Git Subtree**: 모노레포에서 서비스별로 독립 배포

---

## 📦 서비스 상세

### 1. Todo API (zb-todo-api)

**기능**: 할 일 관리 CRUD  
**Java**: 17  
**폴더**: `01-todo-api`  
**특징**:

- JWT 인증
- CSV 내보내기
- 우선순위 관리

### 2. Blog API (zb-blog-api)

**기능**: 블로그 게시글 및 카테고리 관리  
**Java**: 17  
**폴더**: `02-blog-api`  
**특징**:

- 게시글 CRUD
- 카테고리 관리
- 검색 기능

### 3. Auth System (zb-auth-system)

**기능**: JWT 기반 인증/권한 시스템  
**Java**: 21  
**폴더**: `03-auth-system`  
**특징**:

- 회원가입/로그인
- JWT 토큰 발급
- 역할 기반 권한 관리
- 토큰 갱신

**환경변수**: `JWT_SECRET`

### 4. Bookmark Service (zb-bookmark-service)

**기능**: URL 북마크 및 태그 관리  
**Java**: 21  
**폴더**: `04-bookmark-service`  
**특징**:

- URL 북마크 저장
- 태그 기반 분류
- 메타데이터 자동 추출 (Jsoup)
- 검색 기능

### 5. Shopping API (zb-shopping-api)

**기능**: 쇼핑몰 상품 및 주문 관리  
**Java**: 17  
**폴더**: `05-shopping-api`  
**특징**:

- 상품 관리 (CRUD)
- 주문 처리
- 재고 관리
- 카테고리별 상품 조회

### 6. Memo Backend (zb-memo-backend)

**기능**: 텍스트 메모 및 검색  
**Java**: 21  
**폴더**: `06-memo-backend`  
**특징**:

- 메모 CRUD
- 전문 검색
- 태그 기능
- 즐겨찾기

### 7. Weather Service (zb-weather-service)

**기능**: 외부 API 연동 날씨 정보  
**Java**: 17  
**폴더**: `07-weather-service`  
**특징**:

- OpenWeatherMap API 연동
- 현재 날씨 조회
- 날씨 예보
- 캐싱 기능

**환경변수**: `WEATHER_API_KEY`

### 8. URL Shortener (zb-url-shortener)

**기능**: URL 단축 및 클릭 통계  
**Java**: 17  
**폴더**: `08-url-shortener`  
**특징**:

- URL 단축 생성
- QR 코드 생성
- 클릭 통계
- 리다이렉트 처리
- User-Agent 분석

### 9. Survey System (zb-survey-system)

**기능**: 설문조사 생성 및 응답 수집  
**Java**: 21  
**폴더**: `09-survey-system/survey-api`  
**특징**:

- 설문조사 생성
- 응답 수집
- 통계 분석
- JWT 인증

**환경변수**: `JWT_SECRET`

### 10. File Service (zb-file-service)

**기능**: 파일 업로드 및 이미지 처리  
**Java**: 21  
**폴더**: `10-file-service`  
**특징**:

- 파일 업로드
- 이미지 리사이징 (Thumbnailator)
- 썸네일 생성
- 파일 다운로드

---

## 🛠️ 기술 스택

### Backend Framework

- **Spring Boot**: 3.2.x ~ 3.5.x
- **Spring Data JPA**: ORM
- **Spring Security**: 인증/권한 (Auth, Survey)
- **Spring Web**: RESTful API

### Database

- **Production**: PostgreSQL 16 (Heroku Postgres Essential-0)
- **Development**: H2 Database (in-memory)

### Security

- **JWT**: JSON Web Token (Auth, Survey)
- **jjwt**: 0.12.x

### Documentation

- **SpringDoc OpenAPI**: 2.2.0 ~ 2.6.0
- **Swagger UI**: API 문서 자동 생성

### Build Tool

- **Maven**: 의존성 관리 및 빌드

### Cloud Platform

- **Heroku**: PaaS 배포
- **Heroku Postgres**: 관리형 PostgreSQL
- **Heroku Dyno**: 컨테이너 실행 환경

### Additional Libraries

- **Lombok**: 코드 간소화
- **Bean Validation**: 입력 검증
- **Thumbnailator**: 이미지 처리 (File Service)
- **ZXing**: QR 코드 생성 (URL Shortener)
- **Jsoup**: HTML 파싱 (Bookmark Service)
- **WebFlux**: 외부 API 호출 (Weather Service)

---

## 📁 프로젝트 구조

```
zero-base13week/
├── 01-todo-api/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 17)
├── 02-blog-api/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 17)
├── 03-auth-system/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 21)
├── 04-bookmark-service/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 21)
├── 05-shopping-api/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 17)
├── 06-memo-backend/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 21)
├── 07-weather-service/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 17)
├── 08-url-shortener/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 17)
├── 09-survey-system/
│   └── survey-api/
│       ├── src/
│       ├── pom.xml
│       ├── Procfile
│       └── system.properties (java 21)
├── 10-file-service/
│   ├── src/
│   ├── pom.xml
│   ├── Procfile
│   └── system.properties (java 21)
├── README.md
├── HEROKU_MULTI_SERVICE_GUIDE.md
├── QUICK_START.md
├── DEPLOYMENT_CHECKLIST.md
├── service-commands.md
├── PROJECT_SUMMARY.md (이 파일)
├── heroku-multi-service.json
├── deploy-all-services.ps1
├── deploy-single-service.ps1
├── create-all-apps.ps1
├── services-status.ps1
└── .gitignore
```

---

## 🚀 배포 프로세스

### 1. 준비

```powershell
heroku login
cd C:\zero-base13week
git init
git add .
git commit -m "Initial commit"
```

### 2. 앱 생성

```powershell
.\create-all-apps.ps1
```

- 10개 Heroku 앱 생성
- PostgreSQL 애드온 추가
- Java 버전 설정
- 환경변수 설정

### 3. 배포

```powershell
.\deploy-all-services.ps1
```

- Git Subtree를 사용해 각 폴더를 독립 배포
- 서비스별로 빌드 및 배포

### 4. 확인

```powershell
.\services-status.ps1
```

- 모든 서비스 상태 확인
- URL 및 Swagger UI 접속 테스트

---

## 🔑 환경변수

### 필수 환경변수

| 서비스          | 환경변수          | 용도                  |
| --------------- | ----------------- | --------------------- |
| Auth System     | `JWT_SECRET`      | JWT 토큰 서명 키      |
| Survey System   | `JWT_SECRET`      | JWT 토큰 서명 키      |
| Weather Service | `WEATHER_API_KEY` | OpenWeatherMap API 키 |

### 자동 설정 환경변수

- `DATABASE_URL`: PostgreSQL 연결 정보 (Heroku 자동)
- `PORT`: 서비스 포트 (Heroku 자동)
- `JAVA_VERSION`: Java 런타임 버전 (배포 스크립트)

---

## 📊 리소스 사용

### Heroku Dyno

- **타입**: Eco / Basic
- **개수**: 각 서비스 1개 (총 10개)

### PostgreSQL

- **플랜**: Essential-0
- **개수**: 각 서비스 1개 (총 10개)

### 예상 비용

- Eco Dyno: $5/월 (1000시간 공유)
- Essential-0 PostgreSQL: $5/월 × 10 = $50/월
- **총 예상**: 약 $55/월

---

## 🔗 접속 URL

### Production URLs

| #   | 서비스           | URL                                       |
| --- | ---------------- | ----------------------------------------- |
| 1   | Todo API         | https://zb-todo-api.herokuapp.com         |
| 2   | Blog API         | https://zb-blog-api.herokuapp.com         |
| 3   | Auth System      | https://zb-auth-system.herokuapp.com      |
| 4   | Bookmark Service | https://zb-bookmark-service.herokuapp.com |
| 5   | Shopping API     | https://zb-shopping-api.herokuapp.com     |
| 6   | Memo Backend     | https://zb-memo-backend.herokuapp.com     |
| 7   | Weather Service  | https://zb-weather-service.herokuapp.com  |
| 8   | URL Shortener    | https://zb-url-shortener.herokuapp.com    |
| 9   | Survey System    | https://zb-survey-system.herokuapp.com    |
| 10  | File Service     | https://zb-file-service.herokuapp.com     |

### Swagger UI

각 서비스 URL에 `/swagger-ui/index.html` 추가

---

## 📈 모니터링

### 로그 확인

```powershell
heroku logs --tail --app <app-name>
```

### 상태 확인

```powershell
heroku ps --app <app-name>
heroku pg:info --app <app-name>
```

### 전체 서비스 상태

```powershell
.\services-status.ps1
```

---

## 🔄 업데이트 프로세스

### 코드 변경 후 재배포

```powershell
git add .
git commit -m "Update service"
.\deploy-single-service.ps1  # 개별 서비스
# 또는
.\deploy-all-services.ps1    # 전체 서비스
```

---

## 📚 문서

### 주요 문서

1. **README.md** - 프로젝트 소개 및 빠른 시작
2. **HEROKU_MULTI_SERVICE_GUIDE.md** - 상세 배포 가이드
3. **QUICK_START.md** - 5분 안에 배포하기
4. **DEPLOYMENT_CHECKLIST.md** - 배포 체크리스트
5. **service-commands.md** - 서비스별 명령어 참조
6. **PROJECT_SUMMARY.md** - 프로젝트 요약 (이 파일)

---

## 🎯 성과

### 달성 목표

✅ 10개 독립 서비스 개발 완료  
✅ 멀티서비스 아키텍처 구현  
✅ Heroku 배포 자동화  
✅ 완전한 문서화  
✅ API 문서 자동 생성 (Swagger)  
✅ PostgreSQL 데이터베이스 연동  
✅ JWT 인증 시스템 구현  
✅ 외부 API 연동 (날씨)

### 기술적 성취

- 모노레포에서 멀티서비스 관리
- Git Subtree를 활용한 독립 배포
- 서비스별 독립적인 스케일링 가능
- RESTful API 설계 및 구현
- Spring Boot 3.x 활용

---

## 🔮 향후 개선 방향

### 단기 (1-2주)

- [ ] 통합 로깅 시스템 (ELK Stack)
- [ ] API Gateway 도입 (Spring Cloud Gateway)
- [ ] 서비스 간 통신 최적화

### 중기 (1-2개월)

- [ ] CI/CD 파이프라인 구축 (GitHub Actions)
- [ ] 컨테이너화 (Docker)
- [ ] 성능 모니터링 (New Relic)

### 장기 (3-6개월)

- [ ] 마이크로서비스 전환
- [ ] Kubernetes 마이그레이션
- [ ] 서비스 메시 도입 (Istio)

---

## 👥 팀

**프로젝트**: Zero Base 백엔드 스쿨 13주차  
**기간**: 2024-2025  
**인원**: 1명

---

## 📄 라이선스

Zero Base 백엔드 스쿨 과제 프로젝트

---

**최종 업데이트**: 2025년 10월 22일  
**버전**: 1.0.0  
**상태**: ✅ 배포 완료
