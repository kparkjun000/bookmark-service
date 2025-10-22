package com.urlshortener.dto;

import java.time.LocalDateTime;

public class TimelineDataPoint {
    private LocalDateTime timestamp;
    private long clicks;

    public TimelineDataPoint() {}

    public TimelineDataPoint(LocalDateTime timestamp, long clicks) {
        this.timestamp = timestamp;
        this.clicks = clicks;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }
}