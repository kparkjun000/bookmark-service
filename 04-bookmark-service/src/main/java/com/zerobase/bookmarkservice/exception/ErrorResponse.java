package com.zerobase.bookmarkservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "에러 응답")
public class ErrorResponse {
    
    @Schema(description = "에러 코드", example = "USER_NOT_FOUND")
    private String code;
    
    @Schema(description = "에러 메시지", example = "사용자를 찾을 수 없습니다")
    private String message;
    
    @Schema(description = "에러 발생 시간")
    private LocalDateTime timestamp;
    
    @Schema(description = "필드 에러 목록")
    private List<FieldErrorDetail> fieldErrors;
    
    public static ErrorResponse of(ErrorCode errorCode) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .fieldErrors(new ArrayList<>())
                .build();
    }
    
    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return ErrorResponse.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .timestamp(LocalDateTime.now())
                .fieldErrors(fieldErrors.stream()
                        .map(FieldErrorDetail::of)
                        .toList())
                .build();
    }
    
    @Getter
    @Builder
    @AllArgsConstructor
    @Schema(description = "필드 에러 상세")
    public static class FieldErrorDetail {
        
        @Schema(description = "필드 이름", example = "email")
        private String field;
        
        @Schema(description = "에러 메시지", example = "이메일은 필수입니다")
        private String message;
        
        public static FieldErrorDetail of(FieldError fieldError) {
            return FieldErrorDetail.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
        }
    }
}

