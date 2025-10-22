package com.weather.repository;

import com.weather.entity.Location;
import com.weather.entity.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    
    Optional<WeatherData> findTopByLocationOrderByTimestampDesc(Location location);
    
    List<WeatherData> findByLocationAndTimestampBetweenOrderByTimestampDesc(
        Location location, LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT w FROM WeatherData w WHERE w.location = :location " +
           "AND w.timestamp >= :startTime ORDER BY w.timestamp DESC")
    List<WeatherData> findRecentByLocation(
        @Param("location") Location location, 
        @Param("startTime") LocalDateTime startTime);
    
    void deleteByTimestampBefore(LocalDateTime timestamp);
}

