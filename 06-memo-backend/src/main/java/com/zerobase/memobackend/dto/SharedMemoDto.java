package com.zerobase.memobackend.dto;

import com.zerobase.memobackend.entity.SharedMemo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SharedMemoDto {

    private Long id;
    private Long memoId;
    private String shareToken;
    private LocalDateTime expiresAt;
    private Boolean isActive;
    private LocalDateTime createdAt;

    public static SharedMemoDto fromEntity(SharedMemo sharedMemo) {
        return SharedMemoDto.builder()
                .id(sharedMemo.getId())
                .memoId(sharedMemo.getMemo().getId())
                .shareToken(sharedMemo.getShareToken())
                .expiresAt(sharedMemo.getExpiresAt())
                .isActive(sharedMemo.getIsActive())
                .createdAt(sharedMemo.getCreatedAt())
                .build();
    }

}

