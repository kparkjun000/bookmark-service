package com.zerobase.bookmarkservice.controller;

import com.zerobase.bookmarkservice.dto.TagDto;
import com.zerobase.bookmarkservice.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag", description = "태그 관리 API")
@RestController
@RequestMapping("/api/users/{userId}/tags")
@RequiredArgsConstructor
public class TagController {
    
    private final TagService tagService;
    
    @Operation(summary = "태그 생성", description = "새로운 태그를 생성합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "태그 생성 성공",
                     content = @Content(schema = @Schema(implementation = TagDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 태그")
    })
    @PostMapping
    public ResponseEntity<TagDto.Response> createTag(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Valid @RequestBody TagDto.Create request) {
        TagDto.Response response = tagService.createTag(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "사용자 태그 목록 조회", description = "사용자의 모든 태그를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<List<TagDto.Response>> getUserTags(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId) {
        List<TagDto.Response> tags = tagService.getUserTags(userId);
        return ResponseEntity.ok(tags);
    }
    
    @Operation(summary = "태그 조회", description = "ID로 태그를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                     content = @Content(schema = @Schema(implementation = TagDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    })
    @GetMapping("/{tagId}")
    public ResponseEntity<TagDto.Response> getTag(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "태그 ID", required = true)
            @PathVariable Long tagId) {
        TagDto.Response response = tagService.getTag(userId, tagId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "태그 삭제", description = "태그를 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "태그를 찾을 수 없음")
    })
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "태그 ID", required = true)
            @PathVariable Long tagId) {
        tagService.deleteTag(userId, tagId);
        return ResponseEntity.noContent().build();
    }
}

