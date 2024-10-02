package com.gbsb.tripmate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class PlanCreate {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행 계획 생성 DTO")
    public static class Request {
        @Schema(description = "여행 계획 제목", example = "제주도 첫째 날 일정")
        private String planTitle;

        @Schema(description = "여행 날짜", example = "2024-10-15")
        private LocalDate planDate;
    }
}
