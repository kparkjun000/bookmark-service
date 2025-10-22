package com.example.todoapi.controller;

import com.example.todoapi.dto.CategoryDto;
import com.example.todoapi.entity.User;
import com.example.todoapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Category", description = "카테고리 관리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "카테고리 생성", description = "새로운 카테고리를 생성합니다")
    public ResponseEntity<CategoryDto.CategoryResponse> createCategory(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CategoryDto.CreateRequest request) {
        CategoryDto.CategoryResponse response = categoryService.createCategory(user, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회", description = "사용자의 모든 카테고리를 조회합니다")
    public ResponseEntity<List<CategoryDto.CategoryResponse>> getAllCategories(
            @AuthenticationPrincipal User user) {
        List<CategoryDto.CategoryResponse> categories = categoryService.getAllCategories(user);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 상세 조회", description = "특정 카테고리를 조회합니다")
    public ResponseEntity<CategoryDto.CategoryResponse> getCategoryById(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId) {
        CategoryDto.CategoryResponse response = categoryService.getCategoryById(user, categoryId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다")
    public ResponseEntity<CategoryDto.CategoryResponse> updateCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId,
            @RequestBody CategoryDto.UpdateRequest request) {
        CategoryDto.CategoryResponse response = categoryService.updateCategory(user, categoryId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다")
    public ResponseEntity<Void> deleteCategory(
            @AuthenticationPrincipal User user,
            @PathVariable Long categoryId) {
        categoryService.deleteCategory(user, categoryId);
        return ResponseEntity.noContent().build();
    }
}