package com.survey.api.dto;

import com.survey.api.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        private String title;

        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private List<QuestionDto.CreateRequest> questions;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        @Size(max = 200, message = "Title must not exceed 200 characters")
        private String title;

        private String description;
        private Boolean active;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private String title;
        private String description;
        private Long creatorId;
        private String creatorUsername;
        private boolean active;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private int questionCount;
        private int responseCount;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
