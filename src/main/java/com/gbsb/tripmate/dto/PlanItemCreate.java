package com.gbsb.tripmate.dto;

import lombok.*;

import java.time.LocalDate;

public class PlanItemCreate {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private LocalDate travelDate;
        private int itemOrder;
        private Long placeId;
    }
}
