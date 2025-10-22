# Zero Base 13주차 - Heroku 멀티서비스 프로젝트

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![Java](https://img.shields.io/badge/Java-17%20%7C%2021-orange)
![Heroku](https://img.shields.io/badge/Heroku-Deployed-purple)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)

**하나의 Git 저장소에서 10개의 독립적인 Spring Boot 서비스를 관리하고 배포하는 멀티서비스 모노레포**

[배포 가이드](./HEROKU_MULTI_SERVICE_GUIDE.md) | [API 문서](#api-문서) | [문제 해결](#문제-해결)

</div>

---

## 📋 목차

- [프로젝트 개요](#프로젝트-개요)
- [서비스 목록](#서비스-목록)
- [빠른 시작](#빠른-시작)
- [배포 방법](#배포-방법)
- [서비스 관리](#서비스-관리)
- [API 문서](#api-문서)
- [기술 스택](#기술-스택)
- [문제 해결](#문제-해결)

---

## 🎯 프로젝트 개요

이 프로젝트는 Zero Base 백엔드 스쿨 13주차 과제로, **멀티서비스 아키텍처**를 기반으로 한 모노레포입니다.

### 주요 특징

✨ **멀티서비스 아키텍처**

- 하나의 Git 저장소에서 10개의 독립적인 서비스 관리
- 각 서비스는 독립적으로 배포 및 운영
- 서비스별 독립적인 데이터베이스

🚀 **쉬운 배포**

- 자동화된 배포 스크립트
- 전체 또는 개별 서비스 선택 배포
- Git Subtree를 활용한 효율적인 배포

🔧 **완벽한 설정**

- 서비스별 최적화된 Java 버전
- PostgreSQL 자동 프로비저닝
- 환경변수 자동 설정

---

## 🗂️ 서비스 목록

| #   | 서비스               | Heroku URL                                                       | Java | 상태 | 설명                           |
| --- | -------------------- | ---------------------------------------------------------------- | ---- | ---- | ------------------------------ |
| 1   | **Todo API**         | [zb-todo-api](https://zb-todo-api.herokuapp.com)                 | 17   | 🟢   | 할 일 관리 CRUD API            |
| 2   | **Blog API**         | [zb-blog-api](https://zb-blog-api.herokuapp.com)                 | 17   | 🟢   | 블로그 게시글 및 카테고리 관리 |
| 3   | **Auth System**      | [zb-auth-system](https://zb-auth-system.herokuapp.com)           | 21   | 🟢   | JWT 기반 인증/권한 시스템      |
| 4   | **Bookmark Service** | [zb-bookmark-service](https://zb-bookmark-service.herokuapp.com) | 21   | 🟢   | URL 북마크 및 태그 관리        |
| 5   | **Shopping API**     | [zb-shopping-api](https://zb-shopping-api.herokuapp.com)         | 17   | 🟢   | 쇼핑몰 상품/주문 관리          |
| 6   | **Memo Backend**     | [zb-memo-backend](https://zb-memo-backend.herokuapp.com)         | 21   | 🟢   | 텍스트 메모 및 검색 기능       |
| 7   | **Weather Service**  | [zb-weather-service](https://zb-weather-service.herokuapp.com)   | 17   | 🟢   | 외부 API 연동 날씨 정보        |
| 8   | **URL Shortener**    | [zb-url-shortener](https://zb-url-shortener.herokuapp.com)       | 17   | 🟢   | URL 단축 및 클릭 통계          |
| 9   | **Survey System**    | [zb-survey-system](https://zb-survey-system.herokuapp.com)       | 21   | 🟢   | 설문조사 생성/응답 수집        |
| 10  | **File Service**     | [zb-file-service](https://zb-file-service.herokuapp.com)         | 21   | 🟢   | 파일 업로드 및 이미지 처리     |

---

## ⚡ 빠른 시작

### 1️⃣ 사전 준비

```powershell
# Heroku CLI 설치
choco install heroku-cli

# Heroku 로그인
heroku login

# Git 저장소 확인
git status
```

### 2️⃣ 앱 생성

모든 Heroku 앱을 한 번에 생성:

```powershell
.\create-all-apps.ps1
```

### 3️⃣ 배포

전체 서비스 배포:

```powershell
.\deploy-all-services.ps1
```

또는 개별 서비스 배포:

```powershell
.\deploy-single-service.ps1
```

### 4️⃣ 확인

모든 서비스 상태 확인:

```powershell
.\services-status.ps1
```

---

## 🚀 배포 방법

### 방법 1: 전체 일괄 배포

```powershell
# 모든 서비스를 한 번에 배포
.\deploy-all-services.ps1

# 배포 모드 선택:
# 1. 전체 서비스 배포 (10개 모두)
# 2. 개별 서비스 선택 배포
# 3. 앱만 생성 (배포 안함)
```

### 방법 2: 개별 서비스 배포

```powershell
# 특정 서비스 하나만 배포
.\deploy-single-service.ps1

# 서비스 번호 입력 (1-10)
```

### 방법 3: 수동 배포

```powershell
# 예: Todo API 배포
heroku create zb-todo-api --region us
heroku addons:create heroku-postgresql:essential-0 --app zb-todo-api
heroku config:set JAVA_VERSION=17 --app zb-todo-api
git remote add heroku-todo https://git.heroku.com/zb-todo-api.git
git subtree push --prefix 01-todo-api heroku-todo main
```

---

## 🔧 서비스 관리

### 로그 확인

```powershell
# 실시간 로그
heroku logs --tail --app zb-todo-api

# 최근 100줄
heroku logs -n 100 --app zb-todo-api
```

### 재시작

```powershell
heroku restart --app zb-todo-api
```

### 환경변수

```powershell
# 확인
heroku config --app zb-todo-api

# 설정
heroku config:set KEY=VALUE --app zb-todo-api

# 삭제
heroku config:unset KEY --app zb-todo-api
```

### 데이터베이스

```powershell
# 정보 확인
heroku pg:info --app zb-todo-api

# 데이터베이스 접속
heroku pg:psql --app zb-todo-api

# 백업
heroku pg:backups:capture --app zb-todo-api
```

### 서비스 상태 확인

```powershell
# 전체 서비스 상태
.\services-status.ps1

# 개별 서비스 상태
heroku ps --app zb-todo-api
```

---

## 📚 API 문서

각 서비스는 Swagger UI를 통해 API 문서를 제공합니다.

### Swagger UI 접속

```
https://<app-name>.herokuapp.com/swagger-ui/index.html
```

예시:

- Todo API: https://zb-todo-api.herokuapp.com/swagger-ui/index.html
- Blog API: https://zb-blog-api.herokuapp.com/swagger-ui/index.html
- Auth System: https://zb-auth-system.herokuapp.com/swagger-ui/index.html

### API 엔드포인트 예시

#### Todo API

```http
GET    /api/todos          # 모든 할일 조회
POST   /api/todos          # 새 할일 생성
GET    /api/todos/{id}     # 특정 할일 조회
PUT    /api/todos/{id}     # 할일 수정
DELETE /api/todos/{id}     # 할일 삭제
```

#### Auth System

```http
POST   /api/auth/register  # 회원가입
POST   /api/auth/login     # 로그인
GET    /api/auth/me        # 내 정보 조회
POST   /api/auth/refresh   # 토큰 갱신
```

#### Weather Service

```http
GET    /api/weather/current/{city}     # 현재 날씨
GET    /api/weather/forecast/{city}    # 날씨 예보
```

---

## 🛠️ 기술 스택

### Backend

- **Framework**: Spring Boot 3.2.x ~ 3.5.x
- **Language**: Java 17, Java 21
- **Build Tool**: Maven

### Database

- **Production**: PostgreSQL 16 (Heroku Postgres)
- **Development**: H2 Database (in-memory)

### Security

- **Authentication**: JWT (JSON Web Token)
- **Framework**: Spring Security

### API Documentation

- **Tool**: SpringDoc OpenAPI 3 (Swagger)

### Cloud Platform

- **Hosting**: Heroku
- **Region**: US

### Additional Libraries

- **Lombok**: 코드 간소화
- **Validation**: Bean Validation (JSR-380)
- **Image Processing**: Thumbnailator (파일 서비스)
- **QR Code**: ZXing (URL 단축 서비스)
- **Weather API**: OpenWeatherMap (날씨 서비스)

---

## 🐛 문제 해결

### 빌드 실패

```powershell
# 로그 확인
heroku logs --tail --app zb-todo-api

# 로컬 빌드 테스트
cd 01-todo-api
mvn clean package
```

### 데이터베이스 연결 오류

```powershell
# PostgreSQL 확인
heroku addons --app zb-todo-api

# 없으면 추가
heroku addons:create heroku-postgresql:essential-0 --app zb-todo-api
```

### Java 버전 불일치

```powershell
# Java 버전 설정
heroku config:set JAVA_VERSION=17 --app zb-todo-api
# 또는
heroku config:set JAVA_VERSION=21 --app zb-auth-system
```

### Git Push 실패

```powershell
# Force push (주의: 원격 변경사항 덮어씀)
git push heroku-todo `git subtree split --prefix 01-todo-api main`:main --force
```

---

## 📖 상세 문서

더 자세한 정보는 [Heroku 멀티서비스 배포 가이드](./HEROKU_MULTI_SERVICE_GUIDE.md)를 참조하세요.

---

## 📞 지원

### 리소스

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Spring Boot 문서](https://spring.io/projects/spring-boot)
- [Java on Heroku](https://devcenter.heroku.com/categories/java-support)

### 문제 보고

프로젝트 관련 문제나 제안사항이 있으면 이슈를 등록해주세요.

---

## 📄 라이선스

이 프로젝트는 Zero Base 백엔드 스쿨 13주차 과제입니다.

---

## 👥 기여

Zero Base 백엔드 스쿨 수강생

---

<div align="center">

**Made with ❤️ by Zero Base Backend School**

[⬆ 맨 위로 돌아가기](#zero-base-13주차---heroku-멀티서비스-프로젝트)

</div>
