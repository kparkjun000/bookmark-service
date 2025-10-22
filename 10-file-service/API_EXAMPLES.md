# API 사용 예제

이 문서는 File Upload and Image Processing Service API의 사용 예제를 제공합니다.

## 목차

1. [파일 업로드](#파일-업로드)
2. [다중 파일 업로드](#다중-파일-업로드)
3. [파일 다운로드](#파일-다운로드)
4. [파일 미리보기](#파일-미리보기)
5. [썸네일 조회](#썸네일-조회)
6. [파일 메타데이터 조회](#파일-메타데이터-조회)
7. [파일 목록 조회](#파일-목록-조회)
8. [파일 검색](#파일-검색)
9. [파일 삭제](#파일-삭제)

---

## 파일 업로드

### cURL

```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/image.jpg" \
  -F "description=My beautiful image" \
  -F "uploadedBy=john_doe" \
  -F "generateThumbnail=true" \
  -F "resizeImage=false"
```

### JavaScript (Fetch API)

```javascript
const formData = new FormData();
formData.append("file", fileInput.files[0]);
formData.append("description", "My beautiful image");
formData.append("uploadedBy", "john_doe");
formData.append("generateThumbnail", "true");
formData.append("resizeImage", "false");

fetch("http://localhost:8080/api/files/upload", {
  method: "POST",
  body: formData,
})
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

### Python (requests)

```python
import requests

url = 'http://localhost:8080/api/files/upload'
files = {'file': open('/path/to/image.jpg', 'rb')}
data = {
    'description': 'My beautiful image',
    'uploadedBy': 'john_doe',
    'generateThumbnail': 'true',
    'resizeImage': 'false'
}

response = requests.post(url, files=files, data=data)
print(response.json())
```

### 응답 예제

```json
{
  "success": true,
  "message": "File uploaded successfully",
  "data": {
    "fileId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "originalFileName": "image.jpg",
    "contentType": "image/jpeg",
    "fileSize": 2048576,
    "width": 1920,
    "height": 1080,
    "downloadUrl": "/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/download",
    "thumbnailUrl": "/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/thumbnail",
    "uploadedAt": "2024-01-15T10:30:00",
    "message": "File uploaded successfully"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 다중 파일 업로드

### cURL

```bash
curl -X POST http://localhost:8080/api/files/upload-multiple \
  -F "files=@/path/to/image1.jpg" \
  -F "files=@/path/to/image2.jpg" \
  -F "files=@/path/to/image3.png" \
  -F "description=Multiple images" \
  -F "uploadedBy=john_doe" \
  -F "generateThumbnail=true" \
  -F "resizeImage=false"
```

### JavaScript (Fetch API)

```javascript
const formData = new FormData();
Array.from(fileInput.files).forEach((file) => {
  formData.append("files", file);
});
formData.append("description", "Multiple images");
formData.append("uploadedBy", "john_doe");
formData.append("generateThumbnail", "true");

fetch("http://localhost:8080/api/files/upload-multiple", {
  method: "POST",
  body: formData,
})
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

---

## 파일 다운로드

### cURL

```bash
curl -X GET http://localhost:8080/api/files/{fileId}/download \
  -o downloaded_file.jpg
```

### JavaScript

```javascript
// 브라우저에서 파일 다운로드 트리거
window.location.href = `http://localhost:8080/api/files/${fileId}/download`;

// 또는 Fetch API 사용
fetch(`http://localhost:8080/api/files/${fileId}/download`)
  .then((response) => response.blob())
  .then((blob) => {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "filename.jpg";
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
  });
```

---

## 파일 미리보기

### HTML (이미지 태그)

```html
<img src="http://localhost:8080/api/files/{fileId}/view" alt="Preview" />
```

### JavaScript

```javascript
const imgElement = document.createElement("img");
imgElement.src = `http://localhost:8080/api/files/${fileId}/view`;
document.body.appendChild(imgElement);
```

---

## 썸네일 조회

### HTML

```html
<img src="http://localhost:8080/api/files/{fileId}/thumbnail" alt="Thumbnail" />
```

### cURL

```bash
curl -X GET http://localhost:8080/api/files/{fileId}/thumbnail \
  -o thumbnail.jpg
```

---

## 파일 메타데이터 조회

### cURL

```bash
curl -X GET http://localhost:8080/api/files/{fileId}
```

### JavaScript

```javascript
fetch(`http://localhost:8080/api/files/${fileId}`)
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

### 응답 예제

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "fileId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "originalFileName": "image.jpg",
    "contentType": "image/jpeg",
    "fileSize": 2048576,
    "fileExtension": "jpg",
    "width": 1920,
    "height": 1080,
    "hasThumbnail": true,
    "downloadUrl": "http://localhost:8080/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/download",
    "thumbnailUrl": "http://localhost:8080/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/thumbnail",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00",
    "uploadedBy": "john_doe",
    "description": "My beautiful image",
    "status": "ACTIVE"
  },
  "timestamp": "2024-01-15T11:00:00"
}
```

---

## 파일 목록 조회

### cURL (기본)

```bash
curl -X GET "http://localhost:8080/api/files?page=0&size=10"
```

### cURL (정렬 옵션 포함)

```bash
curl -X GET "http://localhost:8080/api/files?page=0&size=10&sortBy=createdAt&sortOrder=desc"
```

### JavaScript

```javascript
const params = new URLSearchParams({
  page: 0,
  size: 10,
  sortBy: "createdAt",
  sortOrder: "desc",
});

fetch(`http://localhost:8080/api/files?${params}`)
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

### 응답 예제

```json
{
  "success": true,
  "message": "Success",
  "data": {
    "files": [
      {
        "id": 1,
        "fileId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
        "originalFileName": "image1.jpg",
        "contentType": "image/jpeg",
        "fileSize": 2048576,
        "width": 1920,
        "height": 1080,
        "hasThumbnail": true,
        "downloadUrl": "http://localhost:8080/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/download",
        "thumbnailUrl": "http://localhost:8080/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/thumbnail",
        "createdAt": "2024-01-15T10:30:00",
        "uploadedBy": "john_doe"
      }
    ],
    "currentPage": 0,
    "totalPages": 5,
    "totalElements": 42,
    "pageSize": 10
  },
  "timestamp": "2024-01-15T11:00:00"
}
```

---

## 파일 검색

### cURL

```bash
curl -X GET "http://localhost:8080/api/files/search?keyword=landscape&page=0&size=10"
```

### JavaScript

```javascript
const params = new URLSearchParams({
  keyword: "landscape",
  page: 0,
  size: 10,
});

fetch(`http://localhost:8080/api/files/search?${params}`)
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

---

## 파일 삭제

### cURL

```bash
curl -X DELETE http://localhost:8080/api/files/{fileId}
```

### JavaScript

```javascript
fetch(`http://localhost:8080/api/files/${fileId}`, {
  method: "DELETE",
})
  .then((response) => response.json())
  .then((data) => console.log(data))
  .catch((error) => console.error("Error:", error));
```

### 응답 예제

```json
{
  "success": true,
  "message": "File with ID a1b2c3d4-e5f6-7890-abcd-ef1234567890 has been deleted",
  "data": "File deleted successfully",
  "timestamp": "2024-01-15T11:30:00"
}
```

---

## 에러 처리

### 파일 크기 초과 에러

```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 413,
  "error": "File Size Exceeded",
  "message": "The file size exceeds the maximum allowed size",
  "path": "/api/files/upload"
}
```

### 잘못된 파일 형식 에러

```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 400,
  "error": "Invalid File",
  "message": "File extension 'exe' is not allowed. Allowed extensions: jpg, jpeg, png, gif, webp, bmp",
  "path": "/api/files/upload"
}
```

### 파일 없음 에러

```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 404,
  "error": "File Not Found",
  "message": "File not found with id: a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "path": "/api/files/a1b2c3d4-e5f6-7890-abcd-ef1234567890/download"
}
```

---

## HTML 폼 예제

```html
<!DOCTYPE html>
<html>
  <head>
    <title>File Upload Example</title>
  </head>
  <body>
    <h1>File Upload</h1>
    <form id="uploadForm" enctype="multipart/form-data">
      <div>
        <label>Select File:</label>
        <input
          type="file"
          name="file"
          id="fileInput"
          accept="image/*"
          required
        />
      </div>
      <div>
        <label>Description:</label>
        <input type="text" name="description" id="description" />
      </div>
      <div>
        <label>Uploaded By:</label>
        <input type="text" name="uploadedBy" id="uploadedBy" />
      </div>
      <div>
        <label>
          <input
            type="checkbox"
            name="generateThumbnail"
            id="generateThumbnail"
            checked
          />
          Generate Thumbnail
        </label>
      </div>
      <div>
        <label>
          <input type="checkbox" name="resizeImage" id="resizeImage" />
          Resize Image
        </label>
      </div>
      <button type="submit">Upload</button>
    </form>

    <div id="result"></div>

    <script>
      document
        .getElementById("uploadForm")
        .addEventListener("submit", function (e) {
          e.preventDefault();

          const formData = new FormData();
          formData.append(
            "file",
            document.getElementById("fileInput").files[0]
          );
          formData.append(
            "description",
            document.getElementById("description").value
          );
          formData.append(
            "uploadedBy",
            document.getElementById("uploadedBy").value
          );
          formData.append(
            "generateThumbnail",
            document.getElementById("generateThumbnail").checked
          );
          formData.append(
            "resizeImage",
            document.getElementById("resizeImage").checked
          );

          fetch("http://localhost:8080/api/files/upload", {
            method: "POST",
            body: formData,
          })
            .then((response) => response.json())
            .then((data) => {
              console.log("Success:", data);
              document.getElementById("result").innerHTML = `
                    <h2>Upload Successful!</h2>
                    <p>File ID: ${data.data.fileId}</p>
                    <p>Original Name: ${data.data.originalFileName}</p>
                    <img src="http://localhost:8080${data.data.thumbnailUrl}" alt="Thumbnail">
                    <br>
                    <a href="http://localhost:8080${data.data.downloadUrl}" download>Download File</a>
                `;
            })
            .catch((error) => {
              console.error("Error:", error);
              document.getElementById(
                "result"
              ).innerHTML = `<p style="color: red;">Upload failed: ${error.message}</p>`;
            });
        });
    </script>
  </body>
</html>
```

---

## Postman 컬렉션

Postman에서 사용할 수 있는 컬렉션 예제:

1. **Environment 설정**

   - Variable: `base_url`
   - Value: `http://localhost:8080`

2. **파일 업로드 요청**

   - Method: POST
   - URL: `{{base_url}}/api/files/upload`
   - Body: form-data
     - file: (File)
     - description: "Test image"
     - uploadedBy: "tester"
     - generateThumbnail: true
     - resizeImage: false

3. **Tests 스크립트 (응답에서 fileId 추출)**
   ```javascript
   if (pm.response.code === 200) {
     const response = pm.response.json();
     pm.environment.set("fileId", response.data.fileId);
   }
   ```

이렇게 하면 다음 요청에서 `{{fileId}}`를 사용할 수 있습니다.
