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
@Schema(description = "리프레시 토큰 요청 정보")
public class RefreshTokenRequest {
    
    @NotBlank(message = "Refresh token is required")
    @Schema(description = "리프레시 토큰")
    private String refreshToken;
}


