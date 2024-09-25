package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.TravelStyle;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class PlanItemCreate {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String planItemName;
        private TravelStyle travelStyle;
        private String planItemDescription;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long placeId;
        private int itemOrder;
    }
}
