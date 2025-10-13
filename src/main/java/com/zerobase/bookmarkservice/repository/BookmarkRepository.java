package com.zerobase.bookmarkservice.repository;

import com.zerobase.bookmarkservice.entity.Bookmark;
import com.zerobase.bookmarkservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    
    List<Bookmark> findByUser(User user);
    
    Page<Bookmark> findByUserId(Long userId, Pageable pageable);
    
    Optional<Bookmark> findByIdAndUserId(Long id, Long userId);
    
    List<Bookmark> findByUserIdAndIsFavoriteTrue(Long userId);
    
    Page<Bookmark> findByUserIdAndIsFavoriteTrue(Long userId, Pageable pageable);
    
    @Query("SELECT b FROM Bookmark b JOIN b.tags t WHERE b.user.id = :userId AND t.id = :tagId")
    Page<Bookmark> findByUserIdAndTagId(@Param("userId") Long userId, @Param("tagId") Long tagId, Pageable pageable);
    
    @Query("SELECT b FROM Bookmark b WHERE b.user.id = :userId AND " +
           "(LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.url) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Bookmark> searchByKeyword(@Param("userId") Long userId, @Param("keyword") String keyword, Pageable pageable);
    
    Page<Bookmark> findByIsPublicTrue(Pageable pageable);
}

