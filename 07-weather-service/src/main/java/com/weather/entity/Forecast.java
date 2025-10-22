package com.weather.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "forecasts", indexes = {
    @Index(name = "idx_forecast_location_timestamp", columnList = "location_id,forecast_timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(name = "forecast_timestamp", nullable = false)
    private LocalDateTime forecastTimestamp;

    @Column(nullable = false)
    private Double temperature;

    @Column(name = "feels_like")
    private Double feelsLike;

    @Column(name = "temp_min")
    private Double tempMin;

    @Column(name = "temp_max")
    private Double tempMax;

    private Integer pressure;

    private Integer humidity;

    @Column(name = "wind_speed")
    private Double windSpeed;

    @Column(name = "wind_direction")
    private Integer windDirection;

    private Integer cloudiness;

    @Column(name = "weather_main")
    private String weatherMain;

    @Column(name = "weather_description")
    private String weatherDescription;

    @Column(name = "weather_icon")
    private String weatherIcon;

    private Double visibility;

    @Column(name = "pop") // Probability of precipitation
    private Double pop;

    @Column(name = "rain_3h")
    private Double rain3h;

    @Column(name = "snow_3h")
    private Double snow3h;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

