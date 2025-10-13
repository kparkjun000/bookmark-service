package com.zerobase.bookmarkservice.dto;

import com.zerobase.bookmarkservice.entity.Bookmark;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookmarkDto {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "북마크 생성 요청")
    public static class Create {
        
        @NotBlank(message = "URL은 필수입니다")
        @Size(max = 2048, message = "URL은 2048자를 초과할 수 없습니다")
        @Schema(description = "북마크 URL", example = "https://www.example.com")
        private String url;
        
        @Size(max = 500, message = "제목은 500자를 초과할 수 없습니다")
        @Schema(description = "북마크 제목 (메타데이터에서 자동 추출 가능)", example = "Example Domain")
        private String title;
        
        @Size(max = 2000, message = "설명은 2000자를 초과할 수 없습니다")
        @Schema(description = "북마크 설명 (메타데이터에서 자동 추출 가능)", example = "This domain is for use in illustrative examples")
        private String description;
        
        @Schema(description = "공개 여부", example = "false")
        private Boolean isPublic;
        
        @Schema(description = "즐겨찾기 여부", example = "false")
        private Boolean isFavorite;
        
        @Schema(description = "태그 이름 목록", example = "[\"개발\", \"참고자료\"]")
        private Set<String> tagNames;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "북마크 수정 요청")
    public static class Update {
        
        @Size(max = 500, message = "제목은 500자를 초과할 수 없습니다")
        @Schema(description = "북마크 제목", example = "Updated Title")
        private String title;
        
        @Size(max = 2000, message = "설명은 2000자를 초과할 수 없습니다")
        @Schema(description = "북마크 설명", example = "Updated Description")
        private String description;
        
        @Schema(description = "공개 여부", example = "true")
        private Boolean isPublic;
        
        @Schema(description = "즐겨찾기 여부", example = "true")
        private Boolean isFavorite;
        
        @Schema(description = "태그 이름 목록", example = "[\"개발\", \"중요\"]")
        private Set<String> tagNames;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "북마크 응답")
    public static class Response {
        
        @Schema(description = "북마크 ID", example = "1")
        private Long id;
        
        @Schema(description = "북마크 URL", example = "https://www.example.com")
        private String url;
        
        @Schema(description = "북마크 제목", example = "Example Domain")
        private String title;
        
        @Schema(description = "북마크 설명", example = "This domain is for use in illustrative examples")
        private String description;
        
        @Schema(description = "이미지 URL", example = "https://www.example.com/image.jpg")
        private String imageUrl;
        
        @Schema(description = "사이트 이름", example = "Example")
        private String siteName;
        
        @Schema(description = "공개 여부", example = "false")
        private Boolean isPublic;
        
        @Schema(description = "즐겨찾기 여부", example = "false")
        private Boolean isFavorite;
        
        @Schema(description = "태그 목록")
        private List<TagDto.Response> tags;
        
        @Schema(description = "생성일시")
        private LocalDateTime createdAt;
        
        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;
        
        public static Response from(Bookmark bookmark) {
            return Response.builder()
                    .id(bookmark.getId())
                    .url(bookmark.getUrl())
                    .title(bookmark.getTitle())
                    .description(bookmark.getDescription())
                    .imageUrl(bookmark.getImageUrl())
                    .siteName(bookmark.getSiteName())
                    .isPublic(bookmark.getIsPublic())
                    .isFavorite(bookmark.getIsFavorite())
                    .tags(bookmark.getTags().stream()
                            .map(TagDto.Response::from)
                            .collect(Collectors.toList()))
                    .createdAt(bookmark.getCreatedAt())
                    .updatedAt(bookmark.getUpdatedAt())
                    .build();
        }
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "메타데이터 응답")
    public static class MetadataResponse {
        
        @Schema(description = "URL", example = "https://www.example.com")
        private String url;
        
        @Schema(description = "제목", example = "Example Domain")
        private String title;
        
        @Schema(description = "설명", example = "This domain is for use in illustrative examples")
        private String description;
        
        @Schema(description = "이미지 URL", example = "https://www.example.com/image.jpg")
        private String imageUrl;
        
        @Schema(description = "사이트 이름", example = "Example")
        private String siteName;
    }
}

