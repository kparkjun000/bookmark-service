package com.zerobase.bookmarkservice.controller;

import com.zerobase.bookmarkservice.dto.BookmarkDto;
import com.zerobase.bookmarkservice.service.BookmarkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Bookmark", description = "북마크 관리 API")
@RestController
@RequestMapping("/api/users/{userId}/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {
    
    private final BookmarkService bookmarkService;
    
    @Operation(summary = "북마크 생성", description = "새로운 북마크를 생성합니다. URL에서 메타데이터를 자동으로 추출합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "북마크 생성 성공",
                     content = @Content(schema = @Schema(implementation = BookmarkDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @PostMapping
    public ResponseEntity<BookmarkDto.Response> createBookmark(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Valid @RequestBody BookmarkDto.Create request) {
        BookmarkDto.Response response = bookmarkService.createBookmark(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @Operation(summary = "북마크 목록 조회", description = "사용자의 북마크 목록을 페이지네이션으로 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping
    public ResponseEntity<Page<BookmarkDto.Response>> getUserBookmarks(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "정렬 기준 (createdAt, updatedAt, title)")
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "정렬 방향 (asc, desc)")
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ? 
                Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
        
        Page<BookmarkDto.Response> bookmarks = bookmarkService.getUserBookmarks(userId, pageable);
        return ResponseEntity.ok(bookmarks);
    }
    
    @Operation(summary = "북마크 상세 조회", description = "ID로 북마크를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
                     content = @Content(schema = @Schema(implementation = BookmarkDto.Response.class))),
        @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    })
    @GetMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkDto.Response> getBookmark(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "북마크 ID", required = true)
            @PathVariable Long bookmarkId) {
        BookmarkDto.Response response = bookmarkService.getBookmark(userId, bookmarkId);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "즐겨찾기 북마크 조회", description = "사용자의 즐겨찾기 북마크를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/favorites")
    public ResponseEntity<Page<BookmarkDto.Response>> getFavoriteBookmarks(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookmarkDto.Response> bookmarks = bookmarkService.getFavoriteBookmarks(userId, pageable);
        return ResponseEntity.ok(bookmarks);
    }
    
    @Operation(summary = "태그별 북마크 조회", description = "특정 태그가 포함된 북마크를 조회합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "사용자 또는 태그를 찾을 수 없음")
    })
    @GetMapping("/tags/{tagId}")
    public ResponseEntity<Page<BookmarkDto.Response>> getBookmarksByTag(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "태그 ID", required = true)
            @PathVariable Long tagId,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookmarkDto.Response> bookmarks = bookmarkService.getBookmarksByTag(userId, tagId, pageable);
        return ResponseEntity.ok(bookmarks);
    }
    
    @Operation(summary = "북마크 검색", description = "제목, 설명, URL로 북마크를 검색합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<BookmarkDto.Response>> searchBookmarks(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "검색 키워드", required = true)
            @RequestParam String keyword,
            @Parameter(description = "페이지 번호 (0부터 시작)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기")
            @RequestParam(defaultValue = "20") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BookmarkDto.Response> bookmarks = bookmarkService.searchBookmarks(userId, keyword, pageable);
        return ResponseEntity.ok(bookmarks);
    }
    
    @Operation(summary = "북마크 수정", description = "북마크 정보를 수정합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공",
                     content = @Content(schema = @Schema(implementation = BookmarkDto.Response.class))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청"),
        @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    })
    @PutMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkDto.Response> updateBookmark(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "북마크 ID", required = true)
            @PathVariable Long bookmarkId,
            @Valid @RequestBody BookmarkDto.Update request) {
        BookmarkDto.Response response = bookmarkService.updateBookmark(userId, bookmarkId, request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "북마크 삭제", description = "북마크를 삭제합니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "삭제 성공"),
        @ApiResponse(responseCode = "404", description = "북마크를 찾을 수 없음")
    })
    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(
            @Parameter(description = "사용자 ID", required = true)
            @PathVariable Long userId,
            @Parameter(description = "북마크 ID", required = true)
            @PathVariable Long bookmarkId) {
        bookmarkService.deleteBookmark(userId, bookmarkId);
        return ResponseEntity.noContent().build();
    }
}

