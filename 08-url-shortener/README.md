# URL Shortener & Analytics Service

URL 단축 및 클릭 통계 RESTful API 서비스입니다.

## 기능

- URL 단축 (해시 알고리즘 사용)
- 리디렉션
- 클릭 통계 및 분석
- QR 코드 생성
- Heroku 단일 서비스 배포

## 기술 스택

- Java 17 (LTS)
- Spring Boot 3.2.x
- PostgreSQL 16+
- Spring Data JPA
- Maven 3.9.x
- Swagger/OpenAPI 3.0

## 프로젝트 구조

```
├── src/main/java/com/urlshortener/
│   ├── controller/           # REST 컨트롤러
│   ├── service/             # 비즈니스 로직
│   ├── repository/          # 데이터 접근
│   ├── entity/              # JPA 엔티티
│   ├── dto/                 # 데이터 전송 객체
│   ├── exception/           # 예외 처리
│   └── config/             # 설정
├── src/main/resources/
│   ├── application.properties
│   └── application-test.properties
├── pom.xml
├── Procfile
└── README.md
```

## 빠른 시작

### 로컬 개발

1. Docker Compose로 실행:
```bash
docker-compose up -d
```

2. 서비스 접속:
- URL Shortener & Analytics: http://localhost:8080

### Heroku 배포

자세한 배포 방법은 `HEROKU_DEPLOYMENT.md`를 참조하세요.

## API 문서

Swagger UI: `/swagger-ui/index.html`

## 주요 엔드포인트

### URL 단축
- `POST /api/urls` - URL 단축 생성
- `GET /api/urls` - 모든 URL 조회
- `GET /api/urls/{shortCode}` - URL 정보 조회
- `DELETE /api/urls/{shortCode}` - URL 삭제
- `GET /{shortCode}` - 리디렉션
- `GET /api/urls/{shortCode}/qr` - QR 코드 생성

### 분석
- `GET /api/analytics/{shortCode}` - 전체 통계
- `GET /api/analytics/{shortCode}/timeline` - 시간대별 통계
- `GET /api/analytics/{shortCode}/browsers` - 브라우저별 통계
- `GET /api/analytics/{shortCode}/os` - OS별 통계
- `GET /api/analytics/{shortCode}/devices` - 디바이스별 통계
- `GET /api/analytics/{shortCode}/locations` - 위치별 통계

## 라이선스

MIT License