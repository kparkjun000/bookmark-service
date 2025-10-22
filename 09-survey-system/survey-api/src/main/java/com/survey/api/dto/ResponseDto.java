package com.survey.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubmitRequest {
        @NotNull(message = "Survey ID is required")
        private Long surveyId;

        private List<AnswerDto.CreateRequest> answers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long id;
        private Long surveyId;
        private String surveyTitle;
        private Long respondentId;
        private String respondentUsername;
        private LocalDateTime submittedAt;
        private List<AnswerDto.Response> answers;
    }
}
