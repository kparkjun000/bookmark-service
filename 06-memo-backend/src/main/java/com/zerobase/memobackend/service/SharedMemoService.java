package com.zerobase.memobackend.service;

import com.zerobase.memobackend.dto.MemoDto;
import com.zerobase.memobackend.dto.SharedMemoDto;
import com.zerobase.memobackend.dto.request.ShareMemoRequest;
import com.zerobase.memobackend.entity.Memo;
import com.zerobase.memobackend.entity.SharedMemo;
import com.zerobase.memobackend.exception.ResourceNotFoundException;
import com.zerobase.memobackend.repository.MemoRepository;
import com.zerobase.memobackend.repository.SharedMemoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class SharedMemoService {

    private final SharedMemoRepository sharedMemoRepository;
    private final MemoRepository memoRepository;
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 메모 공유 링크 생성
     */
    @Transactional
    public SharedMemoDto createShareLink(Long memoId, ShareMemoRequest request) {
        log.info("메모 공유 링크 생성 - 메모 ID: {}", memoId);

        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new ResourceNotFoundException("메모를 찾을 수 없습니다. ID: " + memoId));

        String shareToken = generateShareToken();
        LocalDateTime expiresAt = null;

        if (request.getExpiresInHours() != null && request.getExpiresInHours() > 0) {
            expiresAt = LocalDateTime.now().plusHours(request.getExpiresInHours());
        }

        SharedMemo sharedMemo = SharedMemo.builder()
                .memo(memo)
                .shareToken(shareToken)
                .expiresAt(expiresAt)
                .isActive(true)
                .build();

        SharedMemo savedSharedMemo = sharedMemoRepository.save(sharedMemo);
        return SharedMemoDto.fromEntity(savedSharedMemo);
    }

    /**
     * 공유 토큰으로 메모 조회
     */
    public MemoDto getMemoByShareToken(String shareToken) {
        log.info("공유 토큰으로 메모 조회 - 토큰: {}", shareToken);

        SharedMemo sharedMemo = sharedMemoRepository
                .findActiveByShareToken(shareToken, LocalDateTime.now())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "유효하지 않거나 만료된 공유 링크입니다."));

        return MemoDto.fromEntity(sharedMemo.getMemo());
    }

    /**
     * 메모의 모든 공유 링크 조회
     */
    public List<SharedMemoDto> getShareLinksByMemoId(Long memoId) {
        log.info("메모의 공유 링크 목록 조회 - 메모 ID: {}", memoId);

        if (!memoRepository.existsById(memoId)) {
            throw new ResourceNotFoundException("메모를 찾을 수 없습니다. ID: " + memoId);
        }

        return sharedMemoRepository.findByMemoId(memoId).stream()
                .map(SharedMemoDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 공유 링크 비활성화
     */
    @Transactional
    public void deactivateShareLink(String shareToken) {
        log.info("공유 링크 비활성화 - 토큰: {}", shareToken);

        SharedMemo sharedMemo = sharedMemoRepository.findByShareToken(shareToken)
                .orElseThrow(() -> new ResourceNotFoundException("공유 링크를 찾을 수 없습니다."));

        sharedMemo.setIsActive(false);
        sharedMemoRepository.save(sharedMemo);
    }

    /**
     * 만료된 공유 링크 정리
     */
    @Transactional
    public void cleanupExpiredLinks() {
        log.info("만료된 공유 링크 정리 시작");

        List<SharedMemo> expiredShares = sharedMemoRepository.findExpiredShares(LocalDateTime.now());
        
        expiredShares.forEach(share -> share.setIsActive(false));
        
        sharedMemoRepository.saveAll(expiredShares);
        
        log.info("만료된 공유 링크 {}개 비활성화 완료", expiredShares.size());
    }

    /**
     * 고유한 공유 토큰 생성
     */
    private String generateShareToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}

