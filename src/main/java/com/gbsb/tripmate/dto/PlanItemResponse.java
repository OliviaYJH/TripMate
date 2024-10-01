package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.TravelStyle;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class PlanItemResponse {
    private Long planItemId;
    private LocalDate planDate;
    private String itemName;
    private TravelStyle itemType;
    private String itemDescription;
    private LocalTime startTime;
    private LocalTime endTime;
    private int itemOrder;
}
