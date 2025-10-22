package com.zerobase.memobackend.repository;

import com.zerobase.memobackend.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {

    // 제목으로 검색
    List<Memo> findByTitleContainingIgnoreCase(String title);

    // 작성자로 검색
    List<Memo> findByAuthor(String author);

    // 태그로 검색
    @Query("SELECT DISTINCT m FROM Memo m JOIN m.tags t WHERE t.name = :tagName")
    List<Memo> findByTagName(@Param("tagName") String tagName);

    // 전문 검색 (제목 또는 내용)
    @Query("SELECT m FROM Memo m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Memo> searchByKeyword(@Param("keyword") String keyword);

    // 여러 태그로 검색 (OR 조건)
    @Query("SELECT DISTINCT m FROM Memo m JOIN m.tags t WHERE t.name IN :tagNames")
    List<Memo> findByTagNames(@Param("tagNames") List<String> tagNames);

    // 제목과 내용 복합 검색
    @Query("SELECT m FROM Memo m WHERE " +
           "LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%')) AND " +
           "LOWER(m.content) LIKE LOWER(CONCAT('%', :content, '%'))")
    List<Memo> findByTitleAndContent(@Param("title") String title, 
                                      @Param("content") String content);

}

