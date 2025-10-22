package com.survey.api.controller;

import com.survey.api.dto.QuestionDto;
import com.survey.api.dto.SurveyDto;
import com.survey.api.security.UserDetailsImpl;
import com.survey.api.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Survey", description = "설문조사 관리 API")
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(summary = "설문조사 생성", description = "새로운 설문조사를 생성합니다.")
    @PostMapping
    public ResponseEntity<SurveyDto.Response> createSurvey(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody SurveyDto.CreateRequest request) {
        SurveyDto.Response response = surveyService.createSurvey(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "설문조사 상세 조회", description = "특정 설문조사의 상세 정보를 조회합니다.")
    @GetMapping("/{surveyId}")
    public ResponseEntity<SurveyDto.Response> getSurvey(@PathVariable Long surveyId) {
        SurveyDto.Response response = surveyService.getSurvey(surveyId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "모든 설문조사 조회", description = "모든 설문조사 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SurveyDto.Response>> getAllSurveys() {
        List<SurveyDto.Response> responses = surveyService.getAllSurveys();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "내 설문조사 조회", description = "내가 생성한 설문조사 목록을 조회합니다.")
    @GetMapping("/my")
    public ResponseEntity<List<SurveyDto.Response>> getMySurveys(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<SurveyDto.Response> responses = surveyService.getMySurveys(userDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "활성 설문조사 조회", description = "현재 활성화된 설문조사 목록을 조회합니다.")
    @GetMapping("/active")
    public ResponseEntity<List<SurveyDto.Response>> getActiveSurveys() {
        List<SurveyDto.Response> responses = surveyService.getActiveSurveys();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "설문조사 수정", description = "설문조사 정보를 수정합니다.")
    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyDto.Response> updateSurvey(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody SurveyDto.UpdateRequest request) {
        SurveyDto.Response response = surveyService.updateSurvey(surveyId, userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "설문조사 삭제", description = "설문조사를 삭제합니다.")
    @DeleteMapping("/{surveyId}")
    public ResponseEntity<Void> deleteSurvey(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        surveyService.deleteSurvey(surveyId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "질문 추가", description = "설문조사에 질문을 추가합니다.")
    @PostMapping("/{surveyId}/questions")
    public ResponseEntity<QuestionDto.Response> addQuestion(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody QuestionDto.CreateRequest request) {
        QuestionDto.Response response = surveyService.addQuestion(surveyId, userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "질문 목록 조회", description = "설문조사의 질문 목록을 조회합니다.")
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<List<QuestionDto.Response>> getQuestions(@PathVariable Long surveyId) {
        List<QuestionDto.Response> responses = surveyService.getQuestions(surveyId);
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "질문 수정", description = "설문조사의 질문을 수정합니다.")
    @PutMapping("/questions/{questionId}")
    public ResponseEntity<QuestionDto.Response> updateQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody QuestionDto.UpdateRequest request) {
        QuestionDto.Response response = surveyService.updateQuestion(questionId, userDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "질문 삭제", description = "설문조사의 질문을 삭제합니다.")
    @DeleteMapping("/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long questionId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        surveyService.deleteQuestion(questionId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}

