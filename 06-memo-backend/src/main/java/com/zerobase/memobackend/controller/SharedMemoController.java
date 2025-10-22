package com.zerobase.memobackend.controller;

import com.zerobase.memobackend.dto.MemoDto;
import com.zerobase.memobackend.dto.SharedMemoDto;
import com.zerobase.memobackend.dto.request.ShareMemoRequest;
import com.zerobase.memobackend.dto.response.ApiResponse;
import com.zerobase.memobackend.service.SharedMemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shared")
@RequiredArgsConstructor
@Tag(name = "Shared Memo", description = "메모 공유 API")
public class SharedMemoController {

    private final SharedMemoService sharedMemoService;

    @PostMapping("/memos/{memoId}")
    @Operation(summary = "메모 공유 링크 생성", description = "메모의 공유 링크를 생성합니다.")
    public ResponseEntity<ApiResponse<SharedMemoDto>> createShareLink(
            @Parameter(description = "메모 ID") @PathVariable Long memoId,
            @RequestBody(required = false) ShareMemoRequest request) {
        
        if (request == null) {
            request = new ShareMemoRequest();
        }
        
        SharedMemoDto sharedMemo = sharedMemoService.createShareLink(memoId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("공유 링크가 생성되었습니다.", sharedMemo));
    }

    @GetMapping("/{shareToken}")
    @Operation(summary = "공유 메모 조회", description = "공유 토큰으로 메모를 조회합니다.")
    public ResponseEntity<ApiResponse<MemoDto>> getMemoByShareToken(
            @Parameter(description = "공유 토큰") @PathVariable String shareToken) {
        MemoDto memo = sharedMemoService.getMemoByShareToken(shareToken);
        return ResponseEntity.ok(ApiResponse.success(memo));
    }

    @GetMapping("/memos/{memoId}/links")
    @Operation(summary = "메모의 공유 링크 목록", description = "특정 메모의 모든 공유 링크를 조회합니다.")
    public ResponseEntity<ApiResponse<List<SharedMemoDto>>> getShareLinksByMemoId(
            @Parameter(description = "메모 ID") @PathVariable Long memoId) {
        List<SharedMemoDto> sharedMemos = sharedMemoService.getShareLinksByMemoId(memoId);
        return ResponseEntity.ok(ApiResponse.success(sharedMemos));
    }

    @DeleteMapping("/{shareToken}")
    @Operation(summary = "공유 링크 비활성화", description = "공유 링크를 비활성화합니다.")
    public ResponseEntity<ApiResponse<Void>> deactivateShareLink(
            @Parameter(description = "공유 토큰") @PathVariable String shareToken) {
        sharedMemoService.deactivateShareLink(shareToken);
        return ResponseEntity.ok(ApiResponse.success("공유 링크가 비활성화되었습니다.", null));
    }

}

