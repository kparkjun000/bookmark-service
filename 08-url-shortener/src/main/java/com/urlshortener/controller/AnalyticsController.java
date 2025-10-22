package com.urlshortener.controller;

import com.urlshortener.dto.AnalyticsOverviewResponse;
import com.urlshortener.dto.TimelineResponse;
import com.urlshortener.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@Tag(name = "Analytics", description = "URL 클릭 통계 및 분석 API")
public class AnalyticsController {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    /**
     * Get analytics overview for a short code
     */
    @Operation(summary = "전체 통계 조회", description = "단축 URL의 전체 통계 정보를 조회합니다. 총 클릭 수, 기간별 클릭 수, 브라우저/OS/디바이스 통계를 포함합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}")
    public ResponseEntity<AnalyticsOverviewResponse> getAnalyticsOverview(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting analytics overview for: {}", shortCode);
        AnalyticsOverviewResponse response = analyticsService.getAnalyticsOverview(shortCode);
        return ResponseEntity.ok(response);
    }

    /**
     * Get timeline statistics
     */
    @Operation(summary = "시간대별 통계 조회", description = "시간별 또는 일별로 클릭 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}/timeline")
    public ResponseEntity<TimelineResponse> getTimeline(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode,
            @Parameter(description = "시간 단위 (hourly 또는 daily)") @RequestParam(defaultValue = "daily") String granularity,
            @Parameter(description = "조회 기간 (일)") @RequestParam(defaultValue = "30") int days) {
        log.info("Getting timeline for: {} with granularity: {} and days: {}", shortCode, granularity, days);
        TimelineResponse response = analyticsService.getTimelineStats(shortCode, granularity, days);
        return ResponseEntity.ok(response);
    }

    /**
     * Get browser statistics
     */
    @Operation(summary = "브라우저별 통계 조회", description = "브라우저별 클릭 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}/browsers")
    public ResponseEntity<Map<String, Long>> getBrowserStats(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting browser stats for: {}", shortCode);
        Map<String, Long> stats = analyticsService.getBrowserStats(shortCode);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get operating system statistics
     */
    @Operation(summary = "운영체제별 통계 조회", description = "운영체제별 클릭 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}/os")
    public ResponseEntity<Map<String, Long>> getOperatingSystemStats(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting OS stats for: {}", shortCode);
        Map<String, Long> stats = analyticsService.getOperatingSystemStats(shortCode);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get device type statistics
     */
    @Operation(summary = "디바이스별 통계 조회", description = "디바이스 타입별 클릭 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}/devices")
    public ResponseEntity<Map<String, Long>> getDeviceTypeStats(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting device stats for: {}", shortCode);
        Map<String, Long> stats = analyticsService.getDeviceTypeStats(shortCode);
        return ResponseEntity.ok(stats);
    }

    /**
     * Get location statistics
     */
    @Operation(summary = "위치별 통계 조회", description = "국가별 클릭 통계를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "URL을 찾을 수 없음")
    })
    @GetMapping("/{shortCode}/locations")
    public ResponseEntity<Map<String, Long>> getLocationStats(
            @Parameter(description = "단축 코드", required = true) @PathVariable String shortCode) {
        log.info("Getting location stats for: {}", shortCode);
        Map<String, Long> stats = analyticsService.getLocationStats(shortCode);
        return ResponseEntity.ok(stats);
    }
}
