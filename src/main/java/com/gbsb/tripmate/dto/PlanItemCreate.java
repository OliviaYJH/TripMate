package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.TravelStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlanItemCreate {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "여행 계획 항목 생성 DTO")
    public static class Request {
        @Schema(description = "계획 항목 이름", example = "성산일출봉 방문")
        private String planItemName;

        @Schema(description = "여행 스타일", example = "TOURISM")
        private TravelStyle travelStyle;

        @Schema(description = "계획 항목 설명", example = "유네스코 세계자연유산인 성산일출봉 등반")
        private String planItemDescription;

        @Schema(description = "시작 시간", example = "09:00")
        private LocalTime startTime;

        @Schema(description = "종료 시간", example = "11:00")
        private LocalTime endTime;

        @Schema(description = "장소 ID", example = "1234")
        private Long placeId;

        @Schema(description = "항목 순서", example = "1")
        private int itemOrder;
    }
}
