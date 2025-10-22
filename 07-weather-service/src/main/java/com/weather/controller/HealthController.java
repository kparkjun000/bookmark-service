package com.weather.controller;

import com.weather.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Health", description = "Health check endpoints")
public class HealthController {

    @Operation(
        summary = "Health check",
        description = "Check if the API is running"
    )
    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "Weather Service API");
        health.put("version", "1.0.0");
        
        return ResponseEntity.ok(ApiResponse.success("Service is healthy", health));
    }

    @Operation(
        summary = "Root endpoint",
        description = "Welcome message and API information"
    )
    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> root() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "Weather Service API");
        info.put("version", "1.0.0");
        info.put("swagger", "/swagger-ui.html");
        info.put("api-docs", "/api-docs");
        
        return ResponseEntity.ok(ApiResponse.success("Welcome to Weather Service API", info));
    }
}

