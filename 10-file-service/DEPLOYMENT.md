# Heroku 배포 가이드

이 문서는 File Upload and Image Processing Service를 Heroku에 배포하는 단계별 가이드입니다.

## 사전 준비

### 1. Heroku CLI 설치

#### Windows

```powershell
# Chocolatey 사용
choco install heroku-cli

# 또는 인스톨러 다운로드
# https://devcenter.heroku.com/articles/heroku-cli
```

#### macOS

```bash
brew tap heroku/brew && brew install heroku
```

#### Linux

```bash
curl https://cli-assets.heroku.com/install.sh | sh
```

### 2. Heroku 계정 생성

https://signup.heroku.com/ 에서 무료 계정을 생성합니다.

---

## 배포 단계

### 1. Heroku 로그인

```bash
heroku login
```

브라우저가 열리면 로그인을 진행합니다.

### 2. Git 저장소 초기화

```bash
# 프로젝트 루트 디렉토리에서
git init
git add .
git commit -m "Initial commit for Heroku deployment"
```

### 3. Heroku 앱 생성

```bash
# 앱 이름 자동 생성
heroku create

# 또는 특정 이름 지정
heroku create your-file-service-app
```

앱 이름은 전 세계적으로 유일해야 합니다.

### 4. PostgreSQL 데이터베이스 추가

```bash
# Essential 플랜 (무료)
heroku addons:create heroku-postgresql:essential-0

# 데이터베이스 정보 확인
heroku pg:info
```

데이터베이스 URL은 자동으로 `DATABASE_URL` 환경 변수로 설정됩니다.

### 5. 환경 변수 설정

```bash
# Spring 프로필 설정
heroku config:set SPRING_PROFILES_ACTIVE=prod

# 파일 업로드 디렉토리 (Heroku 임시 디렉토리)
heroku config:set FILE_UPLOAD_DIR=/tmp/uploads

# 설정 확인
heroku config
```

### 6. Java 버전 설정

`system.properties` 파일이 이미 생성되어 있습니다:

```properties
java.runtime.version=21
maven.version=3.9.6
```

### 7. Procfile 확인

`Procfile`이 이미 생성되어 있습니다:

```
web: java -Dserver.port=$PORT -Dspring.profiles.active=prod $JAVA_OPTS -jar target/file-upload-service-1.0.0.jar
```

### 8. 배포

```bash
git push heroku main
```

또는 다른 브랜치에서 배포하는 경우:

```bash
git push heroku your-branch:main
```

### 9. 앱 열기

```bash
heroku open
```

브라우저에서 `https://your-app-name.herokuapp.com`이 열립니다.

---

## 배포 후 확인

### 1. 로그 확인

```bash
# 실시간 로그 보기
heroku logs --tail

# 최근 로그 보기
heroku logs -n 1000
```

### 2. 앱 상태 확인

```bash
heroku ps
```

### 3. 데이터베이스 연결 확인

```bash
heroku pg:psql
```

데이터베이스 콘솔에 접속되면:

```sql
\dt  -- 테이블 목록 확인
SELECT * FROM file_metadata LIMIT 10;  -- 데이터 확인
\q  -- 종료
```

### 4. API 테스트

```bash
# Health check
curl https://your-app-name.herokuapp.com/api/health

# 파일 업로드 테스트
curl -X POST https://your-app-name.herokuapp.com/api/files/upload \
  -F "file=@/path/to/test-image.jpg" \
  -F "description=Test from Heroku" \
  -F "generateThumbnail=true"
```

---

## 문제 해결

### 빌드 실패

#### 1. Maven 빌드 에러

```bash
# 로컬에서 빌드 테스트
mvn clean package

# 빌드 로그 확인
heroku logs --tail
```

#### 2. Java 버전 불일치

`system.properties` 파일이 올바른지 확인:

```properties
java.runtime.version=21
```

### 애플리케이션 시작 실패

#### 1. 포트 바인딩 이슈

Heroku는 `$PORT` 환경 변수를 제공합니다. Procfile에서 이를 올바르게 설정했는지 확인:

```
web: java -Dserver.port=$PORT ...
```

#### 2. 데이터베이스 연결 실패

```bash
# DATABASE_URL 확인
heroku config:get DATABASE_URL

# 데이터베이스 재시작
heroku pg:restart
```

#### 3. 메모리 부족

```bash
# 다이노 스케일 확인
heroku ps

# 더 많은 메모리가 필요한 경우 (유료)
heroku ps:scale web=1:standard-1x
```

### 파일 업로드 이슈

Heroku는 **임시 파일 시스템**을 사용합니다:

- 파일은 `/tmp` 디렉토리에 저장됩니다
- **다이노가 재시작되면 파일이 삭제됩니다** (약 24시간마다)
- 영구 저장을 위해서는 Amazon S3, Cloudinary 등의 외부 스토리지 사용을 권장합니다

---

## Amazon S3 통합 (권장)

영구 파일 저장을 위해 Amazon S3를 통합하는 것을 권장합니다.

### 1. 의존성 추가 (pom.xml)

```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>
```

### 2. 환경 변수 설정

```bash
heroku config:set AWS_ACCESS_KEY_ID=your-access-key
heroku config:set AWS_SECRET_ACCESS_KEY=your-secret-key
heroku config:set AWS_REGION=us-east-1
heroku config:set AWS_S3_BUCKET_NAME=your-bucket-name
```

---

## 성능 최적화

### 1. 다이노 유형 변경 (유료)

```bash
# Standard-1X (더 많은 메모리와 CPU)
heroku ps:scale web=1:standard-1x

# Standard-2X
heroku ps:scale web=1:standard-2x
```

### 2. 자동 스케일링 설정

```bash
# 최소/최대 다이노 수 설정
heroku ps:autoscale:enable web --min 1 --max 3
```

### 3. CDN 사용

정적 파일 및 이미지에 대해 Cloudflare 또는 AWS CloudFront 같은 CDN 사용을 고려하세요.

---

## 모니터링

### 1. Heroku Dashboard

https://dashboard.heroku.com/apps/your-app-name

### 2. 로그 드레인 설정 (선택사항)

```bash
heroku drains:add https://your-log-service.com/logs
```

### 3. APM 도구 추가 (선택사항)

- New Relic
- Datadog
- Scout APM

```bash
# New Relic 예제
heroku addons:create newrelic:wayne
```

---

## 데이터베이스 백업

### 수동 백업

```bash
# 백업 생성
heroku pg:backups:capture

# 백업 목록 확인
heroku pg:backups

# 백업 다운로드
heroku pg:backups:download
```

### 자동 백업 (유료)

```bash
heroku pg:backups:schedule DATABASE_URL --at '02:00 UTC'
```

---

## 스케일링

### 수평 스케일링 (다이노 수 증가)

```bash
# 웹 다이노 2개로 증가
heroku ps:scale web=2
```

### 수직 스케일링 (다이노 유형 변경)

```bash
# Standard-1X로 업그레이드
heroku ps:type web=standard-1x
```

---

## 도메인 설정

### 커스텀 도메인 추가

```bash
# 도메인 추가
heroku domains:add www.yourdomain.com

# SSL 인증서 자동 프로비저닝
heroku certs:auto:enable
```

### DNS 설정

도메인 제공업체에서 CNAME 레코드를 추가합니다:

```
www.yourdomain.com -> your-app-name.herokuapp.com
```

---

## CI/CD 파이프라인

### GitHub 통합

1. Heroku Dashboard에서 앱 선택
2. "Deploy" 탭 클릭
3. "GitHub" 선택 후 저장소 연결
4. "Enable Automatic Deploys" 활성화
5. "Wait for CI to pass before deploy" 선택 (권장)

### Heroku Pipelines

```bash
# 파이프라인 생성
heroku pipelines:create file-service-pipeline

# 스테이징 앱 추가
heroku pipelines:add file-service-pipeline --app file-service-staging --stage staging

# 프로덕션 앱 추가
heroku pipelines:add file-service-pipeline --app file-service-prod --stage production
```

---

## 비용 최적화

### 무료 티어 제한

- **Eco Dyno**: 월 1,000시간 (약 $5/월)
- **Essential Postgres**: 1GB 스토리지, 20 연결

### 비용 절감 팁

1. 불필요한 다이노 중지: `heroku ps:scale web=0`
2. 데이터베이스 정리: 오래된 파일 메타데이터 삭제
3. 외부 스토리지 사용: S3 라이프사이클 정책 활용

---

## 체크리스트

배포 전 확인사항:

- [ ] `pom.xml`에 모든 필요한 의존성이 포함되어 있는가?
- [ ] `system.properties`에 Java 21이 지정되어 있는가?
- [ ] `Procfile`이 올바르게 설정되어 있는가?
- [ ] `.gitignore`에 불필요한 파일이 제외되어 있는가?
- [ ] 환경 변수가 모두 설정되어 있는가?
- [ ] 데이터베이스 마이그레이션이 완료되었는가?
- [ ] API 엔드포인트가 정상 작동하는가?
- [ ] 에러 핸들링이 적절한가?
- [ ] 로그가 제대로 출력되는가?
- [ ] CORS 설정이 적절한가?

---

## 유용한 명령어 모음

```bash
# 앱 정보 확인
heroku apps:info

# 환경 변수 확인
heroku config

# 데이터베이스 정보
heroku pg:info

# 실시간 로그
heroku logs --tail

# 앱 재시작
heroku restart

# 로컬에서 Heroku 환경으로 실행
heroku local web

# 원격 콘솔 접속
heroku run bash

# 데이터베이스 콘솔
heroku pg:psql

# 앱 삭제 (주의!)
heroku apps:destroy --app your-app-name --confirm your-app-name
```

---

## 추가 리소스

- [Heroku Dev Center](https://devcenter.heroku.com/)
- [Heroku Java Support](https://devcenter.heroku.com/categories/java-support)
- [Heroku Postgres](https://devcenter.heroku.com/categories/heroku-postgres)
- [Heroku CLI Commands](https://devcenter.heroku.com/articles/heroku-cli-commands)
