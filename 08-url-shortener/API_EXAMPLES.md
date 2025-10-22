# API 사용 예제

## URL Shortener Service (포트 8080)

### 1. URL 단축 생성

**기본 생성:**

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://www.example.com/very/long/url/path"
  }'
```

**응답:**

```json
{
  "id": 1,
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://www.example.com/very/long/url/path",
  "clickCount": 0,
  "createdAt": "2025-10-12T10:30:00",
  "expiresAt": "2026-10-12T10:30:00",
  "isActive": true,
  "title": null,
  "description": null
}
```

**커스텀 별칭과 메타데이터:**

```bash
curl -X POST http://localhost:8080/api/urls \
  -H "Content-Type: application/json" \
  -d '{
    "originalUrl": "https://github.com/zero-base/url-shortener",
    "customAlias": "github-repo",
    "title": "Zero Base URL Shortener Repository",
    "description": "URL 단축 서비스 GitHub 저장소",
    "expirationDays": 90
  }'
```

**응답:**

```json
{
  "id": 2,
  "shortCode": "github-repo",
  "shortUrl": "http://localhost:8080/github-repo",
  "originalUrl": "https://github.com/zero-base/url-shortener",
  "clickCount": 0,
  "createdAt": "2025-10-12T10:35:00",
  "expiresAt": "2026-01-10T10:35:00",
  "isActive": true,
  "title": "Zero Base URL Shortener Repository",
  "description": "URL 단축 서비스 GitHub 저장소"
}
```

### 2. 단축 URL 정보 조회

```bash
curl http://localhost:8080/api/urls/abc123
```

**응답:**

```json
{
  "id": 1,
  "shortCode": "abc123",
  "shortUrl": "http://localhost:8080/abc123",
  "originalUrl": "https://www.example.com/very/long/url/path",
  "clickCount": 15,
  "createdAt": "2025-10-12T10:30:00",
  "expiresAt": "2026-10-12T10:30:00",
  "isActive": true,
  "title": null,
  "description": null
}
```

### 3. 모든 URL 조회

```bash
curl http://localhost:8080/api/urls
```

### 4. 단축 URL로 리디렉션

브라우저에서:

```
http://localhost:8080/abc123
```

또는 curl로:

```bash
curl -L http://localhost:8080/abc123
```

### 5. QR 코드 생성

**기본 크기 (300x300):**

```bash
curl http://localhost:8080/api/urls/abc123/qr --output qr-code.png
```

**커스텀 크기:**

```bash
curl "http://localhost:8080/api/urls/abc123/qr?width=500&height=500" --output qr-code-large.png
```

브라우저에서 확인:

```
http://localhost:8080/api/urls/abc123/qr
```

### 6. URL 삭제 (비활성화)

```bash
curl -X DELETE http://localhost:8080/api/urls/abc123
```

**응답:** 204 No Content

---

## Analytics Service (포트 8081)

### 1. 전체 통계 조회

```bash
curl http://localhost:8081/api/analytics/abc123
```

**응답:**

```json
{
  "shortCode": "abc123",
  "originalUrl": "https://www.example.com/very/long/url/path",
  "totalClicks": 156,
  "clicksLast24Hours": 12,
  "clicksLast7Days": 89,
  "clicksLast30Days": 156,
  "createdAt": "2025-10-12T10:30:00",
  "firstClickAt": "2025-10-12T11:15:00",
  "lastClickAt": "2025-10-12T15:45:00",
  "topBrowsers": {
    "Chrome": 89,
    "Firefox": 45,
    "Safari": 22
  },
  "topOperatingSystems": {
    "Windows": 78,
    "Mac OS X": 45,
    "Linux": 23,
    "Android": 10
  },
  "topDeviceTypes": {
    "Desktop": 125,
    "Mobile": 25,
    "Tablet": 6
  },
  "topCountries": {
    "Unknown": 156
  }
}
```

### 2. 시간대별 통계 (타임라인)

**일별 통계 (최근 30일):**

```bash
curl "http://localhost:8081/api/analytics/abc123/timeline?granularity=daily&days=30"
```

**응답:**

```json
{
  "shortCode": "abc123",
  "granularity": "daily",
  "dataPoints": [
    {
      "timestamp": "2025-09-12T00:00:00",
      "count": 5
    },
    {
      "timestamp": "2025-09-13T00:00:00",
      "count": 8
    },
    {
      "timestamp": "2025-09-14T00:00:00",
      "count": 12
    }
  ]
}
```

**시간별 통계 (최근 7일):**

```bash
curl "http://localhost:8081/api/analytics/abc123/timeline?granularity=hourly&days=7"
```

### 3. 브라우저별 통계

```bash
curl http://localhost:8081/api/analytics/abc123/browsers
```

**응답:**

```json
{
  "Chrome": 89,
  "Firefox": 45,
  "Safari": 22
}
```

### 4. 운영체제별 통계

```bash
curl http://localhost:8081/api/analytics/abc123/os
```

**응답:**

```json
{
  "Windows": 78,
  "Mac OS X": 45,
  "Linux": 23,
  "Android": 10
}
```

### 5. 디바이스별 통계

```bash
curl http://localhost:8081/api/analytics/abc123/devices
```

**응답:**

```json
{
  "Desktop": 125,
  "Mobile": 25,
  "Tablet": 6
}
```

### 6. 위치별 통계

```bash
curl http://localhost:8081/api/analytics/abc123/locations
```

**응답:**

```json
{
  "Unknown": 156
}
```

---

## PowerShell 예제

### URL 생성

```powershell
$body = @{
    originalUrl = "https://www.example.com/test"
    customAlias = "test-url"
    title = "Test URL"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/urls" `
    -Method Post `
    -ContentType "application/json" `
    -Body $body
```

### 통계 조회

```powershell
Invoke-RestMethod -Uri "http://localhost:8081/api/analytics/test-url"
```

---

## JavaScript/Fetch 예제

### URL 생성

```javascript
fetch("http://localhost:8080/api/urls", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
  },
  body: JSON.stringify({
    originalUrl: "https://www.example.com/test",
    customAlias: "test-url",
    title: "Test URL",
    expirationDays: 90,
  }),
})
  .then((response) => response.json())
  .then((data) => console.log(data));
```

### 통계 조회

```javascript
fetch("http://localhost:8081/api/analytics/test-url")
  .then((response) => response.json())
  .then((data) => console.log(data));
```

---

## 에러 응답 예제

### URL Not Found (404)

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "URL not found: invalid-code",
  "timestamp": "2025-10-12T15:45:00"
}
```

### Custom Alias Already Exists (409)

```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Custom alias already exists: test-url",
  "timestamp": "2025-10-12T15:45:00"
}
```

### Validation Error (400)

```json
{
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "originalUrl": "Original URL is required",
    "customAlias": "Custom alias can only contain letters, numbers, hyphens and underscores"
  }
}
```

---

## Postman Collection

Postman 컬렉션을 import하려면 다음 JSON을 사용하세요:

```json
{
  "info": {
    "name": "URL Shortener API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Create Short URL",
      "request": {
        "method": "POST",
        "header": [{ "key": "Content-Type", "value": "application/json" }],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"originalUrl\": \"https://www.example.com/test\"\n}"
        },
        "url": { "raw": "http://localhost:8080/api/urls" }
      }
    },
    {
      "name": "Get Analytics",
      "request": {
        "method": "GET",
        "url": { "raw": "http://localhost:8081/api/analytics/{{shortCode}}" }
      }
    }
  ]
}
```
