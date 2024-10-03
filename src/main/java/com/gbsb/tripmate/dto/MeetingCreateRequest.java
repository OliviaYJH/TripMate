package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.AgeRange;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "모임 생성 요청 DTO")
public class MeetingCreateRequest {
    @Schema(description = "모임 제목", example = "제주도 여행")
    private String meetingTitle;

    @Schema(description = "모임 설명", example = "제주도의 아름다운 경치를 감상하는 3박 4일 여행")
    private String description;

    @Schema(description = "여행 목적지", example = "제주도")
    private String destination;

    @Schema(description = "성별 조건", example = "ALL_GENDERS")
    private Gender gender;

    @Schema(description = "연령대 조건", example = "TWENTIES")
    private AgeRange ageRange;

    @Schema(description = "여행 스타일", example = "HEALING")
    private TravelStyle travelStyle;

    @Schema(description = "여행 시작 날짜", example = "2023-07-01")
    private LocalDate travelStartDate;

    @Schema(description = "여행 종료 날짜", example = "2023-07-04")
    private LocalDate travelEndDate;

    @Schema(description = "최대 인원 수", example = "6")
    private int memberMax;
}
