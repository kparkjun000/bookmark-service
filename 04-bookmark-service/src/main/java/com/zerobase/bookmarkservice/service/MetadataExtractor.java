package com.zerobase.bookmarkservice.service;

import com.zerobase.bookmarkservice.dto.BookmarkDto;
import com.zerobase.bookmarkservice.exception.CustomException;
import com.zerobase.bookmarkservice.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MetadataExtractor {
    
    private static final int TIMEOUT = 10000; // 10 seconds
    
    public BookmarkDto.MetadataResponse extractMetadata(String url) {
        try {
            log.info("Extracting metadata from URL: {}", url);
            
            Document document = Jsoup.connect(url)
                    .timeout(TIMEOUT)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .get();
            
            String title = extractTitle(document);
            String description = extractDescription(document);
            String imageUrl = extractImage(document, url);
            String siteName = extractSiteName(document, url);
            
            return BookmarkDto.MetadataResponse.builder()
                    .url(url)
                    .title(title)
                    .description(description)
                    .imageUrl(imageUrl)
                    .siteName(siteName)
                    .build();
                    
        } catch (IOException e) {
            log.error("Failed to extract metadata from URL: {}", url, e);
            throw new CustomException(ErrorCode.METADATA_EXTRACTION_FAILED, 
                    "URL에서 메타데이터를 추출하는데 실패했습니다: " + e.getMessage());
        }
    }
    
    private String extractTitle(Document document) {
        // Try Open Graph title
        String title = document.select("meta[property=og:title]").attr("content");
        
        // Try Twitter title
        if (title.isEmpty()) {
            title = document.select("meta[name=twitter:title]").attr("content");
        }
        
        // Try standard title tag
        if (title.isEmpty()) {
            title = document.title();
        }
        
        return title.isEmpty() ? null : title.trim();
    }
    
    private String extractDescription(Document document) {
        // Try Open Graph description
        String description = document.select("meta[property=og:description]").attr("content");
        
        // Try Twitter description
        if (description.isEmpty()) {
            description = document.select("meta[name=twitter:description]").attr("content");
        }
        
        // Try standard description meta tag
        if (description.isEmpty()) {
            description = document.select("meta[name=description]").attr("content");
        }
        
        return description.isEmpty() ? null : description.trim();
    }
    
    private String extractImage(Document document, String baseUrl) {
        // Try Open Graph image
        String imageUrl = document.select("meta[property=og:image]").attr("content");
        
        // Try Twitter image
        if (imageUrl.isEmpty()) {
            imageUrl = document.select("meta[name=twitter:image]").attr("content");
        }
        
        // Try to make relative URLs absolute
        if (!imageUrl.isEmpty() && !imageUrl.startsWith("http")) {
            try {
                imageUrl = new java.net.URL(new java.net.URL(baseUrl), imageUrl).toString();
            } catch (Exception e) {
                log.warn("Failed to convert relative image URL to absolute: {}", imageUrl);
            }
        }
        
        return imageUrl.isEmpty() ? null : imageUrl.trim();
    }
    
    private String extractSiteName(Document document, String url) {
        // Try Open Graph site name
        String siteName = document.select("meta[property=og:site_name]").attr("content");
        
        // Try to extract from domain
        if (siteName.isEmpty()) {
            try {
                java.net.URL urlObj = new java.net.URL(url);
                siteName = urlObj.getHost().replaceAll("^www\\.", "");
            } catch (Exception e) {
                log.warn("Failed to extract site name from URL: {}", url);
            }
        }
        
        return siteName.isEmpty() ? null : siteName.trim();
    }
}

