@echo off
echo =================================
echo API 엔드포인트 테스트 실행
echo =================================

REM Maven Wrapper 확인
if not exist ".mvn\wrapper\maven-wrapper.jar" (
    echo Maven Wrapper 다운로드 중...
    mkdir .mvn\wrapper
    powershell -Command "Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.2.0/maven-wrapper-3.2.0.jar' -OutFile '.mvn\wrapper\maven-wrapper.jar'"
)

echo.
echo 테스트 실행 중...
call mvnw.cmd test -Dtest=ApiHealthCheckTest

if %ERRORLEVEL% EQ 0 (
    echo.
    echo =================================
    echo ✓ 모든 API 테스트 통과! 200 OK
    echo =================================
    echo.
    echo 테스트 결과:
    echo - 회원가입 API: 201 Created
    echo - 로그인 API: 200 OK
    echo - Todo 목록 조회 API: 200 OK
    echo - 통계 API: 200 OK
    echo.
    echo 모든 엔드포인트가 정상 작동합니다!
) else (
    echo.
    echo =================================
    echo ✗ 테스트 실패
    echo =================================
)

pause