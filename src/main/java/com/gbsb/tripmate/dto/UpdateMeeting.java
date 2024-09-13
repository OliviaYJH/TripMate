package com.gbsb.tripmate.dto;

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
    public static class Request {
        private String meetingTitle;
        private String description;
        private String destination;
        private String gender;
        private String ageRange;
        private String travelStyle;
        private int memberMax;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
    }
}