package com.zerobase.memobackend.controller;

import com.zerobase.memobackend.dto.MemoDto;
import com.zerobase.memobackend.dto.request.CreateMemoRequest;
import com.zerobase.memobackend.dto.request.UpdateMemoRequest;
import com.zerobase.memobackend.dto.response.ApiResponse;
import com.zerobase.memobackend.service.MemoService;
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
@RequestMapping("/api/memos")
@RequiredArgsConstructor
@Tag(name = "Memo", description = "메모 관리 API")
public class MemoController {

    private final MemoService memoService;

    @GetMapping
    @Operation(summary = "모든 메모 조회", description = "저장된 모든 메모를 조회합니다.")
    public ResponseEntity<ApiResponse<List<MemoDto>>> getAllMemos() {
        List<MemoDto> memos = memoService.getAllMemos();
        return ResponseEntity.ok(ApiResponse.success(memos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "메모 상세 조회", description = "ID로 특정 메모를 조회합니다.")
    public ResponseEntity<ApiResponse<MemoDto>> getMemoById(
            @Parameter(description = "메모 ID") @PathVariable Long id) {
        MemoDto memo = memoService.getMemoById(id);
        return ResponseEntity.ok(ApiResponse.success(memo));
    }

    @PostMapping
    @Operation(summary = "메모 생성", description = "새로운 메모를 생성합니다.")
    public ResponseEntity<ApiResponse<MemoDto>> createMemo(
            @Valid @RequestBody CreateMemoRequest request) {
        MemoDto memo = memoService.createMemo(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("메모가 성공적으로 생성되었습니다.", memo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "메모 수정", description = "기존 메모를 수정합니다.")
    public ResponseEntity<ApiResponse<MemoDto>> updateMemo(
            @Parameter(description = "메모 ID") @PathVariable Long id,
            @Valid @RequestBody UpdateMemoRequest request) {
        MemoDto memo = memoService.updateMemo(id, request);
        return ResponseEntity.ok(ApiResponse.success("메모가 성공적으로 수정되었습니다.", memo));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "메모 삭제", description = "메모를 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteMemo(
            @Parameter(description = "메모 ID") @PathVariable Long id) {
        memoService.deleteMemo(id);
        return ResponseEntity.ok(ApiResponse.success("메모가 성공적으로 삭제되었습니다.", null));
    }

    @GetMapping("/search")
    @Operation(summary = "메모 검색", description = "키워드로 메모를 검색합니다. (제목 및 내용)")
    public ResponseEntity<ApiResponse<List<MemoDto>>> searchMemos(
            @Parameter(description = "검색 키워드") @RequestParam String keyword) {
        List<MemoDto> memos = memoService.searchMemos(keyword);
        return ResponseEntity.ok(ApiResponse.success(memos));
    }

    @GetMapping("/tag/{tagName}")
    @Operation(summary = "태그로 메모 검색", description = "특정 태그가 달린 메모를 조회합니다.")
    public ResponseEntity<ApiResponse<List<MemoDto>>> getMemosByTag(
            @Parameter(description = "태그 이름") @PathVariable String tagName) {
        List<MemoDto> memos = memoService.getMemosByTag(tagName);
        return ResponseEntity.ok(ApiResponse.success(memos));
    }

    @GetMapping("/author/{author}")
    @Operation(summary = "작성자로 메모 검색", description = "특정 작성자의 메모를 조회합니다.")
    public ResponseEntity<ApiResponse<List<MemoDto>>> getMemosByAuthor(
            @Parameter(description = "작성자 이름") @PathVariable String author) {
        List<MemoDto> memos = memoService.getMemosByAuthor(author);
        return ResponseEntity.ok(ApiResponse.success(memos));
    }

}

