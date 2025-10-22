package com.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "JWT 토큰 응답 정보")
public class JwtResponse {
    
    @Schema(description = "액세스 토큰")
    private String accessToken;
    
    @Schema(description = "리프레시 토큰")
    private String refreshToken;
    
    @Builder.Default
    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType = "Bearer";
    
    @Schema(description = "사용자 아이디", example = "johndoe")
    private String username;
    
    @Schema(description = "이메일 주소", example = "john@example.com")
    private String email;
    
    @Schema(description = "사용자 권한 목록", example = "[\"ROLE_USER\"]")
    private Set<String> roles;
}


