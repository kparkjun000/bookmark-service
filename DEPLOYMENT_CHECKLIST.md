# Heroku 멀티서비스 배포 체크리스트 ✅

## 사전 준비 단계

### 1. 필수 도구 설치

- [ ] Heroku CLI 설치 완료
  ```powershell
  heroku --version
  ```
- [ ] Git 설치 확인
  ```powershell
  git --version
  ```
- [ ] Java 설치 확인 (17, 21)
  ```powershell
  java -version
  ```
- [ ] Maven 설치 확인
  ```powershell
  mvn -version
  ```

### 2. Heroku 계정 설정

- [ ] Heroku 계정 생성 (https://signup.heroku.com/)
- [ ] Heroku CLI 로그인 완료
  ```powershell
  heroku login
  heroku auth:whoami
  ```
- [ ] 결제 정보 등록 (PostgreSQL 사용을 위해 필요)

### 3. Git 저장소 준비

- [ ] Git 저장소 초기화
  ```powershell
  cd C:\zero-base13week
  git init
  ```
- [ ] 초기 커밋 완료
  ```powershell
  git add .
  git commit -m "Initial commit for multi-service deployment"
  ```

---

## 서비스별 준비사항 확인

### 전체 서비스 공통

- [ ] 각 서비스 폴더에 `Procfile` 존재 확인
- [ ] 각 서비스 폴더에 `system.properties` 존재 확인
- [ ] 각 서비스 폴더에 `pom.xml` 존재 확인

### 개별 서비스 체크

#### ✅ 01-todo-api

- [ ] Procfile 존재
- [ ] system.properties (Java 17)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트
  ```powershell
  cd 01-todo-api
  mvn clean package
  ```

#### ✅ 02-blog-api

- [ ] Procfile 존재
- [ ] system.properties (Java 17)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

#### ✅ 03-auth-system

- [ ] Procfile 존재
- [ ] system.properties (Java 21)
- [ ] pom.xml 확인
- [ ] JWT_SECRET 환경변수 준비
- [ ] 로컬 빌드 테스트

#### ✅ 04-bookmark-service

- [ ] Procfile 존재
- [ ] system.properties (Java 21)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

#### ✅ 05-shopping-api

- [ ] Procfile 존재
- [ ] system.properties (Java 17)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

#### ✅ 06-memo-backend

- [ ] Procfile 존재
- [ ] system.properties (Java 21)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

#### ✅ 07-weather-service

- [ ] Procfile 존재
- [ ] system.properties (Java 17)
- [ ] pom.xml 확인
- [ ] WEATHER_API_KEY 준비 (OpenWeatherMap)
- [ ] 로컬 빌드 테스트

#### ✅ 08-url-shortener

- [ ] Procfile 존재
- [ ] system.properties (Java 17)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

#### ✅ 09-survey-system

- [ ] Procfile 존재 (survey-api 폴더)
- [ ] system.properties (Java 21)
- [ ] pom.xml 확인
- [ ] JWT_SECRET 환경변수 준비
- [ ] 로컬 빌드 테스트

#### ✅ 10-file-service

- [ ] Procfile 존재
- [ ] system.properties (Java 21)
- [ ] pom.xml 확인
- [ ] 로컬 빌드 테스트

---

## 배포 단계

### Phase 1: Heroku 앱 생성

- [ ] 모든 앱 생성 스크립트 실행
  ```powershell
  .\create-all-apps.ps1
  ```
- [ ] 10개 앱 모두 생성 확인
- [ ] PostgreSQL 애드온 추가 확인
- [ ] Java 버전 설정 확인

### Phase 2: 환경변수 설정

- [ ] Auth System JWT_SECRET 설정
  ```powershell
  heroku config:set JWT_SECRET=<your-secret> --app zb-auth-system
  ```
- [ ] Survey System JWT_SECRET 설정
  ```powershell
  heroku config:set JWT_SECRET=<your-secret> --app zb-survey-system
  ```
- [ ] Weather Service API Key 설정
  ```powershell
  heroku config:set WEATHER_API_KEY=<your-key> --app zb-weather-service
  ```

### Phase 3: 배포

- [ ] 배포 스크립트 실행
  ```powershell
  .\deploy-all-services.ps1
  ```
- [ ] 배포 모드 선택 (1: 전체, 2: 개별, 3: 스킵)
- [ ] 배포 진행 상황 모니터링
- [ ] 각 서비스 배포 성공 확인

---

## 배포 후 검증

### 서비스 상태 확인

- [ ] 전체 서비스 상태 확인
  ```powershell
  .\services-status.ps1
  ```
- [ ] 각 서비스 URL 접속 테스트

### API 테스트

- [ ] Todo API Swagger 접속
  - URL: https://zb-todo-api.herokuapp.com/swagger-ui/index.html
- [ ] Blog API Swagger 접속
  - URL: https://zb-blog-api.herokuapp.com/swagger-ui/index.html
- [ ] Auth System Swagger 접속
  - URL: https://zb-auth-system.herokuapp.com/swagger-ui/index.html
- [ ] Bookmark Service Swagger 접속
  - URL: https://zb-bookmark-service.herokuapp.com/swagger-ui/index.html
- [ ] Shopping API Swagger 접속
  - URL: https://zb-shopping-api.herokuapp.com/swagger-ui/index.html
- [ ] Memo Backend Swagger 접속
  - URL: https://zb-memo-backend.herokuapp.com/swagger-ui/index.html
- [ ] Weather Service Swagger 접속
  - URL: https://zb-weather-service.herokuapp.com/swagger-ui/index.html
- [ ] URL Shortener Swagger 접속
  - URL: https://zb-url-shortener.herokuapp.com/swagger-ui/index.html
- [ ] Survey System Swagger 접속
  - URL: https://zb-survey-system.herokuapp.com/swagger-ui/index.html
- [ ] File Service Swagger 접속
  - URL: https://zb-file-service.herokuapp.com/swagger-ui/index.html

### 기능 테스트

- [ ] Todo API: CRUD 작업 테스트
- [ ] Blog API: 게시글 생성/조회 테스트
- [ ] Auth System: 회원가입/로그인 테스트
- [ ] Bookmark Service: 북마크 추가/조회 테스트
- [ ] Shopping API: 상품/주문 관리 테스트
- [ ] Memo Backend: 메모 생성/검색 테스트
- [ ] Weather Service: 날씨 조회 테스트
- [ ] URL Shortener: URL 단축/리다이렉트 테스트
- [ ] Survey System: 설문 생성/응답 테스트
- [ ] File Service: 파일 업로드/다운로드 테스트

### 로그 확인

- [ ] 각 서비스의 로그에 에러 없는지 확인
  ```powershell
  heroku logs --tail --app zb-todo-api
  ```

### 데이터베이스 확인

- [ ] 각 서비스의 DB 연결 확인
  ```powershell
  heroku pg:info --app zb-todo-api
  ```

---

## 모니터링 설정

### 알림 설정

- [ ] Heroku Dashboard에서 알림 설정
- [ ] 빌드 실패 알림 활성화
- [ ] Dyno 재시작 알림 활성화

### 정기 점검

- [ ] 주간 로그 리뷰
- [ ] 월간 데이터베이스 백업
- [ ] 분기별 성능 검토

---

## 문서화

### 완료 문서

- [ ] README.md 검토
- [ ] HEROKU_MULTI_SERVICE_GUIDE.md 검토
- [ ] QUICK_START.md 검토
- [ ] service-commands.md 검토

### 팀 공유

- [ ] 배포된 URL 목록 공유
- [ ] API 문서 링크 공유
- [ ] 환경변수 관리 방법 공유
- [ ] 로그 확인 방법 공유

---

## 비용 관리

### 리소스 확인

- [ ] 각 앱의 Dyno 타입 확인 (Eco/Basic/Standard)
- [ ] PostgreSQL 플랜 확인 (Essential-0)
- [ ] 월간 예상 비용 계산

### 최적화

- [ ] 사용하지 않는 앱 확인
- [ ] 불필요한 애드온 제거
- [ ] Dyno 타입 최적화

---

## 비상 대응

### 롤백 준비

- [ ] 이전 버전 Git 태그 생성
- [ ] 데이터베이스 백업 완료
  ```powershell
  heroku pg:backups:capture --app <app-name>
  ```

### 문제 해결 가이드

- [ ] 빌드 실패 시 대응 방법 숙지
- [ ] 앱 시작 실패 시 대응 방법 숙지
- [ ] DB 연결 실패 시 대응 방법 숙지

---

## 최종 확인

### 배포 완료

- [ ] 10개 서비스 모두 정상 동작
- [ ] 모든 Swagger UI 접속 가능
- [ ] 기본 API 호출 성공
- [ ] 로그에 치명적 에러 없음

### 문서 업데이트

- [ ] 배포된 URL 업데이트
- [ ] 환경변수 설정 문서화
- [ ] 알려진 이슈 문서화

### 팀 인수인계

- [ ] 배포 프로세스 공유
- [ ] 관리 명령어 교육
- [ ] 문제 해결 가이드 공유
- [ ] Heroku Dashboard 접근 권한 설정

---

## 성공! 🎉

모든 체크리스트가 완료되었다면, 축하합니다!

### 다음 단계

1. **모니터링**: 서비스 상태를 정기적으로 확인
2. **최적화**: 성능과 비용을 지속적으로 개선
3. **업데이트**: 새로운 기능 추가 및 배포
4. **문서화**: 변경사항을 항상 문서화

### 유용한 리소스

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Spring Boot on Heroku](https://spring.io/guides/gs/spring-boot-heroku/)
- [PostgreSQL on Heroku](https://devcenter.heroku.com/categories/heroku-postgres)

---

**배포 날짜**: ******\_******  
**배포자**: ******\_******  
**버전**: 1.0.0
