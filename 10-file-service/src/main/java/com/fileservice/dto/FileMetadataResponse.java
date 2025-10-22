package com.fileservice.dto;

import com.fileservice.entity.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileMetadataResponse {
    
    private Long id;
    private String fileId;
    private String originalFileName;
    private String contentType;
    private Long fileSize;
    private String fileExtension;
    private Integer width;
    private Integer height;
    private Boolean hasThumbnail;
    private String downloadUrl;
    private String thumbnailUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String uploadedBy;
    private String description;
    private String status;
    
    public static FileMetadataResponse fromEntity(FileMetadata metadata, String baseUrl) {
        return FileMetadataResponse.builder()
                .id(metadata.getId())
                .fileId(metadata.getFileId())
                .originalFileName(metadata.getOriginalFileName())
                .contentType(metadata.getContentType())
                .fileSize(metadata.getFileSize())
                .fileExtension(metadata.getFileExtension())
                .width(metadata.getWidth())
                .height(metadata.getHeight())
                .hasThumbnail(metadata.getHasThumbnail())
                .downloadUrl(baseUrl + "/api/files/" + metadata.getFileId() + "/download")
                .thumbnailUrl(metadata.getHasThumbnail() ? 
                    baseUrl + "/api/files/" + metadata.getFileId() + "/thumbnail" : null)
                .createdAt(metadata.getCreatedAt())
                .updatedAt(metadata.getUpdatedAt())
                .uploadedBy(metadata.getUploadedBy())
                .description(metadata.getDescription())
                .status(metadata.getStatus().name())
                .build();
    }
}

