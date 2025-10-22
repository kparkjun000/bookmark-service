# Survey API

설문조사 생성 및 응답 수집을 위한 RESTful API 서버입니다.

## 기술 스택

- **Java 21**
- **Spring Boot 3.5.4**
- **Spring Security** (JWT 인증)
- **Spring Data JPA**
- **PostgreSQL**
- **Swagger/OpenAPI 3** (API 문서화)
- **Maven**

## 주요 기능

### 1. 사용자 인증

- 회원가입
- 로그인 (JWT 토큰 발급)

### 2. 설문조사 관리

- 설문조사 생성/수정/삭제
- 설문조사 목록 조회
- 내가 생성한 설문조사 조회
- 활성 설문조사 조회

### 3. 질문 관리

- 질문 추가/수정/삭제
- 질문 목록 조회
- 질문 타입: 단일 선택, 다중 선택, 텍스트, 평점

### 4. 응답 관리

- 설문 응답 제출
- 응답 조회 (생성자/응답자)
- 응답 삭제

## 시작하기

### 사전 요구사항

- Java 21 이상
- Maven 3.6 이상
- PostgreSQL 데이터베이스

### 데이터베이스 설정

PostgreSQL에 데이터베이스를 생성합니다:

```sql
CREATE DATABASE surveydb;
```

### 환경 변수 설정

다음 환경 변수를 설정하거나 `application.properties` 파일을 수정합니다:

```properties
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/surveydb
DB_USERNAME=postgres
DB_PASSWORD=your_password

# JWT
JWT_SECRET=your-256-bit-secret-key-change-this-in-production-environment-please

# Server
PORT=8080
```

### 애플리케이션 실행

```bash
cd survey-api
mvn clean install
mvn spring-boot:run
```

애플리케이션이 시작되면 `http://localhost:8080`에서 접근할 수 있습니다.

## API 문서

Swagger UI를 통해 API 문서를 확인할 수 있습니다:

```
http://localhost:8080/swagger-ui.html
```

## API 엔드포인트

### 인증 API (`/api/auth`)

| Method | Endpoint           | Description |
| ------ | ------------------ | ----------- |
| POST   | `/api/auth/signup` | 회원가입    |
| POST   | `/api/auth/login`  | 로그인      |

### 설문조사 API (`/api/surveys`)

| Method | Endpoint              | Description        | 인증 필요 |
| ------ | --------------------- | ------------------ | --------- |
| POST   | `/api/surveys`        | 설문조사 생성      | ✅        |
| GET    | `/api/surveys`        | 모든 설문조사 조회 | ✅        |
| GET    | `/api/surveys/{id}`   | 설문조사 상세 조회 | ✅        |
| GET    | `/api/surveys/my`     | 내 설문조사 조회   | ✅        |
| GET    | `/api/surveys/active` | 활성 설문조사 조회 | ✅        |
| PUT    | `/api/surveys/{id}`   | 설문조사 수정      | ✅        |
| DELETE | `/api/surveys/{id}`   | 설문조사 삭제      | ✅        |

### 질문 API (`/api/surveys/{surveyId}/questions`)

| Method | Endpoint                              | Description    | 인증 필요 |
| ------ | ------------------------------------- | -------------- | --------- |
| POST   | `/api/surveys/{surveyId}/questions`   | 질문 추가      | ✅        |
| GET    | `/api/surveys/{surveyId}/questions`   | 질문 목록 조회 | ✅        |
| PUT    | `/api/surveys/questions/{questionId}` | 질문 수정      | ✅        |
| DELETE | `/api/surveys/questions/{questionId}` | 질문 삭제      | ✅        |

### 응답 API (`/api/responses`)

| Method | Endpoint                           | Description           | 인증 필요 |
| ------ | ---------------------------------- | --------------------- | --------- |
| POST   | `/api/responses`                   | 응답 제출             | ✅        |
| GET    | `/api/responses/{id}`              | 응답 상세 조회        | ✅        |
| GET    | `/api/responses/survey/{surveyId}` | 설문의 모든 응답 조회 | ✅        |
| GET    | `/api/responses/my`                | 내 응답 조회          | ✅        |
| DELETE | `/api/responses/{id}`              | 응답 삭제             | ✅        |

## 인증

이 API는 JWT(JSON Web Token) 기반 인증을 사용합니다.

### 인증 플로우

1. 회원가입 또는 로그인으로 JWT 토큰을 받습니다.
2. 이후 요청의 헤더에 토큰을 포함시킵니다:
   ```
   Authorization: Bearer {your-jwt-token}
   ```

### 예시

```bash
# 1. 회원가입
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'

# 2. 로그인
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'

# 3. 토큰을 사용하여 설문조사 생성
curl -X POST http://localhost:8080/api/surveys \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {your-token}" \
  -d '{
    "title": "고객 만족도 조사",
    "description": "서비스 개선을 위한 설문조사입니다.",
    "questions": [
      {
        "questionText": "서비스에 만족하시나요?",
        "type": "SINGLE_CHOICE",
        "orderIndex": 0,
        "required": true,
        "options": ["매우 만족", "만족", "보통", "불만족", "매우 불만족"]
      }
    ]
  }'
```

## 데이터 모델

### User (사용자)

- id: Long
- username: String (unique)
- email: String (unique)
- password: String (암호화됨)
- role: Enum (USER, ADMIN)

### Survey (설문조사)

- id: Long
- title: String
- description: String
- creator: User
- active: Boolean
- startDate: LocalDateTime
- endDate: LocalDateTime
- questions: List<Question>

### Question (질문)

- id: Long
- survey: Survey
- questionText: String
- type: Enum (SINGLE_CHOICE, MULTIPLE_CHOICE, TEXT, RATING)
- orderIndex: Integer
- required: Boolean
- options: List<String>

### Response (응답)

- id: Long
- survey: Survey
- respondent: User (nullable)
- answers: List<Answer>
- submittedAt: LocalDateTime

### Answer (답변)

- id: Long
- response: Response
- question: Question
- values: List<String>

## 프로젝트 구조

```
survey-api/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/survey/api/
│       │       ├── config/           # 설정 클래스
│       │       ├── controller/       # REST 컨트롤러
│       │       ├── dto/              # 데이터 전송 객체
│       │       ├── entity/           # JPA 엔티티
│       │       ├── exception/        # 예외 처리
│       │       ├── repository/       # JPA 리포지토리
│       │       ├── security/         # 보안 관련
│       │       ├── service/          # 비즈니스 로직
│       │       └── SurveyApiApplication.java
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## 보안 고려사항

- 모든 비밀번호는 BCrypt로 암호화됩니다.
- JWT 토큰은 24시간 후 만료됩니다.
- CSRF 보호는 비활성화되어 있습니다 (Stateless API).
- 프로덕션 환경에서는 반드시 `JWT_SECRET`을 변경하세요.

## 개발 참고사항

### 테스트

```bash
mvn test
```

### 빌드

```bash
mvn clean package
```

빌드된 JAR 파일은 `target/` 디렉토리에 생성됩니다.

### 실행

```bash
java -jar target/survey-api-1.0.0.jar
```

## Heroku 배포

이 프로젝트는 Heroku에 배포하도록 설정되어 있습니다.

### 배포 준비

다음 파일들이 이미 설정되어 있습니다:

- `Procfile` - Heroku 실행 명령
- `system.properties` - Java 21 버전 지정
- `application.properties` - Heroku 환경 변수 지원

### 배포 단계

1. **Heroku CLI 설치 및 로그인**

   ```bash
   # Heroku CLI가 설치되어 있지 않다면
   # https://devcenter.heroku.com/articles/heroku-cli 에서 다운로드

   heroku login
   ```

2. **Heroku 앱 생성**

   ```bash
   heroku create your-app-name
   ```

3. **PostgreSQL 애드온 추가**

   ```bash
   heroku addons:create heroku-postgresql:essential-0 -a your-app-name
   ```

4. **환경 변수 설정**

   ```bash
   heroku config:set JWT_SECRET=your-secure-256-bit-secret-key -a your-app-name
   ```

5. **Git 설정 및 배포**

   ```bash
   # Git 저장소 초기화 (아직 안 했다면)
   git init
   git add .
   git commit -m "Initial commit"

   # Heroku git remote 추가
   heroku git:remote -a your-app-name

   # 배포
   git push heroku master
   ```

6. **데이터베이스 마이그레이션**

   애플리케이션이 시작되면 JPA가 자동으로 테이블을 생성합니다 (ddl-auto=update).

7. **앱 열기**

   ```bash
   heroku open -a your-app-name
   ```

### 배포 확인

배포가 완료되면 다음 URL에서 API에 접근할 수 있습니다:

- **API Base URL**: `https://your-app-name.herokuapp.com`
- **Swagger UI**: `https://your-app-name.herokuapp.com/swagger-ui.html`

### 로그 확인

```bash
heroku logs --tail -a your-app-name
```

### 환경 변수 확인

```bash
heroku config -a your-app-name
```

### 주의사항

- **JWT_SECRET**: 반드시 안전한 256비트 시크릿 키로 변경하세요.
- **PostgreSQL**: Heroku의 무료 플랜은 10,000행 제한이 있습니다.
- **비용**: PostgreSQL essential-0 플랜은 월 최대 $5입니다.

### 현재 배포된 앱

**앱 이름**: `survey-system-api`
**URL**: https://survey-system-api-dd94bac93976.herokuapp.com
**Swagger**: https://survey-system-api-dd94bac93976.herokuapp.com/swagger-ui.html

## 라이센스

이 프로젝트는 학습 목적으로 작성되었습니다.

## 문의

문제가 발생하거나 개선 사항이 있으면 이슈를 등록해주세요.
