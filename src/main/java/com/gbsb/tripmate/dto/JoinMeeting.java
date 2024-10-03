package com.gbsb.tripmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

public class JoinMeeting {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "모임 참여 요청 DTO")
    public static class Request {
        @Schema(description = "참여 시작 날짜", example = "2024-10-03")
        private LocalDate travelStartDate;

        @Schema(description = "참여 종료 날짜", example = "2024-10-05")
        private LocalDate travelEndDate;
    }
}