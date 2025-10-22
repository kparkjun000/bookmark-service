package com.example.todoapi.repository;

import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {
    Page<Todo> findByUser(User user, Pageable pageable);

    Optional<Todo> findByIdAndUser(Long id, User user);

    List<Todo> findByUserAndCompleted(User user, Boolean completed);

    List<Todo> findByUserAndPriority(User user, Todo.Priority priority);

    List<Todo> findByUserAndCategoryId(User user, Long categoryId);

    @Query("SELECT t FROM Todo t WHERE t.user = :user AND " +
           "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Todo> searchByKeyword(@Param("user") User user, @Param("keyword") String keyword);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user = :user")
    Long countByUser(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user = :user AND t.completed = true")
    Long countCompletedByUser(@Param("user") User user);

    @Query("SELECT COUNT(t) FROM Todo t WHERE t.user = :user AND t.category.id = :categoryId")
    Long countByCategoryId(@Param("user") User user, @Param("categoryId") Long categoryId);

    List<Todo> findByUserAndDueDateBetween(User user, LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM Todo t WHERE t.user = :user AND t.dueDate < :now AND t.completed = false")
    List<Todo> findOverdueTodos(@Param("user") User user, @Param("now") LocalDateTime now);
}