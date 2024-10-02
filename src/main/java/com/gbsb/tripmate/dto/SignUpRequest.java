package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "회원가입 요청 DTO")
public class SignUpRequest {
    @NotBlank
    @Size(max = 40)
    @Email
    @Schema(description = "사용자 이메일", example = "user@example.com")
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    @Schema(description = "사용자 비밀번호 (6-20자)", example = "password123")
    private String password;

    @NotBlank
    @Size(min = 3, max = 15)
    @Schema(description = "사용자 닉네임 (3-15자)", example = "traveler")
    private String nickname;

    @NotNull
    @Schema(description = "사용자 성별", example = "MALE")
    private Gender gender;

    @NotBlank
    @Schema(description = "사용자 생년월일 (YYYY-MM-DD)", example = "1990-01-01")
    private String birthdate;

    @NotBlank
    @Size(max = 40)
    @Schema(description = "사용자 실명", example = "홍길동")
    private String name;

    @Schema(description = "사용자 자기소개", example = "여행을 좋아하는 직장인입니다.")
    private String introduce;
}
