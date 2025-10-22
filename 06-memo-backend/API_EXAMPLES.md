# API 사용 예시

이 문서는 Memo Backend API의 실제 사용 예시를 제공합니다.

## 📌 목차

1. [메모 관리](#메모-관리)
2. [태그 관리](#태그-관리)
3. [메모 검색](#메모-검색)
4. [메모 공유](#메모-공유)

---

## 메모 관리

### 1. 메모 생성

**요청:**

```bash
POST /api/memos
Content-Type: application/json

{
  "title": "Spring Boot 학습",
  "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
  "author": "김개발",
  "tagNames": ["학습", "Spring"]
}
```

**응답:**

```json
{
  "success": true,
  "message": "메모가 성공적으로 생성되었습니다.",
  "data": {
    "id": 1,
    "title": "Spring Boot 학습",
    "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
    "author": "김개발",
    "tags": [
      {
        "id": 1,
        "name": "학습",
        "color": "#3B82F6"
      },
      {
        "id": 2,
        "name": "Spring",
        "color": "#3B82F6"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 2. 모든 메모 조회

**요청:**

```bash
GET /api/memos
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot 학습",
      "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
      "author": "김개발",
      "tags": [
        {
          "id": 1,
          "name": "학습",
          "color": "#3B82F6"
        }
      ],
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T10:35:00"
}
```

### 3. 특정 메모 조회

**요청:**

```bash
GET /api/memos/1
```

**응답:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Spring Boot 학습",
    "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
    "author": "김개발",
    "tags": [
      {
        "id": 1,
        "name": "학습",
        "color": "#3B82F6"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:35:00"
}
```

### 4. 메모 수정

**요청:**

```bash
PUT /api/memos/1
Content-Type: application/json

{
  "title": "Spring Boot 3.2 학습 완료",
  "content": "Spring Boot 3.2의 주요 기능을 모두 학습했습니다.",
  "tagNames": ["학습", "Spring", "완료"]
}
```

**응답:**

```json
{
  "success": true,
  "message": "메모가 성공적으로 수정되었습니다.",
  "data": {
    "id": 1,
    "title": "Spring Boot 3.2 학습 완료",
    "content": "Spring Boot 3.2의 주요 기능을 모두 학습했습니다.",
    "author": "김개발",
    "tags": [
      {
        "id": 1,
        "name": "학습",
        "color": "#3B82F6"
      },
      {
        "id": 2,
        "name": "Spring",
        "color": "#3B82F6"
      },
      {
        "id": 3,
        "name": "완료",
        "color": "#3B82F6"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00"
  },
  "timestamp": "2024-01-15T11:00:00"
}
```

### 5. 메모 삭제

**요청:**

```bash
DELETE /api/memos/1
```

**응답:**

```json
{
  "success": true,
  "message": "메모가 성공적으로 삭제되었습니다.",
  "timestamp": "2024-01-15T11:05:00"
}
```

---

## 태그 관리

### 1. 태그 생성

**요청:**

```bash
POST /api/tags
Content-Type: application/json

{
  "name": "긴급",
  "color": "#EF4444"
}
```

**응답:**

```json
{
  "success": true,
  "message": "태그가 성공적으로 생성되었습니다.",
  "data": {
    "id": 1,
    "name": "긴급",
    "color": "#EF4444",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 2. 모든 태그 조회

**요청:**

```bash
GET /api/tags
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "긴급",
      "color": "#EF4444",
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    },
    {
      "id": 2,
      "name": "학습",
      "color": "#3B82F6",
      "createdAt": "2024-01-15T10:31:00",
      "updatedAt": "2024-01-15T10:31:00"
    }
  ],
  "timestamp": "2024-01-15T10:35:00"
}
```

### 3. 태그 수정

**요청:**

```bash
PUT /api/tags/1
Content-Type: application/json

{
  "name": "중요",
  "color": "#F59E0B"
}
```

**응답:**

```json
{
  "success": true,
  "message": "태그가 성공적으로 수정되었습니다.",
  "data": {
    "id": 1,
    "name": "중요",
    "color": "#F59E0B",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T11:00:00"
  },
  "timestamp": "2024-01-15T11:00:00"
}
```

---

## 메모 검색

### 1. 키워드로 검색

**요청:**

```bash
GET /api/memos/search?keyword=Spring
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot 학습",
      "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
      "author": "김개발",
      "tags": [
        {
          "id": 2,
          "name": "Spring",
          "color": "#3B82F6"
        }
      ],
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T10:35:00"
}
```

### 2. 태그로 검색

**요청:**

```bash
GET /api/memos/tag/학습
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot 학습",
      "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
      "author": "김개발",
      "tags": [
        {
          "id": 1,
          "name": "학습",
          "color": "#3B82F6"
        }
      ],
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T10:35:00"
}
```

### 3. 작성자로 검색

**요청:**

```bash
GET /api/memos/author/김개발
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "title": "Spring Boot 학습",
      "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
      "author": "김개발",
      "tags": [
        {
          "id": 1,
          "name": "학습",
          "color": "#3B82F6"
        }
      ],
      "createdAt": "2024-01-15T10:30:00",
      "updatedAt": "2024-01-15T10:30:00"
    }
  ],
  "timestamp": "2024-01-15T10:35:00"
}
```

---

## 메모 공유

### 1. 공유 링크 생성 (24시간 유효)

**요청:**

```bash
POST /api/shared/memos/1
Content-Type: application/json

{
  "expiresInHours": 24
}
```

**응답:**

```json
{
  "success": true,
  "message": "공유 링크가 생성되었습니다.",
  "data": {
    "id": 1,
    "memoId": 1,
    "shareToken": "AbC123XyZ_-randomToken456",
    "expiresAt": "2024-01-16T10:30:00",
    "isActive": true,
    "createdAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 2. 공유 링크 생성 (무제한)

**요청:**

```bash
POST /api/shared/memos/1
Content-Type: application/json

{}
```

**응답:**

```json
{
  "success": true,
  "message": "공유 링크가 생성되었습니다.",
  "data": {
    "id": 2,
    "memoId": 1,
    "shareToken": "DeF789UvW_-randomToken012",
    "expiresAt": null,
    "isActive": true,
    "createdAt": "2024-01-15T10:35:00"
  },
  "timestamp": "2024-01-15T10:35:00"
}
```

### 3. 공유 토큰으로 메모 조회

**요청:**

```bash
GET /api/shared/AbC123XyZ_-randomToken456
```

**응답:**

```json
{
  "success": true,
  "data": {
    "id": 1,
    "title": "Spring Boot 학습",
    "content": "Spring Boot 3.2 버전의 새로운 기능을 학습해야 함",
    "author": "김개발",
    "tags": [
      {
        "id": 1,
        "name": "학습",
        "color": "#3B82F6"
      }
    ],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  "timestamp": "2024-01-15T10:40:00"
}
```

### 4. 메모의 공유 링크 목록 조회

**요청:**

```bash
GET /api/shared/memos/1/links
```

**응답:**

```json
{
  "success": true,
  "data": [
    {
      "id": 1,
      "memoId": 1,
      "shareToken": "AbC123XyZ_-randomToken456",
      "expiresAt": "2024-01-16T10:30:00",
      "isActive": true,
      "createdAt": "2024-01-15T10:30:00"
    },
    {
      "id": 2,
      "memoId": 1,
      "shareToken": "DeF789UvW_-randomToken012",
      "expiresAt": null,
      "isActive": true,
      "createdAt": "2024-01-15T10:35:00"
    }
  ],
  "timestamp": "2024-01-15T10:40:00"
}
```

### 5. 공유 링크 비활성화

**요청:**

```bash
DELETE /api/shared/AbC123XyZ_-randomToken456
```

**응답:**

```json
{
  "success": true,
  "message": "공유 링크가 비활성화되었습니다.",
  "timestamp": "2024-01-15T10:45:00"
}
```

---

## 에러 응답 예시

### 리소스를 찾을 수 없는 경우

**요청:**

```bash
GET /api/memos/999
```

**응답:**

```json
{
  "success": false,
  "error": "메모를 찾을 수 없습니다. ID: 999",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 유효성 검증 실패

**요청:**

```bash
POST /api/memos
Content-Type: application/json

{
  "title": "",
  "content": ""
}
```

**응답:**

```json
{
  "success": false,
  "error": "Validation failed",
  "data": {
    "title": "제목은 필수입니다",
    "content": "내용은 필수입니다"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 중복 리소스

**요청:**

```bash
POST /api/tags
Content-Type: application/json

{
  "name": "학습",
  "color": "#3B82F6"
}
```

**응답:**

```json
{
  "success": false,
  "error": "이미 존재하는 태그입니다: 학습",
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 🔗 추가 리소스

- [Swagger UI](http://localhost:8080/swagger-ui.html) - 대화형 API 문서
- [API Docs JSON](http://localhost:8080/api-docs) - OpenAPI 3.0 스펙
