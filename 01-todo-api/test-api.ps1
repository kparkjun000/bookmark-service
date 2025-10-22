# API 테스트 스크립트

Write-Host "Todo API 테스트 시작" -ForegroundColor Green
Write-Host "=========================" -ForegroundColor Green

# Base URL
$baseUrl = "http://localhost:8080"

# 1. 헬스 체크 (Swagger UI)
Write-Host "`n1. Swagger UI 접근 테스트" -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/swagger-ui.html" -Method GET -UseBasicParsing
    if ($response.StatusCode -eq 200) {
        Write-Host "   ✓ Swagger UI: 200 OK" -ForegroundColor Green
    }
} catch {
    Write-Host "   ✗ Swagger UI 접근 실패: $_" -ForegroundColor Red
}

# 2. 회원가입 테스트
Write-Host "`n2. 회원가입 API 테스트 (/api/auth/signup)" -ForegroundColor Yellow
$signupBody = @{
    username = "testuser"
    email = "test@example.com"
    password = "password123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/signup" `
        -Method POST `
        -Body $signupBody `
        -ContentType "application/json"
    Write-Host "   ✓ 회원가입 성공: 201 Created" -ForegroundColor Green
    Write-Host "   사용자 ID: $($response.id)" -ForegroundColor Cyan
} catch {
    if ($_.Exception.Response.StatusCode -eq 201) {
        Write-Host "   ✓ 회원가입 성공: 201 Created" -ForegroundColor Green
    } else {
        Write-Host "   ✗ 회원가입 실패: $_" -ForegroundColor Red
    }
}

# 3. 로그인 테스트
Write-Host "`n3. 로그인 API 테스트 (/api/auth/login)" -ForegroundColor Yellow
$loginBody = @{
    username = "testuser"
    password = "password123"
} | ConvertTo-Json

try {
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" `
        -Method POST `
        -Body $loginBody `
        -ContentType "application/json"
    $token = $response.token
    Write-Host "   ✓ 로그인 성공: 200 OK" -ForegroundColor Green
    Write-Host "   JWT 토큰 발급 완료" -ForegroundColor Cyan
} catch {
    Write-Host "   ✗ 로그인 실패: $_" -ForegroundColor Red
    $token = $null
}

if ($token) {
    $headers = @{
        "Authorization" = "Bearer $token"
        "Content-Type" = "application/json"
    }

    # 4. Todo 생성 테스트
    Write-Host "`n4. Todo 생성 API 테스트 (/api/todos)" -ForegroundColor Yellow
    $todoBody = @{
        title = "첫 번째 할일"
        description = "테스트 할일입니다"
        priority = "HIGH"
    } | ConvertTo-Json

    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/todos" `
            -Method POST `
            -Headers $headers `
            -Body $todoBody
        Write-Host "   ✓ Todo 생성 성공: 201 Created" -ForegroundColor Green
        $todoId = $response.id
        Write-Host "   Todo ID: $todoId" -ForegroundColor Cyan
    } catch {
        if ($_.Exception.Response.StatusCode -eq 201) {
            Write-Host "   ✓ Todo 생성 성공: 201 Created" -ForegroundColor Green
        } else {
            Write-Host "   ✗ Todo 생성 실패: $_" -ForegroundColor Red
        }
    }

    # 5. Todo 목록 조회 테스트
    Write-Host "`n5. Todo 목록 조회 API 테스트 (/api/todos)" -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/todos" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"}
        Write-Host "   ✓ Todo 목록 조회 성공: 200 OK" -ForegroundColor Green
        Write-Host "   총 Todo 개수: $($response.totalElements)" -ForegroundColor Cyan
    } catch {
        Write-Host "   ✗ Todo 목록 조회 실패: $_" -ForegroundColor Red
    }

    # 6. 통계 조회 테스트
    Write-Host "`n6. 통계 API 테스트 (/api/statistics)" -ForegroundColor Yellow
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/api/statistics" `
            -Method GET `
            -Headers @{"Authorization" = "Bearer $token"}
        Write-Host "   ✓ 통계 조회 성공: 200 OK" -ForegroundColor Green
        Write-Host "   총 Todo: $($response.totalTodos), 완료: $($response.completedTodos)" -ForegroundColor Cyan
    } catch {
        Write-Host "   ✗ 통계 조회 실패: $_" -ForegroundColor Red
    }
}

Write-Host "`n=========================" -ForegroundColor Green
Write-Host "테스트 완료" -ForegroundColor Green