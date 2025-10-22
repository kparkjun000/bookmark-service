package com.zerobase.memobackend.repository;

import com.zerobase.memobackend.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    // 태그 이름으로 검색
    Optional<Tag> findByName(String name);

    // 태그 이름으로 존재 여부 확인
    boolean existsByName(String name);

    // 태그 이름 리스트로 검색
    List<Tag> findByNameIn(Set<String> names);

    // 태그 이름으로 부분 검색
    List<Tag> findByNameContainingIgnoreCase(String name);

}

