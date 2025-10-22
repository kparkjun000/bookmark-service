package com.zerobase.memobackend.dto;

import com.zerobase.memobackend.entity.Memo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemoDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Set<TagDto> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static MemoDto fromEntity(Memo memo) {
        return MemoDto.builder()
                .id(memo.getId())
                .title(memo.getTitle())
                .content(memo.getContent())
                .author(memo.getAuthor())
                .tags(memo.getTags().stream()
                        .map(TagDto::fromEntity)
                        .collect(Collectors.toSet()))
                .createdAt(memo.getCreatedAt())
                .updatedAt(memo.getUpdatedAt())
                .build();
    }

}

