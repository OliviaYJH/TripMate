package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.AgeRange;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class UpdateMeeting {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "모임 정보 업데이트 DTO")
    public static class Request {
        @Schema(description = "수정된 모임 제목", example = "제주도 힐링 여행")
        private String meetingTitle;

        @Schema(description = "수정된 모임 설명", example = "제주도의 아름다운 자연을 즐기는 힐링 여행")
        private String description;

        @Schema(description = "수정된 여행 목적지", example = "제주도")
        private String destination;

        @Schema(description = "수정된 성별 조건", example = "ALL_GENDERS")
        private Gender gender;

        @Schema(description = "수정된 연령대 조건", example = "TWENTIES")
        private AgeRange ageRange;

        @Schema(description = "수정된 여행 스타일", example = "HEALING")
        private TravelStyle travelStyle;

        @Schema(description = "수정된 최대 인원 수", example = "8")
        private int memberMax;

        @Schema(description = "수정된 여행 시작 날짜", example = "2024-11-01")
        private LocalDate travelStartDate;

        @Schema(description = "수정된 여행 종료 날짜", example = "2024-11-05")
        private LocalDate travelEndDate;
    }
}