# Weather Service API

RESTful API 서비스로 외부 날씨 API(OpenWeatherMap)를 연동하여 실시간 날씨 정보와 예보 데이터를 제공합니다.

## 📋 목차

- [기능](#기능)
- [기술 스택](#기술-스택)
- [프로젝트 구조](#프로젝트-구조)
- [시작하기](#시작하기)
- [API 문서](#api-문서)
- [환경 변수 설정](#환경-변수-설정)
- [Heroku 배포](#heroku-배포)
- [API 엔드포인트](#api-엔드포인트)
- [예제 요청](#예제-요청)

## ✨ 기능

- 🌤️ **현재 날씨 조회**: 도시별 실시간 날씨 정보 제공
- 📅 **날씨 예보**: 최대 5일간의 날씨 예보 (3시간 간격)
- 🗺️ **위치 관리**: 도시 및 국가별 위치 정보 저장
- 💾 **데이터 저장**: PostgreSQL을 사용한 날씨 데이터 히스토리 저장
- 🚀 **캐싱**: 외부 API 호출 최적화를 위한 캐시 기능
- 📖 **API 문서**: Swagger UI를 통한 인터랙티브 API 문서

## 🛠 기술 스택

- **Java**: 17 (LTS)
- **Framework**: Spring Boot 3.2.5
- **Database**: PostgreSQL 16+
- **ORM**: Spring Data JPA
- **Build Tool**: Maven 3.9.x
- **API Documentation**: SpringDoc OpenAPI 3 (Swagger)
- **External API**: OpenWeatherMap API
- **Deployment**: Heroku
- **Cache**: Spring Cache

## 📁 프로젝트 구조

```
weather-service/
├── src/
│   ├── main/
│   │   ├── java/com/weather/
│   │   │   ├── config/           # 설정 클래스
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── WebClientConfig.java
│   │   │   ├── controller/       # REST 컨트롤러
│   │   │   │   ├── WeatherController.java
│   │   │   │   └── HealthController.java
│   │   │   ├── dto/              # 데이터 전송 객체
│   │   │   │   ├── external/     # 외부 API DTO
│   │   │   │   ├── WeatherResponse.java
│   │   │   │   ├── ForecastResponse.java
│   │   │   │   ├── LocationRequest.java
│   │   │   │   └── ApiResponse.java
│   │   │   ├── entity/           # JPA 엔티티
│   │   │   │   ├── Location.java
│   │   │   │   ├── WeatherData.java
│   │   │   │   └── Forecast.java
│   │   │   ├── exception/        # 예외 처리
│   │   │   │   ├── ExternalApiException.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── repository/       # JPA 리포지토리
│   │   │   │   ├── LocationRepository.java
│   │   │   │   ├── WeatherDataRepository.java
│   │   │   │   └── ForecastRepository.java
│   │   │   ├── service/          # 비즈니스 로직
│   │   │   │   ├── WeatherService.java
│   │   │   │   └── OpenWeatherMapService.java
│   │   │   └── WeatherServiceApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
├── Procfile                      # Heroku 배포 설정
├── system.properties             # Java 버전 지정
├── pom.xml                       # Maven 의존성 설정
└── README.md
```

## 🚀 시작하기

### 사전 요구사항

- Java 17 이상
- Maven 3.9.x
- PostgreSQL 16+
- OpenWeatherMap API Key ([무료 가입](https://openweathermap.org/api))

### 로컬 실행

1. **프로젝트 클론**

```bash
git clone <repository-url>
cd weather-service
```

2. **PostgreSQL 데이터베이스 생성**

```sql
CREATE DATABASE weatherdb;
```

3. **환경 변수 설정**

`src/main/resources/application.yml` 파일에서 아래 항목 수정:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/weatherdb
    username: your_username
    password: your_password

weather:
  api:
    openweathermap:
      key: your_openweathermap_api_key
```

4. **애플리케이션 빌드 및 실행**

```bash
mvn clean package
mvn spring-boot:run
```

또는

```bash
java -jar target/weather-service-1.0.0.jar
```

5. **애플리케이션 확인**

- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html
- API Docs: http://localhost:8080/api-docs

## 📖 API 문서

Swagger UI를 통해 모든 API 엔드포인트를 테스트할 수 있습니다.

**Swagger UI URL**: `http://localhost:8080/swagger-ui.html`

## 🔧 환경 변수 설정

### 로컬 개발

`application.yml` 또는 환경 변수로 설정:

| 변수명                | 설명                  | 기본값                                       |
| --------------------- | --------------------- | -------------------------------------------- |
| `DATABASE_URL`        | PostgreSQL 연결 URL   | `jdbc:postgresql://localhost:5432/weatherdb` |
| `DATABASE_USERNAME`   | DB 사용자명           | `postgres`                                   |
| `DATABASE_PASSWORD`   | DB 비밀번호           | `postgres`                                   |
| `OPENWEATHER_API_KEY` | OpenWeatherMap API 키 | (필수)                                       |
| `SPRING_PROFILE`      | 활성 프로필           | `dev`                                        |
| `PORT`                | 서버 포트             | `8080`                                       |

### Heroku 환경

Heroku Config Vars로 설정:

```bash
heroku config:set OPENWEATHER_API_KEY=your_api_key
heroku config:set SPRING_PROFILE=prod
```

## 🌐 Heroku 배포

### 1. Heroku CLI 설치

```bash
# macOS
brew tap heroku/brew && brew install heroku

# Windows
# Download from: https://devcenter.heroku.com/articles/heroku-cli
```

### 2. Heroku 앱 생성

```bash
heroku login
heroku create your-app-name
```

### 3. PostgreSQL 애드온 추가

```bash
heroku addons:create heroku-postgresql:essential-0
```

### 4. 환경 변수 설정

```bash
heroku config:set OPENWEATHER_API_KEY=your_openweather_api_key
heroku config:set SPRING_PROFILE=prod
heroku config:set JAVA_OPTS="-Xmx300m -Xss512k -XX:CICompilerCount=2"
```

### 5. 배포

```bash
git add .
git commit -m "Initial commit"
git push heroku main
```

### 6. 로그 확인

```bash
heroku logs --tail
```

### 7. 앱 열기

```bash
heroku open
```

Swagger URL: `https://your-app-name.herokuapp.com/swagger-ui.html`

## 📡 API 엔드포인트

### Health Check

- **GET** `/api/v1/health` - 서비스 상태 확인
- **GET** `/api/v1/` - API 정보

### 날씨 조회

- **GET** `/api/v1/weather/current` - 현재 날씨 조회 (GET)
  - Query Parameters: `city` (required), `country` (optional)
- **POST** `/api/v1/weather/current` - 현재 날씨 조회 (POST)
  - Request Body: `LocationRequest`

### 날씨 예보

- **GET** `/api/v1/weather/forecast` - 날씨 예보 조회 (GET)
  - Query Parameters: `city` (required), `country` (optional), `days` (1-5, default: 5)
- **POST** `/api/v1/weather/forecast` - 날씨 예보 조회 (POST)
  - Request Body: `LocationRequest`
  - Query Parameter: `days` (1-5, default: 5)

### 날씨 히스토리

- **GET** `/api/v1/weather/history/{locationId}` - 저장된 날씨 데이터 조회
  - Path Variable: `locationId`

## 📝 예제 요청

### cURL 예제

#### 현재 날씨 조회 (GET)

```bash
curl -X GET "http://localhost:8080/api/v1/weather/current?city=Seoul&country=KR"
```

#### 현재 날씨 조회 (POST)

```bash
curl -X POST "http://localhost:8080/api/v1/weather/current" \
  -H "Content-Type: application/json" \
  -d '{
    "city": "Seoul",
    "country": "KR"
  }'
```

#### 날씨 예보 조회

```bash
curl -X GET "http://localhost:8080/api/v1/weather/forecast?city=Seoul&country=KR&days=5"
```

### 응답 예제

```json
{
  "success": true,
  "message": "Weather data retrieved successfully",
  "data": {
    "id": 1,
    "location": {
      "id": 1,
      "city": "Seoul",
      "country": "KR",
      "countryCode": "KR",
      "latitude": 37.5665,
      "longitude": 126.978
    },
    "timestamp": "2024-05-01T12:00:00",
    "temperature": 18.5,
    "feelsLike": 17.8,
    "tempMin": 15.0,
    "tempMax": 20.0,
    "pressure": 1013,
    "humidity": 65,
    "windSpeed": 3.5,
    "windDirection": 180,
    "cloudiness": 40,
    "weatherMain": "Clouds",
    "weatherDescription": "scattered clouds",
    "weatherIcon": "03d",
    "visibility": 10000.0,
    "rain1h": null,
    "snow1h": null
  },
  "timestamp": "2024-05-01T12:00:00"
}
```

## 🔍 데이터베이스 스키마

### Locations Table

```sql
CREATE TABLE locations (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    country_code VARCHAR(2),
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    timezone_offset INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    UNIQUE(city, country)
);
```

### Weather Data Table

```sql
CREATE TABLE weather_data (
    id BIGSERIAL PRIMARY KEY,
    location_id BIGINT NOT NULL REFERENCES locations(id),
    timestamp TIMESTAMP NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    feels_like DOUBLE PRECISION,
    temp_min DOUBLE PRECISION,
    temp_max DOUBLE PRECISION,
    pressure INTEGER,
    humidity INTEGER,
    wind_speed DOUBLE PRECISION,
    wind_direction INTEGER,
    cloudiness INTEGER,
    weather_main VARCHAR(255),
    weather_description VARCHAR(255),
    weather_icon VARCHAR(10),
    visibility DOUBLE PRECISION,
    rain_1h DOUBLE PRECISION,
    snow_1h DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL
);
```

### Forecasts Table

```sql
CREATE TABLE forecasts (
    id BIGSERIAL PRIMARY KEY,
    location_id BIGINT NOT NULL REFERENCES locations(id),
    forecast_timestamp TIMESTAMP NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    feels_like DOUBLE PRECISION,
    temp_min DOUBLE PRECISION,
    temp_max DOUBLE PRECISION,
    pressure INTEGER,
    humidity INTEGER,
    wind_speed DOUBLE PRECISION,
    wind_direction INTEGER,
    cloudiness INTEGER,
    weather_main VARCHAR(255),
    weather_description VARCHAR(255),
    weather_icon VARCHAR(10),
    visibility DOUBLE PRECISION,
    pop DOUBLE PRECISION,
    rain_3h DOUBLE PRECISION,
    snow_3h DOUBLE PRECISION,
    created_at TIMESTAMP NOT NULL
);
```

## 🐛 트러블슈팅

### OpenWeatherMap API 키 오류

- API 키가 올바르게 설정되었는지 확인
- 무료 API 키는 활성화까지 몇 시간이 걸릴 수 있음

### 데이터베이스 연결 오류

- PostgreSQL이 실행 중인지 확인
- 데이터베이스 URL, 사용자명, 비밀번호 확인

### Heroku 배포 오류

- Procfile이 프로젝트 루트에 있는지 확인
- Java 버전이 올바르게 설정되었는지 확인
- 빌드 로그 확인: `heroku logs --tail`

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 있습니다.

## 👥 기여

Pull Request는 언제나 환영합니다!

## 📞 문의

문제가 있거나 질문이 있으시면 이슈를 생성해주세요.
