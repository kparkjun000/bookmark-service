package com.zerobase.memobackend.repository;

import com.zerobase.memobackend.entity.SharedMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SharedMemoRepository extends JpaRepository<SharedMemo, Long> {

    // 공유 토큰으로 검색
    Optional<SharedMemo> findByShareToken(String shareToken);

    // 메모 ID로 공유 목록 조회
    List<SharedMemo> findByMemoId(Long memoId);

    // 활성화된 공유 링크만 조회
    @Query("SELECT s FROM SharedMemo s WHERE s.shareToken = :token AND s.isActive = true " +
           "AND (s.expiresAt IS NULL OR s.expiresAt > :now)")
    Optional<SharedMemo> findActiveByShareToken(@Param("token") String token, 
                                                  @Param("now") LocalDateTime now);

    // 만료된 공유 링크 조회
    @Query("SELECT s FROM SharedMemo s WHERE s.expiresAt < :now AND s.isActive = true")
    List<SharedMemo> findExpiredShares(@Param("now") LocalDateTime now);

}

