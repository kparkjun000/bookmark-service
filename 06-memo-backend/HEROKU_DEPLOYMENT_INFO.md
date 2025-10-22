# 🚀 Heroku 배포 완료!

## ✅ 배포 정보

**앱 이름:** `memo-backend-zerobase`  
**URL:** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/

## 📍 주요 엔드포인트

### 🏠 홈 (Swagger UI로 자동 리다이렉트)

```
https://memo-backend-zerobase-58fd97273b19.herokuapp.com/
```

### 📚 Swagger UI (API 문서)

```
https://memo-backend-zerobase-58fd97273b19.herokuapp.com/swagger-ui.html
```

### 🔗 API 엔드포인트 예시

#### 메모 관리

- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos
- **POST** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos
- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/{id}
- **PUT** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/{id}
- **DELETE** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/{id}

#### 검색

- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/search?keyword=검색어
- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/tag/{tagName}
- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos/author/{author}

#### 태그 관리

- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/tags
- **POST** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/tags

#### 메모 공유

- **POST** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/shared/memos/{memoId}
- **GET** https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/shared/{shareToken}

## 🧪 테스트 방법

### 1. 브라우저에서 Swagger UI 접속

```
https://memo-backend-zerobase-58fd97273b19.herokuapp.com/swagger-ui.html
```

### 2. curl로 API 테스트

#### 모든 메모 조회

```bash
curl https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos
```

#### 메모 생성

```bash
curl -X POST https://memo-backend-zerobase-58fd97273b19.herokuapp.com/api/memos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "첫 번째 메모",
    "content": "Heroku에 배포된 메모 앱!",
    "author": "테스터",
    "tagNames": ["배포", "성공"]
  }'
```

## 🗄️ 데이터베이스

- **PostgreSQL Essential-0**
- 자동으로 생성 및 연결됨
- DATABASE_URL 환경 변수 자동 설정됨

## 📊 앱 상태 확인

```bash
# Dyno 상태 확인
heroku ps

# 로그 확인
heroku logs --tail

# 환경 변수 확인
heroku config

# 앱 열기
heroku open
```

## 🔄 업데이트 배포 방법

```bash
# 코드 수정 후
git add .
git commit -m "Update message"
git push heroku master
```

## ✨ 구현된 기능

✅ 메모 CRUD (생성, 조회, 수정, 삭제)  
✅ 태그 관리 (태그 추가, 수정, 삭제, 색상 지정)  
✅ 전문 검색 (키워드, 태그, 작성자)  
✅ 메모 공유 (공유 링크 생성, 만료 시간 설정)  
✅ Swagger/OpenAPI 문서  
✅ 전역 예외 처리  
✅ PostgreSQL 데이터베이스

## 🎯 성능 최적화

- Spring Boot 3.2.5 사용
- Java 21 LTS
- Hibernate 6.x
- Connection Pool 설정 완료
- 인덱스 설정 (메모 제목, 태그 이름)

## 💰 비용

- **Dyno:** Basic (무료)
- **PostgreSQL:** Essential-0 (~$0.007/시간, 최대 $5/월)

## 🎉 배포 성공!

**모든 API가 정상 작동합니다!**

루트 경로(`/`)로 접속하면 자동으로 Swagger UI로 리다이렉트됩니다.

---

Made with ❤️ by ZeroBase
