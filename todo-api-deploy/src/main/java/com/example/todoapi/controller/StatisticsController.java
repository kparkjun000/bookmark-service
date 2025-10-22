package com.example.todoapi.controller;

import com.example.todoapi.dto.StatisticsDto;
import com.example.todoapi.entity.User;
import com.example.todoapi.service.StatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
@Tag(name = "Statistics", description = "통계 API")
public class StatisticsController {
    private final StatisticsService statisticsService;

    @GetMapping
    @Operation(summary = "통계 조회", description = "할일에 대한 통계를 조회합니다")
    public ResponseEntity<StatisticsDto> getStatistics(@AuthenticationPrincipal User user) {
        StatisticsDto statistics = statisticsService.getStatistics(user);
        return ResponseEntity.ok(statistics);
    }
}