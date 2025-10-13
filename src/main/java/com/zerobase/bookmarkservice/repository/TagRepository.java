package com.zerobase.bookmarkservice.repository;

import com.zerobase.bookmarkservice.entity.Tag;
import com.zerobase.bookmarkservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    
    List<Tag> findByUser(User user);
    
    List<Tag> findByUserId(Long userId);
    
    Optional<Tag> findByNameAndUser(String name, User user);
    
    Optional<Tag> findByIdAndUserId(Long id, Long userId);
    
    boolean existsByNameAndUser(String name, User user);
}

