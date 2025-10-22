package com.weather.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data", indexes = {
    @Index(name = "idx_location_timestamp", columnList = "location_id,timestamp")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column(nullable = false)
    private LocalDateTime timestamp;

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

    @Column(name = "rain_1h")
    private Double rain1h;

    @Column(name = "snow_1h")
    private Double snow1h;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

