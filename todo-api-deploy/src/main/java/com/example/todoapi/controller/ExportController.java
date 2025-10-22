package com.example.todoapi.controller;

import com.example.todoapi.entity.User;
import com.example.todoapi.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Export", description = "데이터 내보내기 API")
public class ExportController {
    private final ExportService exportService;

    @GetMapping("/json")
    @Operation(summary = "JSON 내보내기", description = "할일 목록을 JSON 형식으로 내보냅니다")
    public ResponseEntity<byte[]> exportAsJson(@AuthenticationPrincipal User user) throws Exception {
        byte[] data = exportService.exportTodosAsJson(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentDispositionFormData("attachment", "todos.json");

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }

    @GetMapping("/csv")
    @Operation(summary = "CSV 내보내기", description = "할일 목록을 CSV 형식으로 내보냅니다")
    public ResponseEntity<byte[]> exportAsCsv(@AuthenticationPrincipal User user) throws Exception {
        byte[] data = exportService.exportTodosAsCsv(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "todos.csv");

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}