package com.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청 정보")
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    @Schema(description = "사용자 아이디", example = "johndoe")
    private String username;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "비밀번호", example = "password123")
    private String password;
}


