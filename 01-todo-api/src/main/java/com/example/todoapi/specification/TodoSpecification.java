package com.example.todoapi.specification;

import com.example.todoapi.entity.Todo;
import com.example.todoapi.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class TodoSpecification {

    public static Specification<Todo> belongsToUser(User user) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user"), user);
    }

    public static Specification<Todo> containsKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likePattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), likePattern)
            );
        };
    }

    public static Specification<Todo> isCompleted(Boolean completed) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("completed"), completed);
    }

    public static Specification<Todo> hasPriority(Todo.Priority priority) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("priority"), priority);
    }

    public static Specification<Todo> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Todo> dueDateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("dueDate"), from, to);
    }
}