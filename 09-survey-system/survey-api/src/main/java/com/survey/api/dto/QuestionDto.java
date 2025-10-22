package com.survey.api.dto;

import com.survey.api.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class QuestionDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        @NotBlank(message = "Question text is required")
        private String questionText;

        @NotNull(message = "Question type is required")
        private Question.QuestionType type;

        @NotNull(message = "Order index is required")
        private Integer orderIndex;

        private Boolean required;
        private List<String> options;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        private String questionText;
        private Question.QuestionType type;
        private Integer orderIndex;
        private Boolean required;
        private List<String> options;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long surveyId;
        private String questionText;
        private Question.QuestionType type;
        private int orderIndex;
        private boolean required;
        private List<String> options;
    }
}
