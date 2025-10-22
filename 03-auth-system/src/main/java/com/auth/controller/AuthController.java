package com.auth.controller;

import com.auth.dto.*;
import com.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "회원가입, 로그인, 토큰 관리 등 인증 관련 API")
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 (중복된 사용자명 또는 이메일)",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.auth.dto.ApiResponse<JwtResponse>> signup(
            @Valid @RequestBody SignupRequest signupRequest) {
        com.auth.dto.ApiResponse<JwtResponse> response = authService.signup(signupRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 및 JWT 토큰 발급")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
                content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패 (잘못된 사용자명 또는 비밀번호)",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.auth.dto.ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest loginRequest) {
        com.auth.dto.ApiResponse<JwtResponse> response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "리프레시 토큰을 사용하여 새로운 액세스 토큰 발급")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토큰 갱신 성공",
                content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "유효하지 않거나 만료된 리프레시 토큰",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.auth.dto.ApiResponse<JwtResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        com.auth.dto.ApiResponse<JwtResponse> response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 및 리프레시 토큰 삭제")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                content = @Content(schema = @Schema(implementation = com.auth.dto.ApiResponse.class)))
    })
    public ResponseEntity<com.auth.dto.ApiResponse<Void>> logout(
            @AuthenticationPrincipal UserDetails userDetails) {
        com.auth.dto.ApiResponse<Void> response = authService.logout(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }
}


