# 기여 가이드

File Upload and Image Processing Service 프로젝트에 기여해주셔서 감사합니다!

## 기여 방법

### 1. 이슈 생성

버그 리포트나 기능 제안을 위해 GitHub Issues를 사용해주세요.

### 2. Fork 및 Clone

```bash
# 저장소 Fork 후 Clone
git clone https://github.com/your-username/file-upload-service.git
cd file-upload-service
```

### 3. 브랜치 생성

```bash
git checkout -b feature/your-feature-name
# 또는
git checkout -b bugfix/issue-number
```

### 4. 개발

- 코드 스타일 가이드를 따라주세요
- 테스트를 작성해주세요
- 커밋 메시지는 명확하게 작성해주세요

### 5. 테스트

```bash
mvn test
```

### 6. Pull Request

- 변경 사항을 명확히 설명해주세요
- 관련 이슈 번호를 언급해주세요

## 코드 스타일

- Java 21 기능 활용
- Lombok 사용 권장
- Spring Boot 3.2 베스트 프랙티스 준수
- RESTful API 디자인 원칙 준수

## 라이센스

기여한 코드는 MIT 라이센스에 따라 배포됩니다.
