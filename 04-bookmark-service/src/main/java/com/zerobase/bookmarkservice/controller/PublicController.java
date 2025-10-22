package com.zerobase.bookmarkservice.controller;

import com.zerobase.bookmarkservice.dto.BookmarkDto;
import com.zerobase.bookmarkservice.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Public", description = "공개 API")
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class PublicController {
    
    private final BookmarkService bookmarkService;
    
    @Operation(summary = "공개 북마크 조회", description = "공개로 설정된 북마크를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping("/bookmarks")
    public ResponseEntity<Page<BookmarkDto.Response>> getPublicBookmarks(
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookmarkDto.Response> bookmarks = bookmarkService.getPublicBookmarks(pageable);
        return ResponseEntity.ok(bookmarks);
    }
    
    @Operation(summary = "URL 메타데이터 추출", description = "URL에서 메타데이터(제목, 설명, 이미지 등)를 추출합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "추출 성공"),
        @ApiResponse(responseCode = "400", description = "잘못된 URL"),
        @ApiResponse(responseCode = "500", description = "메타데이터 추출 실패")
    })
    @GetMapping("/metadata")
    public ResponseEntity<BookmarkDto.MetadataResponse> extractMetadata(
            @Parameter(description = "메타데이터를 추출할 URL", required = true)
            @RequestParam String url) {
        BookmarkDto.MetadataResponse response = bookmarkService.extractMetadata(url);
        return ResponseEntity.ok(response);
    }
}

