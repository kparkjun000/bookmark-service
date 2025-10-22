package com.example.todoapi.repository;

import com.example.todoapi.entity.Category;
import com.example.todoapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);

    Optional<Category> findByIdAndUser(Long id, User user);

    Optional<Category> findByNameAndUser(String name, User user);

    Boolean existsByNameAndUser(String name, User user);

    @Query("SELECT c.id as id, c.name as name, COUNT(t) as todoCount " +
           "FROM Category c LEFT JOIN c.todos t " +
           "WHERE c.user = :user " +
           "GROUP BY c.id, c.name")
    List<Object[]> findCategoriesWithTodoCount(@Param("user") User user);
}