package com.gbsb.tripmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 업데이트 요청 DTO")
public class UpdateUserProfileRequest {
    @Schema(description = "수정된 사용자 닉네임", example = "여행하는호랑이")
    private String userNickname;

    @Schema(description = "수정된 비밀번호", example = "newPassword123")
    private String password;

    @Schema(description = "수정된 자기소개", example = "세계 여행을 꿈꾸는 직장인입니다.")
    private String introduce;
}