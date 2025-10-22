package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherResponse {

    private Long id;
    private LocationDto location;
    private LocalDateTime timestamp;
    private Double temperature;
    private Double feelsLike;
    private Double tempMin;
    private Double tempMax;
    private Integer pressure;
    private Integer humidity;
    private Double windSpeed;
    private Integer windDirection;
    private Integer cloudiness;
    private String weatherMain;
    private String weatherDescription;
    private String weatherIcon;
    private Double visibility;
    private Double rain1h;
    private Double snow1h;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LocationDto {
        private Long id;
        private String city;
        private String country;
        private String countryCode;
        private Double latitude;
        private Double longitude;
    }
}

