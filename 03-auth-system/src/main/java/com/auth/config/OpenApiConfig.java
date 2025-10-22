package com.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "JWT Authentication & Authorization API",
        version = "1.0.0",
        description = """
            JWT 기반 인증 및 권한 관리 RESTful API 시스템
            
            ## 주요 기능
            * 회원가입 및 로그인
            * JWT 액세스 토큰 및 리프레시 토큰 관리
            * 역할 기반 접근 제어 (RBAC)
            * 사용자 관리 (조회, 수정, 삭제)
            
            ## 권한 레벨
            * **USER**: 일반 사용자 권한
            * **MODERATOR**: 중간 관리자 권한
            * **ADMIN**: 최고 관리자 권한
            
            ## 인증 방법
            1. `/api/auth/signup` 또는 `/api/auth/login`으로 JWT 토큰 발급
            2. 요청 헤더에 `Authorization: Bearer {token}` 형식으로 토큰 포함
            3. 토큰 만료 시 `/api/auth/refresh`로 새 토큰 발급
            """,
        contact = @Contact(
            name = "API Support",
            email = "support@example.com"
        )
    ),
    servers = {
        @Server(url = "/", description = "Default Server URL")
    }
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT 토큰을 입력하세요. 예: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
)
public class OpenApiConfig {
}


