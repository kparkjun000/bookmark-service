@echo off
REM Windows Setup Script for URL Shortener Project

echo ========================================
echo URL Shortener Setup Script
echo ========================================
echo.

REM Check Java
echo Checking Java installation...
java -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 21
    exit /b 1
)
echo.

REM Check Maven
echo Checking Maven installation...
mvn -version
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven 3.9.x
    exit /b 1
)
echo.

REM Check PostgreSQL (optional)
echo Checking PostgreSQL installation...
psql --version
if %ERRORLEVEL% NEQ 0 (
    echo WARNING: PostgreSQL is not installed or not in PATH
    echo You can use Docker instead or install PostgreSQL manually
)
echo.

REM Build URL Shortener Service
echo Building URL Shortener Service...
cd url-shortener-service
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to build URL Shortener Service
    exit /b 1
)
cd ..
echo URL Shortener Service built successfully!
echo.

REM Build Analytics Service
echo Building Analytics Service...
cd analytics-service
call mvn clean package -DskipTests
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Failed to build Analytics Service
    exit /b 1
)
cd ..
echo Analytics Service built successfully!
echo.

echo ========================================
echo Setup completed successfully!
echo ========================================
echo.
echo Next steps:
echo 1. Start PostgreSQL database
echo    - Use Docker: docker-compose up -d postgres
echo    - Or install PostgreSQL locally
echo.
echo 2. Start URL Shortener Service:
echo    cd url-shortener-service
echo    mvn spring-boot:run
echo.
echo 3. Start Analytics Service (in another terminal):
echo    cd analytics-service
echo    mvn spring-boot:run
echo.
echo 4. Access services:
echo    - URL Shortener: http://localhost:8080
echo    - Analytics: http://localhost:8081
echo.
echo For more information, see QUICKSTART.md
echo.

pause

