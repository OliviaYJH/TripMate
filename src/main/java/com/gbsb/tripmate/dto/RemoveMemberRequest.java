package com.gbsb.tripmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "모임 멤버 제거 요청 DTO")
public class RemoveMemberRequest {
    @Schema(description = "제거할 모임 멤버의 ID", example = "123")
    private Long meetingMemberId;

    @Schema(description = "멤버 제거 사유", example = "불참 횟수 초과")
    private String reason;
}
