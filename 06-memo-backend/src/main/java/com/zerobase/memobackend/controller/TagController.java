package com.zerobase.memobackend.controller;

import com.zerobase.memobackend.dto.TagDto;
import com.zerobase.memobackend.dto.request.CreateTagRequest;
import com.zerobase.memobackend.dto.response.ApiResponse;
import com.zerobase.memobackend.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Tag(name = "Tag", description = "태그 관리 API")
public class TagController {

    private final TagService tagService;

    @GetMapping
    @Operation(summary = "모든 태그 조회", description = "저장된 모든 태그를 조회합니다.")
    public ResponseEntity<ApiResponse<List<TagDto>>> getAllTags() {
        List<TagDto> tags = tagService.getAllTags();
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

    @GetMapping("/{id}")
    @Operation(summary = "태그 상세 조회", description = "ID로 특정 태그를 조회합니다.")
    public ResponseEntity<ApiResponse<TagDto>> getTagById(
            @Parameter(description = "태그 ID") @PathVariable Long id) {
        TagDto tag = tagService.getTagById(id);
        return ResponseEntity.ok(ApiResponse.success(tag));
    }

    @PostMapping
    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다.")
    public ResponseEntity<ApiResponse<TagDto>> createTag(
            @Valid @RequestBody CreateTagRequest request) {
        TagDto tag = tagService.createTag(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("태그가 성공적으로 생성되었습니다.", tag));
    }

    @PutMapping("/{id}")
    @Operation(summary = "태그 수정", description = "기존 태그를 수정합니다.")
    public ResponseEntity<ApiResponse<TagDto>> updateTag(
            @Parameter(description = "태그 ID") @PathVariable Long id,
            @Valid @RequestBody CreateTagRequest request) {
        TagDto tag = tagService.updateTag(id, request);
        return ResponseEntity.ok(ApiResponse.success("태그가 성공적으로 수정되었습니다.", tag));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "태그 삭제", description = "태그를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteTag(
            @Parameter(description = "태그 ID") @PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(ApiResponse.success("태그가 성공적으로 삭제되었습니다.", null));
    }

    @GetMapping("/search")
    @Operation(summary = "태그 검색", description = "키워드로 태그를 검색합니다.")
    public ResponseEntity<ApiResponse<List<TagDto>>> searchTags(
            @Parameter(description = "검색 키워드") @RequestParam String keyword) {
        List<TagDto> tags = tagService.searchTags(keyword);
        return ResponseEntity.ok(ApiResponse.success(tags));
    }

}

