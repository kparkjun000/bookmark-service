# 프로젝트 요약

## 프로젝트 개요

**File Upload and Image Processing Service** - Spring Boot 기반의 파일 업로드 및 이미지 처리 RESTful API 서비스

## 기술 스택

- **언어**: Java 21 (LTS)
- **프레임워크**: Spring Boot 3.2.5
- **빌드 도구**: Maven 3.9.6
- **데이터베이스**: PostgreSQL 16+
- **ORM**: Spring Data JPA + Hibernate 6.x
- **이미지 처리**: Thumbnailator 0.4.20
- **배포**: Heroku

## 주요 기능

### 1. 파일 업로드

- ✅ 단일 파일 업로드
- ✅ 다중 파일 업로드
- ✅ 파일 크기 제한 (최대 10MB)
- ✅ 파일 형식 검증 (jpg, jpeg, png, gif, webp, bmp)
- ✅ UUID 기반 파일 ID 생성

### 2. 이미지 처리

- ✅ 자동 썸네일 생성 (200x200px)
- ✅ 이미지 리사이징 (최대 1920x1080px)
- ✅ 이미지 해상도 자동 감지
- ✅ 비율 유지 리사이징

### 3. 파일 관리

- ✅ 파일 다운로드
- ✅ 파일 미리보기
- ✅ 파일 메타데이터 조회
- ✅ 페이지네이션 지원 파일 목록
- ✅ 파일 검색 (키워드)
- ✅ 소프트 삭제

### 4. API 기능

- ✅ RESTful API 디자인
- ✅ 전역 예외 처리
- ✅ 표준화된 응답 형식
- ✅ CORS 지원
- ✅ Health Check 엔드포인트

## 프로젝트 구조

```
10-file-service/
├── src/
│   ├── main/
│   │   ├── java/com/fileservice/
│   │   │   ├── config/              # 설정 클래스
│   │   │   ├── controller/          # REST 컨트롤러
│   │   │   ├── dto/                 # 데이터 전송 객체
│   │   │   ├── entity/              # JPA 엔티티
│   │   │   ├── exception/           # 예외 처리
│   │   │   ├── repository/          # 데이터 접근 계층
│   │   │   ├── service/             # 비즈니스 로직
│   │   │   └── FileUploadServiceApplication.java
│   │   └── resources/
│   │       ├── application.yml      # 기본 설정
│   │       ├── application-dev.yml  # 개발 환경
│   │       ├── application-prod.yml # 프로덕션 환경
│   │       └── schema.sql           # DB 스키마
│   └── test/                        # 테스트 코드
├── target/                          # 빌드 출력
├── .mvn/                            # Maven Wrapper
├── docker-compose.yml               # Docker 구성
├── Dockerfile                       # Docker 이미지 빌드
├── Procfile                         # Heroku 배포
├── system.properties                # Java 버전 지정
├── pom.xml                          # Maven 의존성
├── README.md                        # 전체 문서
├── QUICKSTART.md                    # 빠른 시작
├── API_EXAMPLES.md                  # API 예제
├── DEPLOYMENT.md                    # 배포 가이드
└── CONTRIBUTING.md                  # 기여 가이드
```

## API 엔드포인트

### 파일 업로드

| Method | Endpoint                     | 설명             |
| ------ | ---------------------------- | ---------------- |
| POST   | `/api/files/upload`          | 단일 파일 업로드 |
| POST   | `/api/files/upload-multiple` | 다중 파일 업로드 |

### 파일 조회

| Method | Endpoint                        | 설명                          |
| ------ | ------------------------------- | ----------------------------- |
| GET    | `/api/files`                    | 파일 목록 조회 (페이지네이션) |
| GET    | `/api/files/{fileId}`           | 파일 메타데이터 조회          |
| GET    | `/api/files/{fileId}/download`  | 파일 다운로드                 |
| GET    | `/api/files/{fileId}/view`      | 파일 미리보기                 |
| GET    | `/api/files/{fileId}/thumbnail` | 썸네일 조회                   |
| GET    | `/api/files/search`             | 파일 검색                     |

### 파일 삭제

| Method | Endpoint              | 설명      |
| ------ | --------------------- | --------- |
| DELETE | `/api/files/{fileId}` | 파일 삭제 |

### 시스템

| Method | Endpoint      | 설명      |
| ------ | ------------- | --------- |
| GET    | `/api/health` | 헬스 체크 |
| GET    | `/api/`       | API 정보  |

## 데이터베이스 스키마

### file_metadata 테이블

```sql
- id (BIGSERIAL PRIMARY KEY)
- file_id (VARCHAR UNIQUE)
- original_file_name (VARCHAR)
- stored_file_name (VARCHAR)
- content_type (VARCHAR)
- file_size (BIGINT)
- file_extension (VARCHAR)
- file_path (VARCHAR)
- width (INTEGER)
- height (INTEGER)
- has_thumbnail (BOOLEAN)
- thumbnail_path (VARCHAR)
- thumbnail_file_name (VARCHAR)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
- uploaded_by (VARCHAR)
- description (VARCHAR)
- status (VARCHAR)
```

## 환경 설정

### 개발 환경 (dev)

- 로컬 PostgreSQL 사용
- DDL auto: update
- 상세한 로깅
- 로컬 파일 시스템 저장

### 프로덕션 환경 (prod)

- Heroku PostgreSQL
- DDL auto: validate
- 최소 로깅
- 임시 파일 시스템 (/tmp)

### 테스트 환경 (test)

- 테스트 데이터베이스
- DDL auto: create-drop
- 별도 업로드 디렉토리

## 보안 기능

1. **파일 검증**

   - 파일 확장자 화이트리스트
   - 파일 크기 제한
   - 경로 순회 공격 방지

2. **데이터 보호**

   - UUID 기반 파일 ID
   - 소프트 삭제 (물리적 삭제 + 상태 변경)

3. **에러 처리**
   - 전역 예외 핸들러
   - 표준화된 에러 응답
   - 로깅

## 성능 최적화

1. **데이터베이스**

   - 인덱스 (file_id, status, created_at)
   - 페이지네이션
   - 읽기 전용 트랜잭션

2. **파일 처리**

   - 스트리밍 전송 (Resource)
   - 비동기 이미지 처리 가능

3. **캐싱**
   - Hibernate 2차 캐시 지원
   - HTTP 캐시 헤더 설정 가능

## 의존성

### 주요 의존성

```xml
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Validation
- PostgreSQL Driver
- Thumbnailator (이미지 처리)
- Apache Commons IO
- Lombok
- Spring Boot Actuator
```

## 빠른 시작

### 로컬 실행

```bash
# 빌드
mvn clean install

# 실행
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Docker 실행

```bash
docker-compose up -d
```

### Heroku 배포

```bash
heroku create
heroku addons:create heroku-postgresql:essential-0
git push heroku main
```

## 테스트

### 파일 업로드 테스트

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@test-image.jpg" \
  -F "generateThumbnail=true"
```

### 헬스 체크

```bash
curl http://localhost:8080/api/health
```

## 확장 가능성

### 향후 개선 가능 사항

1. **클라우드 스토리지 통합**

   - AWS S3
   - Google Cloud Storage
   - Azure Blob Storage

2. **추가 이미지 처리**

   - 워터마크
   - 필터 적용
   - 포맷 변환

3. **고급 기능**

   - 파일 버전 관리
   - 파일 공유 링크
   - 만료 기능
   - 압축 파일 지원

4. **보안 강화**

   - JWT 인증
   - 역할 기반 접근 제어
   - 파일 암호화

5. **성능 개선**
   - Redis 캐싱
   - CDN 통합
   - 비동기 처리 큐

## 문서

- **README.md**: 전체 프로젝트 문서
- **QUICKSTART.md**: 빠른 시작 가이드
- **API_EXAMPLES.md**: API 사용 예제
- **DEPLOYMENT.md**: Heroku 배포 가이드
- **CONTRIBUTING.md**: 기여 가이드

## 라이센스

MIT License

## 지원

- GitHub Issues: 버그 리포트 및 기능 제안
- Documentation: 프로젝트 내 마크다운 파일들

---

## 개발 체크리스트

### 완료된 항목 ✅

- [x] Maven 프로젝트 구조
- [x] Spring Boot 애플리케이션 설정
- [x] PostgreSQL 데이터베이스 연동
- [x] JPA Entity 및 Repository
- [x] 파일 업로드 서비스
- [x] 이미지 처리 서비스
- [x] REST API 컨트롤러
- [x] 전역 예외 처리
- [x] DTO 및 응답 표준화
- [x] Docker 지원
- [x] Heroku 배포 설정
- [x] API 문서화
- [x] 빠른 시작 가이드
- [x] 배포 가이드

### 선택적 개선 사항

- [ ] JWT 인증 추가
- [ ] S3 통합
- [ ] Redis 캐싱
- [ ] API 스웨거 문서
- [ ] 단위 테스트 추가
- [ ] 통합 테스트 추가
- [ ] CI/CD 파이프라인

---

**프로젝트 상태**: ✅ 프로덕션 준비 완료

이 서비스는 완전히 기능하는 파일 업로드 및 이미지 처리 API로, 로컬 개발 환경과 Heroku 프로덕션 환경 모두에서 즉시 사용할 수 있습니다.
