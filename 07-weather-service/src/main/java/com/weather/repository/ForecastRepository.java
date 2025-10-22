package com.weather.repository;

import com.weather.entity.Forecast;
import com.weather.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {
    
    List<Forecast> findByLocationAndForecastTimestampBetweenOrderByForecastTimestampAsc(
        Location location, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT f FROM Forecast f WHERE f.location = :location " +
           "AND f.forecastTimestamp >= :startTime " +
           "ORDER BY f.forecastTimestamp ASC")
    List<Forecast> findUpcomingForecasts(
        @Param("location") Location location, 
        @Param("startTime") LocalDateTime startTime);
    
    void deleteByForecastTimestampBefore(LocalDateTime timestamp);
    
    void deleteByLocationAndForecastTimestampBetween(
        Location location, LocalDateTime start, LocalDateTime end);
}

