package com.zerobase.bookmarkservice.dto;

import com.zerobase.bookmarkservice.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

public class UserDto {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자 생성 요청")
    public static class Create {
        
        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "유효한 이메일 형식이어야 합니다")
        @Schema(description = "사용자 이메일", example = "user@example.com")
        private String email;
        
        @NotBlank(message = "이름은 필수입니다")
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자 응답")
    public static class Response {
        
        @Schema(description = "사용자 ID", example = "1")
        private Long id;
        
        @Schema(description = "사용자 이메일", example = "user@example.com")
        private String email;
        
        @Schema(description = "사용자 이름", example = "홍길동")
        private String name;
        
        @Schema(description = "생성일시")
        private LocalDateTime createdAt;
        
        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;
        
        public static Response from(User user) {
            return Response.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        }
    }
}

