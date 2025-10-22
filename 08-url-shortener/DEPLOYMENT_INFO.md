# 🚀 Heroku 배포 정보

## 배포 완료! ✅

**배포 일시**: 2025년 10월 12일

---

## 배포된 서비스

### 1. URL Shortener Service
- **URL**: https://aparkjun-url-shortener-0a458a370402.herokuapp.com/
- **Heroku App**: aparkjun-url-shortener
- **Database**: PostgreSQL (postgresql-slippery-84901)
- **Region**: US
- **Status**: ✅ Active (200 OK)

#### API 엔드포인트:
- `POST /api/urls` - URL 단축 생성
- `GET /api/urls/{shortCode}` - URL 정보 조회
- `GET /{shortCode}` - 리디렉션
- `GET /api/urls/{shortCode}/qr` - QR 코드 생성
- `DELETE /api/urls/{shortCode}` - URL 삭제

### 2. Analytics Service
- **URL**: https://aparkjun-analytics-da57014a4820.herokuapp.com/
- **Heroku App**: aparkjun-analytics
- **Database**: 공유 (postgresql-slippery-84901)
- **Region**: US
- **Status**: ✅ Active (200 OK)

#### API 엔드포인트:
- `GET /api/analytics/{shortCode}` - 전체 통계
- `GET /api/analytics/{shortCode}/timeline` - 시간대별 통계
- `GET /api/analytics/{shortCode}/browsers` - 브라우저별 통계
- `GET /api/analytics/{shortCode}/os` - OS별 통계
- `GET /api/analytics/{shortCode}/devices` - 디바이스별 통계
- `GET /api/analytics/{shortCode}/locations` - 위치별 통계

---

## 테스트 결과

### URL 단축 생성 ✅
```powershell
Invoke-RestMethod -Uri "https://aparkjun-url-shortener-0a458a370402.herokuapp.com/api/urls" `
    -Method Post `
    -ContentType "application/json" `
    -Body '{"originalUrl": "https://www.github.com"}'
```

**응답:**
```json
{
  "id": 1,
  "shortCode": "85KkTs",
  "shortUrl": "https://aparkjun-url-shortener-0a458a370402.herokuapp.com/85KkTs",
  "originalUrl": "https://www.github.com",
  "clickCount": 0,
  "createdAt": "2025-10-12T04:54:20.761486388",
  "expiresAt": "2026-10-12T04:54:20.754305754",
  "isActive": true
}
```

### 리디렉션 ✅
브라우저에서 접속:
```
https://aparkjun-url-shortener-0a458a370402.herokuapp.com/85KkTs
```
→ `https://www.github.com` 으로 리디렉션 (302 Found)

### 통계 조회 ✅
```powershell
Invoke-RestMethod -Uri "https://aparkjun-analytics-da57014a4820.herokuapp.com/api/analytics/85KkTs"
```

**응답:**
```json
{
  "shortCode": "85KkTs",
  "originalUrl": "https://www.github.com",
  "totalClicks": 1,
  "clicksLast24Hours": 1,
  "clicksLast7Days": 1,
  "clicksLast30Days": 1,
  "createdAt": "2025-10-12T04:54:20.761486",
  "firstClickAt": "2025-10-12T04:54:33.155768",
  "lastClickAt": "2025-10-12T04:54:33.155768",
  "topBrowsers": {"Unknown": 1},
  "topOperatingSystems": {"Windows": 1},
  "topDeviceTypes": {"Desktop": 1}
}
```

---

## 추가 테스트 예제

### 1. 커스텀 별칭으로 URL 생성
```powershell
$body = @{
    originalUrl = "https://www.naver.com"
    customAlias = "naver"
    title = "네이버"
    description = "한국의 대표 포털 사이트"
} | ConvertTo-Json

Invoke-RestMethod -Uri "https://aparkjun-url-shortener-0a458a370402.herokuapp.com/api/urls" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### 2. QR 코드 생성
```
https://aparkjun-url-shortener-0a458a370402.herokuapp.com/api/urls/85KkTs/qr
```

브라우저나 PowerShell로 다운로드:
```powershell
Invoke-WebRequest -Uri "https://aparkjun-url-shortener-0a458a370402.herokuapp.com/api/urls/85KkTs/qr" `
    -OutFile "qr-code.png"
```

### 3. 시간대별 통계
```powershell
Invoke-RestMethod -Uri "https://aparkjun-analytics-da57014a4820.herokuapp.com/api/analytics/85KkTs/timeline?granularity=hourly&days=7"
```

### 4. 브라우저별 통계
```powershell
Invoke-RestMethod -Uri "https://aparkjun-analytics-da57014a4820.herokuapp.com/api/analytics/85KkTs/browsers"
```

---

## 환경 설정

### 환경 변수
```bash
# URL Shortener Service
heroku config -a aparkjun-url-shortener

# Analytics Service
heroku config -a aparkjun-analytics
```

### 로그 확인
```bash
# URL Shortener Service 로그
heroku logs --tail -a aparkjun-url-shortener

# Analytics Service 로그
heroku logs --tail -a aparkjun-analytics
```

### 데이터베이스 접속
```bash
heroku pg:psql -a aparkjun-url-shortener
```

---

## 관리 명령어

### 앱 재시작
```bash
heroku restart -a aparkjun-url-shortener
heroku restart -a aparkjun-analytics
```

### 스케일 조정
```bash
heroku ps:scale web=1 -a aparkjun-url-shortener
heroku ps:scale web=1 -a aparkjun-analytics
```

### 데이터베이스 백업
```bash
heroku pg:backups:capture -a aparkjun-url-shortener
```

### 앱 정보 확인
```bash
heroku apps:info -a aparkjun-url-shortener
heroku apps:info -a aparkjun-analytics
```

---

## 비용 정보

- **Dyno**: Eco (각 앱당)
- **Database**: Essential-0 (~$5/month)
- **총 비용**: ~$5/month (데이터베이스 공유)

---

## 다음 단계

1. ✅ 배포 완료
2. ✅ API 테스트 완료
3. ✅ 리디렉션 작동 확인
4. ✅ 통계 수집 확인

### 향후 개선사항:
- [ ] 커스텀 도메인 연결
- [ ] HTTPS 강제
- [ ] Rate Limiting 추가
- [ ] 캐싱 추가 (Redis)
- [ ] 모니터링 대시보드

---

## 문제 해결

### 앱이 시작되지 않는 경우
```bash
heroku logs --tail -a <app-name>
heroku ps -a <app-name>
```

### 데이터베이스 연결 오류
```bash
heroku config:get DATABASE_URL -a <app-name>
heroku pg:info -a <app-name>
```

### 메모리 부족
```bash
heroku config:set JAVA_OPTS="-Xmx512m" -a <app-name>
```

---

## 🎉 배포 성공!

Multi-Service 프로젝트가 성공적으로 Heroku에 배포되었습니다!

**Happy Coding! 🚀**

