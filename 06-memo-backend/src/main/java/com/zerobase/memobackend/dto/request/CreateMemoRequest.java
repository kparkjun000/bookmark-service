package com.zerobase.memobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "메모 생성 요청")
public class CreateMemoRequest {

    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자를 초과할 수 없습니다")
    @Schema(description = "메모 제목", example = "프로젝트 아이디어")
    private String title;

    @NotBlank(message = "내용은 필수입니다")
    @Schema(description = "메모 내용", example = "새로운 메모 앱 개발하기")
    private String content;

    @Size(max = 100, message = "작성자 이름은 100자를 초과할 수 없습니다")
    @Schema(description = "작성자", example = "홍길동")
    private String author;

    @Schema(description = "태그 이름 목록", example = "[\"개발\", \"아이디어\"]")
    private Set<String> tagNames;

}

