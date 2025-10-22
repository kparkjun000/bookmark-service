package com.weather.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForecastResponse {

    private LocationDto location;
    private List<ForecastItemDto> forecasts;

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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ForecastItemDto {
        private Long id;
        private LocalDateTime forecastTimestamp;
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
        private Double pop;
        private Double rain3h;
        private Double snow3h;
    }
}

