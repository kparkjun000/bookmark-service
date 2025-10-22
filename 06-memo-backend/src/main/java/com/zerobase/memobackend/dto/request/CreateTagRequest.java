package com.zerobase.memobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "태그 생성 요청")
public class CreateTagRequest {

    @NotBlank(message = "태그 이름은 필수입니다")
    @Size(max = 50, message = "태그 이름은 50자를 초과할 수 없습니다")
    @Schema(description = "태그 이름", example = "개발")
    private String name;

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "색상은 #RRGGBB 형식이어야 합니다")
    @Schema(description = "태그 색상 (Hex 코드)", example = "#3B82F6")
    private String color;

}

