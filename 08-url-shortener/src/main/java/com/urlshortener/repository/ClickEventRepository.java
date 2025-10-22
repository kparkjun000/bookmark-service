package com.urlshortener.repository;

import com.urlshortener.entity.ClickEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent, Long> {

    long countByShortCode(String shortCode);

    List<ClickEvent> findByShortCodeOrderByClickedAtDesc(String shortCode);

    @Query("SELECT c FROM ClickEvent c WHERE c.shortCode = :shortCode AND c.clickedAt BETWEEN :startDate AND :endDate")
    List<ClickEvent> findByShortCodeAndDateRange(
            @Param("shortCode") String shortCode,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT COUNT(c) FROM ClickEvent c WHERE c.shortCode = :shortCode AND c.clickedAt >= :since")
    long countByShortCodeSince(@Param("shortCode") String shortCode, @Param("since") LocalDateTime since);

    @Query("SELECT COUNT(c) FROM ClickEvent c WHERE c.shortCode = :shortCode AND c.clickedAt >= :since")
    long countClicksSince(@Param("shortCode") String shortCode, @Param("since") LocalDateTime since);

    @Query("SELECT DATE_TRUNC('hour', ce.clickedAt) as hour, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "AND ce.clickedAt BETWEEN :startTime AND :endTime " +
           "GROUP BY DATE_TRUNC('hour', ce.clickedAt) " +
           "ORDER BY hour")
    List<Object[]> getHourlyStats(@Param("shortCode") String shortCode, 
                                 @Param("startTime") LocalDateTime startTime, 
                                 @Param("endTime") LocalDateTime endTime);

    @Query("SELECT DATE_TRUNC('day', ce.clickedAt) as day, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "AND ce.clickedAt BETWEEN :startTime AND :endTime " +
           "GROUP BY DATE_TRUNC('day', ce.clickedAt) " +
           "ORDER BY day")
    List<Object[]> getDailyStats(@Param("shortCode") String shortCode, 
                                @Param("startTime") LocalDateTime startTime, 
                                @Param("endTime") LocalDateTime endTime);

    @Query("SELECT ce.browser, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "GROUP BY ce.browser " +
           "ORDER BY clicks DESC")
    List<Object[]> getBrowserStats(@Param("shortCode") String shortCode);

    @Query("SELECT ce.operatingSystem, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "GROUP BY ce.operatingSystem " +
           "ORDER BY clicks DESC")
    List<Object[]> getOperatingSystemStats(@Param("shortCode") String shortCode);

    @Query("SELECT ce.deviceType, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "GROUP BY ce.deviceType " +
           "ORDER BY clicks DESC")
    List<Object[]> getDeviceTypeStats(@Param("shortCode") String shortCode);

    @Query("SELECT ce.country, COUNT(ce) as clicks " +
           "FROM ClickEvent ce WHERE ce.shortCode = :shortCode " +
           "GROUP BY ce.country " +
           "ORDER BY clicks DESC")
    List<Object[]> getLocationStats(@Param("shortCode") String shortCode);
}

