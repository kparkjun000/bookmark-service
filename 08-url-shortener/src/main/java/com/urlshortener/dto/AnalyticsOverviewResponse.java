package com.urlshortener.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AnalyticsOverviewResponse {
    private String shortCode;
    private String originalUrl;
    private long totalClicks;
    private long clicksToday;
    private long clicksThisWeek;
    private long clicksThisMonth;
    private Map<String, Long> browserStats;
    private Map<String, Long> osStats;
    private Map<String, Long> deviceStats;
    private Map<String, Long> locationStats;
    private List<TimelineDataPoint> recentClicks;

    public AnalyticsOverviewResponse() {}

    public AnalyticsOverviewResponse(String shortCode, String originalUrl, long totalClicks, 
                                   long clicksToday, long clicksThisWeek, long clicksThisMonth,
                                   Map<String, Long> browserStats, Map<String, Long> osStats,
                                   Map<String, Long> deviceStats, Map<String, Long> locationStats,
                                   List<TimelineDataPoint> recentClicks) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.totalClicks = totalClicks;
        this.clicksToday = clicksToday;
        this.clicksThisWeek = clicksThisWeek;
        this.clicksThisMonth = clicksThisMonth;
        this.browserStats = browserStats;
        this.osStats = osStats;
        this.deviceStats = deviceStats;
        this.locationStats = locationStats;
        this.recentClicks = recentClicks;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public long getClicksToday() {
        return clicksToday;
    }

    public void setClicksToday(long clicksToday) {
        this.clicksToday = clicksToday;
    }

    public long getClicksThisWeek() {
        return clicksThisWeek;
    }

    public void setClicksThisWeek(long clicksThisWeek) {
        this.clicksThisWeek = clicksThisWeek;
    }

    public long getClicksThisMonth() {
        return clicksThisMonth;
    }

    public void setClicksThisMonth(long clicksThisMonth) {
        this.clicksThisMonth = clicksThisMonth;
    }

    public Map<String, Long> getBrowserStats() {
        return browserStats;
    }

    public void setBrowserStats(Map<String, Long> browserStats) {
        this.browserStats = browserStats;
    }

    public Map<String, Long> getOsStats() {
        return osStats;
    }

    public void setOsStats(Map<String, Long> osStats) {
        this.osStats = osStats;
    }

    public Map<String, Long> getDeviceStats() {
        return deviceStats;
    }

    public void setDeviceStats(Map<String, Long> deviceStats) {
        this.deviceStats = deviceStats;
    }

    public Map<String, Long> getLocationStats() {
        return locationStats;
    }

    public void setLocationStats(Map<String, Long> locationStats) {
        this.locationStats = locationStats;
    }

    public List<TimelineDataPoint> getRecentClicks() {
        return recentClicks;
    }

    public void setRecentClicks(List<TimelineDataPoint> recentClicks) {
        this.recentClicks = recentClicks;
    }
}
