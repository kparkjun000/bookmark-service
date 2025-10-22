package com.zerobase.memobackend.dto;

import com.zerobase.memobackend.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {

    private Long id;
    private String name;
    private String color;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TagDto fromEntity(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .name(tag.getName())
                .color(tag.getColor())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();
    }

}

