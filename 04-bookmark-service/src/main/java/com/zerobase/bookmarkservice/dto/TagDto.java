package com.zerobase.bookmarkservice.dto;

import com.zerobase.bookmarkservice.entity.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

public class TagDto {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "태그 생성 요청")
    public static class Create {
        
        @NotBlank(message = "태그 이름은 필수입니다")
        @Size(max = 50, message = "태그 이름은 50자를 초과할 수 없습니다")
        @Schema(description = "태그 이름", example = "개발")
        private String name;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "태그 응답")
    public static class Response {
        
        @Schema(description = "태그 ID", example = "1")
        private Long id;
        
        @Schema(description = "태그 이름", example = "개발")
        private String name;
        
        @Schema(description = "북마크 개수", example = "5")
        private Integer bookmarkCount;
        
        @Schema(description = "생성일시")
        private LocalDateTime createdAt;
        
        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;
        
        public static Response from(Tag tag) {
            return Response.builder()
                    .id(tag.getId())
                    .name(tag.getName())
                    .bookmarkCount(tag.getBookmarks().size())
                    .createdAt(tag.getCreatedAt())
                    .updatedAt(tag.getUpdatedAt())
                    .build();
        }
    }
}

