@echo off
echo =================================
echo Todo API 빌드 및 실행
echo =================================

REM Java 21 확인
echo Java 버전 확인...
java -version

REM Maven Wrapper 다운로드 확인
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Maven Wrapper 다운로드 중...
    mkdir .mvn\wrapper
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

REM 프로젝트 빌드
echo.
echo 프로젝트 빌드 중...
call mvnw.cmd clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo 빌드 실패!
    exit /b %ERRORLEVEL%
)

REM PostgreSQL 확인 메시지
echo.
echo =================================
echo PostgreSQL 설정 확인
echo =================================
echo 데이터베이스: todoapi
echo 사용자: %USERNAME%
echo 포트: 5432
echo.
echo PostgreSQL이 실행 중인지 확인하세요!
echo =================================
pause

REM 애플리케이션 실행
echo.
echo 애플리케이션 실행 중...
call mvnw.cmd spring-boot:run

pause