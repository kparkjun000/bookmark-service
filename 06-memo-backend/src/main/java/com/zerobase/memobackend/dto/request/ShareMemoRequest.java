package com.zerobase.memobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "메모 공유 요청")
public class ShareMemoRequest {

    @Schema(description = "만료 시간 (시간 단위, null이면 무제한)", example = "24")
    private Integer expiresInHours;

}

