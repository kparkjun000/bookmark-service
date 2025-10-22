package com.urlshortener.service;

import com.urlshortener.dto.CreateUrlRequest;
import com.urlshortener.entity.ClickEvent;
import com.urlshortener.entity.ShortenedUrl;
import com.urlshortener.exception.CustomAliasAlreadyExistsException;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.repository.ClickEventRepository;
import com.urlshortener.repository.ShortenedUrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UrlShortenerService {

    private static final Logger log = LoggerFactory.getLogger(UrlShortenerService.class);

    private final ShortenedUrlRepository urlRepository;
    private final ClickEventRepository clickEventRepository;
    private final HashService hashService;
    private final UserAgentParser userAgentParser;

    public UrlShortenerService(ShortenedUrlRepository urlRepository, 
                              ClickEventRepository clickEventRepository,
                              HashService hashService,
                              UserAgentParser userAgentParser) {
        this.urlRepository = urlRepository;
        this.clickEventRepository = clickEventRepository;
        this.hashService = hashService;
        this.userAgentParser = userAgentParser;
    }

    @Value("${app.default-expiration-days:365}")
    private int defaultExpirationDays;

    private static final int MAX_COLLISION_ATTEMPTS = 10;

    /**
     * Create a shortened URL
     */
    @Transactional
    public ShortenedUrl createShortenedUrl(CreateUrlRequest request) {
        String shortCode;

        // Check if custom alias is provided
        if (request.getCustomAlias() != null && !request.getCustomAlias().isEmpty()) {
            if (urlRepository.existsByCustomAlias(request.getCustomAlias())) {
                throw new CustomAliasAlreadyExistsException("Custom alias already exists: " + request.getCustomAlias());
            }
            shortCode = request.getCustomAlias();
        } else {
            // Generate short code with collision resolution
            shortCode = generateUniqueShortCode(request.getOriginalUrl());
        }

        // Calculate expiration date
        LocalDateTime expiresAt = null;
        if (request.getExpirationDays() != null && request.getExpirationDays() > 0) {
            expiresAt = LocalDateTime.now().plusDays(request.getExpirationDays());
        } else {
            expiresAt = LocalDateTime.now().plusDays(defaultExpirationDays);
        }

        // Create and save the shortened URL
        ShortenedUrl shortenedUrl = new ShortenedUrl();
        shortenedUrl.setShortCode(shortCode);
        shortenedUrl.setOriginalUrl(request.getOriginalUrl());
        shortenedUrl.setCustomAlias(request.getCustomAlias());
        shortenedUrl.setTitle(request.getTitle());
        shortenedUrl.setDescription(request.getDescription());
        shortenedUrl.setExpiresAt(expiresAt);
        shortenedUrl.setClickCount(0L);
        shortenedUrl.setIsActive(true);

        return urlRepository.save(shortenedUrl);
    }

    /**
     * Generate a unique short code with collision resolution
     */
    private String generateUniqueShortCode(String originalUrl) {
        for (int attempt = 0; attempt < MAX_COLLISION_ATTEMPTS; attempt++) {
            String shortCode = hashService.generateShortCode(originalUrl, attempt);
            
            if (!urlRepository.existsByShortCode(shortCode)) {
                return shortCode;
            }
            
            log.warn("Collision detected for short code: {}. Attempt: {}", shortCode, attempt + 1);
        }

        throw new RuntimeException("Failed to generate unique short code after " + MAX_COLLISION_ATTEMPTS + " attempts");
    }

    /**
     * Get shortened URL by short code
     */
    @Transactional(readOnly = true)
    public ShortenedUrl getShortenedUrl(String shortCode) {
        return urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));
    }

    /**
     * Get original URL and record click event
     */
    @Transactional
    public String getOriginalUrlAndRecordClick(String shortCode, HttpServletRequest request) {
        ShortenedUrl shortenedUrl = getShortenedUrl(shortCode);

        if (!shortenedUrl.isAccessible()) {
            if (shortenedUrl.isExpired()) {
                throw new UrlNotFoundException("URL has expired: " + shortCode);
            } else {
                throw new UrlNotFoundException("URL is not active: " + shortCode);
            }
        }

        // Increment click count
        shortenedUrl.incrementClickCount();
        urlRepository.save(shortenedUrl);

        // Record click event asynchronously
        recordClickEventAsync(shortCode, request);

        return shortenedUrl.getOriginalUrl();
    }

    /**
     * Record click event asynchronously
     */
    @Async
    public void recordClickEventAsync(String shortCode, HttpServletRequest request) {
        try {
            String ipAddress = getClientIpAddress(request);
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setShortCode(shortCode);
            clickEvent.setIpAddress(ipAddress);
            clickEvent.setUserAgent(userAgent);
            clickEvent.setReferer(referer);
            clickEvent.setBrowser(userAgentParser.parseBrowser(userAgent));
            clickEvent.setOperatingSystem(userAgentParser.parseOperatingSystem(userAgent));
            clickEvent.setDeviceType(userAgentParser.parseDeviceType(userAgent));

            clickEventRepository.save(clickEvent);
            log.info("Click event recorded for short code: {}", shortCode);
        } catch (Exception e) {
            log.error("Failed to record click event for short code: {}", shortCode, e);
        }
    }

    /**
     * Get client IP address
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String[] headers = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
        };

        for (String header : headers) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                // Get first IP if multiple IPs are present
                if (ip.contains(",")) {
                    ip = ip.split(",")[0];
                }
                return ip.trim();
            }
        }

        return request.getRemoteAddr();
    }

    /**
     * Delete shortened URL
     */
    @Transactional
    public void deleteShortenedUrl(String shortCode) {
        ShortenedUrl shortenedUrl = getShortenedUrl(shortCode);
        shortenedUrl.setIsActive(false);
        urlRepository.save(shortenedUrl);
    }

    /**
     * Get all URLs
     */
    @Transactional(readOnly = true)
    public List<ShortenedUrl> getAllUrls() {
        return urlRepository.findAll();
    }

    /**
     * Get click events for a short code
     */
    @Transactional(readOnly = true)
    public List<ClickEvent> getClickEvents(String shortCode) {
        return clickEventRepository.findByShortCodeOrderByClickedAtDesc(shortCode);
    }
}

