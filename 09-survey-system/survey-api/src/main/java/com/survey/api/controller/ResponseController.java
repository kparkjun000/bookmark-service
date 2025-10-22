package com.survey.api.controller;

import com.survey.api.dto.ResponseDto;
import com.survey.api.security.UserDetailsImpl;
import com.survey.api.service.ResponseService;
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

@Tag(name = "Response", description = "설문 응답 관리 API")
@RestController
@RequestMapping("/api/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @Operation(summary = "설문 응답 제출", description = "설문조사에 대한 응답을 제출합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<ResponseDto.Response> submitResponse(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ResponseDto.SubmitRequest request) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        ResponseDto.Response response = responseService.submitResponse(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "응답 상세 조회", description = "특정 응답의 상세 정보를 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/{responseId}")
    public ResponseEntity<ResponseDto.Response> getResponse(@PathVariable Long responseId) {
        ResponseDto.Response response = responseService.getResponse(responseId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "설문의 모든 응답 조회", description = "특정 설문조사의 모든 응답을 조회합니다. (설문 생성자만 가능)")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<ResponseDto.Response>> getSurveyResponses(
            @PathVariable Long surveyId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ResponseDto.Response> responses = responseService.getSurveyResponses(surveyId, userDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "내 응답 조회", description = "내가 제출한 모든 응답을 조회합니다.")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/my")
    public ResponseEntity<List<ResponseDto.Response>> getMyResponses(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<ResponseDto.Response> responses = responseService.getMyResponses(userDetails.getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "응답 삭제", description = "응답을 삭제합니다. (응답자 또는 설문 생성자만 가능)")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{responseId}")
    public ResponseEntity<Void> deleteResponse(
            @PathVariable Long responseId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        responseService.deleteResponse(responseId, userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}

