package com.example.todoapi.dto;

import com.example.todoapi.entity.Todo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class TodoDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Title is required")
        private String title;

        private String description;

        @NotNull(message = "Priority is required")
        private Todo.Priority priority;

        private LocalDateTime dueDate;

        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String title;
        private String description;
        private Boolean completed;
        private Todo.Priority priority;
        private LocalDateTime dueDate;
        private Long categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TodoResponse {
        private Long id;
        private String title;
        private String description;
        private Boolean completed;
        private Todo.Priority priority;
        private LocalDateTime dueDate;
        private CategoryDto.CategoryResponse category;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchRequest {
        private String keyword;
        private Boolean completed;
        private Todo.Priority priority;
        private Long categoryId;
        private LocalDateTime dueDateFrom;
        private LocalDateTime dueDateTo;
    }
}