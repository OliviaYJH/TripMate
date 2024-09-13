package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.dto.constant.AgeRange;
import com.gbsb.tripmate.dto.constant.Gender;
import com.gbsb.tripmate.dto.constant.TravelStyle;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GroupCreateRequest {
    private String meetingTitle;
    private String description;
    private String destination;
    private Gender gender;
    private AgeRange ageRange;
    private TravelStyle travelStyle;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
    private int memberMax;
}
