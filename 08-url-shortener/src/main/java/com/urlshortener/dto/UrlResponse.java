package com.urlshortener.dto;

import com.urlshortener.entity.ShortenedUrl;

import java.time.LocalDateTime;

public class UrlResponse {

    private Long id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Long clickCount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    private String title;
    private String description;

    // Constructors
    public UrlResponse() {}

    public UrlResponse(Long id, String shortCode, String shortUrl, String originalUrl, Long clickCount,
                      LocalDateTime createdAt, LocalDateTime expiresAt, Boolean isActive,
                      String title, String description) {
        this.id = id;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.isActive = isActive;
        this.title = title;
        this.description = description;
    }

    public static UrlResponse from(ShortenedUrl url, String baseUrl) {
        return new UrlResponse(
                url.getId(),
                url.getShortCode(),
                baseUrl + "/" + url.getShortCode(),
                url.getOriginalUrl(),
                url.getClickCount(),
                url.getCreatedAt(),
                url.getExpiresAt(),
                url.getIsActive(),
                url.getTitle(),
                url.getDescription()
        );
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }

    public void setClickCount(Long clickCount) {
        this.clickCount = clickCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
