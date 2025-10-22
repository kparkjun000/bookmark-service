package com.urlshortener.service;

import com.urlshortener.dto.AnalyticsOverviewResponse;
import com.urlshortener.dto.TimelineDataPoint;
import com.urlshortener.dto.TimelineResponse;
import com.urlshortener.entity.ClickEvent;
import com.urlshortener.entity.ShortenedUrl;
import com.urlshortener.exception.UrlNotFoundException;
import com.urlshortener.repository.ClickEventRepository;
import com.urlshortener.repository.ShortenedUrlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsService.class);

    private final ClickEventRepository clickEventRepository;
    private final ShortenedUrlRepository shortenedUrlRepository;

    public AnalyticsService(ClickEventRepository clickEventRepository, ShortenedUrlRepository shortenedUrlRepository) {
        this.clickEventRepository = clickEventRepository;
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    public AnalyticsOverviewResponse getAnalyticsOverview(String shortCode) {
        log.info("Getting analytics overview for: {}", shortCode);
        
        ShortenedUrl shortenedUrl = shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime todayStart = now.toLocalDate().atStartOfDay();
        LocalDateTime weekStart = now.minusWeeks(1);
        LocalDateTime monthStart = now.minusMonths(1);

        long totalClicks = clickEventRepository.countByShortCode(shortCode);
        long clicksToday = clickEventRepository.countClicksSince(shortCode, todayStart);
        long clicksThisWeek = clickEventRepository.countClicksSince(shortCode, weekStart);
        long clicksThisMonth = clickEventRepository.countClicksSince(shortCode, monthStart);

        Map<String, Long> browserStats = getBrowserStats(shortCode);
        Map<String, Long> osStats = getOperatingSystemStats(shortCode);
        Map<String, Long> deviceStats = getDeviceTypeStats(shortCode);
        Map<String, Long> locationStats = getLocationStats(shortCode);

        List<TimelineDataPoint> recentClicks = getTimelineStats(shortCode, "hourly", 24).getData();

        return new AnalyticsOverviewResponse(
                shortCode,
                shortenedUrl.getOriginalUrl(),
                totalClicks,
                clicksToday,
                clicksThisWeek,
                clicksThisMonth,
                browserStats,
                osStats,
                deviceStats,
                locationStats,
                recentClicks
        );
    }

    public TimelineResponse getTimelineStats(String shortCode, String granularity, int days) {
        log.info("Getting timeline stats for: {} with granularity: {} and days: {}", shortCode, granularity, days);
        
        shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days);

        List<Object[]> results;
        if ("hourly".equals(granularity)) {
            results = clickEventRepository.getHourlyStats(shortCode, startTime, endTime);
        } else {
            results = clickEventRepository.getDailyStats(shortCode, startTime, endTime);
        }

        List<TimelineDataPoint> dataPoints = results.stream()
                .map(result -> {
                    LocalDateTime timestamp = (LocalDateTime) result[0];
                    Long clicks = ((Number) result[1]).longValue();
                    return new TimelineDataPoint(timestamp, clicks);
                })
                .collect(Collectors.toList());

        return new TimelineResponse(shortCode, granularity, dataPoints);
    }

    public Map<String, Long> getBrowserStats(String shortCode) {
        log.info("Getting browser stats for: {}", shortCode);
        
        shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        return clickEventRepository.getBrowserStats(shortCode)
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> ((Number) result[1]).longValue()
                ));
    }

    public Map<String, Long> getOperatingSystemStats(String shortCode) {
        log.info("Getting OS stats for: {}", shortCode);
        
        shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        return clickEventRepository.getOperatingSystemStats(shortCode)
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> ((Number) result[1]).longValue()
                ));
    }

    public Map<String, Long> getDeviceTypeStats(String shortCode) {
        log.info("Getting device stats for: {}", shortCode);
        
        shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        return clickEventRepository.getDeviceTypeStats(shortCode)
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> ((Number) result[1]).longValue()
                ));
    }

    public Map<String, Long> getLocationStats(String shortCode) {
        log.info("Getting location stats for: {}", shortCode);
        
        shortenedUrlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("URL not found: " + shortCode));

        return clickEventRepository.getLocationStats(shortCode)
                .stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> ((Number) result[1]).longValue()
                ));
    }
}
