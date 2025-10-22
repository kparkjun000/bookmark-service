# 🚀 빠른 시작 가이드

## 현재 터미널에서 Heroku 로그인 대기 중!

터미널에 다음 메시지가 보이고 있습니다:

```
heroku: Press any key to open up the browser to login or q to exit:
```

### 👉 다음 단계

1. **터미널에서 아무 키나 누르세요** (q 제외)
2. 브라우저가 자동으로 열립니다
3. Heroku에 로그인하세요
4. 로그인 성공 메시지를 확인하세요
5. 터미널로 돌아오세요

### 로그인 완료 후 실행할 명령어

```bash
# 방법 1: 자동 배포 스크립트 사용 (권장)
.\deploy.bat

# 방법 2: 수동 명령어
git push heroku master
```

## 📱 배포 완료 후 접속 URL

### ✨ Swagger UI (여기로 접속하세요!)

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/swagger-ui.html
```

이 URL로 접속하면:

- 📚 전체 API 문서 확인
- 🧪 API 직접 테스트
- 📝 요청/응답 예시 확인

### 🔗 기타 URL

**OpenAPI JSON**

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api-docs
```

**공개 북마크 조회**

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/public/bookmarks
```

**URL 메타데이터 추출 테스트**

```
https://zerobase-bookmark-service-0aab4ffd66ec.herokuapp.com/api/public/metadata?url=https://github.com
```

## 🎯 첫 API 테스트

배포가 완료되면 Swagger UI에서:

1. **GET /api/users** 클릭
2. "Try it out" 버튼 클릭
3. "Execute" 버튼 클릭
4. 응답에서 샘플 사용자 확인 ✅

```json
[
  {
    "id": 1,
    "email": "demo@example.com",
    "name": "데모 사용자",
    "createdAt": "2025-10-13T..."
  }
]
```

## 📊 샘플 데이터

앱이 시작되면 자동으로 생성되는 데이터:

- ✅ 사용자: demo@example.com
- ✅ 태그: 개발, 참고자료, 튜토리얼
- ✅ 북마크: Spring Boot, PostgreSQL, GitHub

## 🔍 배포 진행 상황 확인

다른 터미널을 열고 실행:

```bash
# 실시간 로그 확인
heroku logs --tail --app zerobase-bookmark-service

# 앱 상태 확인
heroku ps --app zerobase-bookmark-service

# PostgreSQL 상태 확인
heroku pg:info --app zerobase-bookmark-service
```

## ⚡ 빠른 문제 해결

### 배포 실패 시

```bash
# 로그 확인
heroku logs --tail

# PostgreSQL 대기 (1-2분)
heroku addons:wait postgresql-clean-34059

# 재배포
git push heroku master --force
```

### 앱이 응답하지 않을 시

```bash
# 앱 재시작
heroku restart --app zerobase-bookmark-service

# 상태 확인
heroku ps --app zerobase-bookmark-service
```

## 💡 팁

- 배포는 **3-5분** 정도 소요됩니다
- PostgreSQL 초기화는 **1-2분** 추가 소요됩니다
- Swagger UI는 앱 시작 **즉시** 사용 가능합니다
- 샘플 데이터는 **자동 생성**됩니다

## 📞 도움이 필요하면

1. `DEPLOY_GUIDE.md` - 상세 배포 가이드
2. `README.md` - 프로젝트 전체 문서
3. Heroku 로그 - `heroku logs --tail`

---

**지금 바로 시작하세요! 터미널에서 아무 키나 누르고 로그인하세요! 🚀**
