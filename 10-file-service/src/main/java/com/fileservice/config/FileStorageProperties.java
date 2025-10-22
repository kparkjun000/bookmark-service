package com.fileservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    
    private String uploadDir;
    private Long maxSize;
    private List<String> allowedExtensions;
}

