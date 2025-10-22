package com.fileservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileUploadResponse {
    
    private String fileId;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String downloadUrl;
    private String thumbnailUrl;
    private Integer width;
    private Integer height;
    private LocalDateTime uploadedAt;
    private String message;
}

