package com.gbsb.tripmate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    private String meetingTitle;
    private String meetingDescription;
    private String destination;
    private String leaderNickname;
    private LocalDate travelStartDate;
    private LocalDate travelEndDate;
}
