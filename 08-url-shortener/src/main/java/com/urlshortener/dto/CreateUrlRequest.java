package com.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateUrlRequest {

    @NotBlank(message = "Original URL is required")
    @Pattern(regexp = "^https?://.*", message = "URL must start with http:// or https://")
    @Size(max = 2048, message = "URL is too long")
    private String originalUrl;

    @Size(max = 50, message = "Custom alias is too long")
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$", message = "Custom alias can only contain letters, numbers, hyphens and underscores")
    private String customAlias;

    @Size(max = 200, message = "Title is too long")
    private String title;

    @Size(max = 500, message = "Description is too long")
    private String description;

    private Integer expirationDays;

    // Constructors
    public CreateUrlRequest() {}

    public CreateUrlRequest(String originalUrl, String customAlias, String title, String description, Integer expirationDays) {
        this.originalUrl = originalUrl;
        this.customAlias = customAlias;
        this.title = title;
        this.description = description;
        this.expirationDays = expirationDays;
    }

    // Getters and Setters
    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getCustomAlias() {
        return customAlias;
    }

    public void setCustomAlias(String customAlias) {
        this.customAlias = customAlias;
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

    public Integer getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(Integer expirationDays) {
        this.expirationDays = expirationDays;
    }
}

