package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.AgeRange;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
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
        private Gender gender;
        private AgeRange ageRange;
        private TravelStyle travelStyle;
        private int memberMax;
        private LocalDate travelStartDate;
        private LocalDate travelEndDate;
    }
}
