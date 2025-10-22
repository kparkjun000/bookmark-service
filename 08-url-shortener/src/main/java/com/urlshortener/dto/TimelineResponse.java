package com.urlshortener.dto;

import java.util.List;

public class TimelineResponse {
    private String shortCode;
    private String granularity;
    private List<TimelineDataPoint> data;

    public TimelineResponse() {}

    public TimelineResponse(String shortCode, String granularity, List<TimelineDataPoint> data) {
        this.shortCode = shortCode;
        this.granularity = granularity;
        this.data = data;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public String getGranularity() {
        return granularity;
    }

    public void setGranularity(String granularity) {
        this.granularity = granularity;
    }

    public List<TimelineDataPoint> getData() {
        return data;
    }

    public void setData(List<TimelineDataPoint> data) {
        this.data = data;
    }
}