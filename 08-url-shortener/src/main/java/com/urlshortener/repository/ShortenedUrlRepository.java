package com.urlshortener.repository;

import com.urlshortener.entity.ShortenedUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ShortenedUrlRepository extends JpaRepository<ShortenedUrl, Long> {

    Optional<ShortenedUrl> findByShortCode(String shortCode);

    Optional<ShortenedUrl> findByCustomAlias(String customAlias);

    boolean existsByShortCode(String shortCode);

    boolean existsByCustomAlias(String customAlias);

    @Modifying
    @Query("UPDATE ShortenedUrl u SET u.clickCount = u.clickCount + 1 WHERE u.shortCode = :shortCode")
    int incrementClickCount(@Param("shortCode") String shortCode);

    @Query("SELECT COUNT(u) FROM ShortenedUrl u WHERE u.isActive = true")
    long countActiveUrls();

    @Query("SELECT u FROM ShortenedUrl u WHERE u.expiresAt < :now AND u.isActive = true")
    Iterable<ShortenedUrl> findExpiredUrls(@Param("now") LocalDateTime now);
}

