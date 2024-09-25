package com.gbsb.tripmate.dto;

import lombok.*;

import java.time.LocalDate;

public class PlanCreate {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Request {
        private String planTitle;
        private LocalDate planDate;
    }
}
