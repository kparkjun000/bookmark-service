package com.auth.dto;

import com.auth.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 정보 응답")
public class UserResponse {
    
    @Schema(description = "사용자 ID", example = "1")
    private Long id;
    
    @Schema(description = "사용자 아이디", example = "johndoe")
    private String username;
    
    @Schema(description = "이메일 주소", example = "john@example.com")
    private String email;
    
    @Schema(description = "사용자 권한 목록", example = "[\"USER\"]")
    private Set<Role> roles;
    
    @Schema(description = "계정 활성화 여부", example = "true")
    private Boolean enabled;
    
    @Schema(description = "생성 일시")
    private LocalDateTime createdAt;
    
    @Schema(description = "수정 일시")
    private LocalDateTime updatedAt;
}


