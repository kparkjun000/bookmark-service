package com.fileservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "image")
public class ImageProcessingProperties {
    
    private Thumbnail thumbnail = new Thumbnail();
    private Resize resize = new Resize();
    
    @Data
    public static class Thumbnail {
        private Integer width = 200;
        private Integer height = 200;
    }
    
    @Data
    public static class Resize {
        private Integer maxWidth = 1920;
        private Integer maxHeight = 1080;
    }
}

