package com.gbsb.tripmate.dto;

import lombok.Data;

@Data
public class RemoveMemberRequest {
    private Long meetingMemberId;
    private String reason;
}
