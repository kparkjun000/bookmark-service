package com.fileservice.controller;

import com.fileservice.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "File Upload Service");
        
        return ResponseEntity.ok(ApiResponse.success(health));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, String>>> root() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "File Upload and Image Processing API");
        info.put("version", "1.0.0");
        info.put("description", "RESTful API for file upload and image processing");
        
        return ResponseEntity.ok(ApiResponse.success(info));
    }
}

