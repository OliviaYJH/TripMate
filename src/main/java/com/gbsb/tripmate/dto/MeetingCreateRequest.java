package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.AgeGroup;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MeetingCreateRequest {
    private String meetingTitle;
    private String description;
    private String destination;
    private Gender gender;
    private AgeGroup ageGroup;
    private TravelStyle travelStyle;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
    private int memberMax;
}
