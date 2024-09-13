package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.service.MeetingService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/groups")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PutMapping("/{groupId}")
    BaseResponse<Boolean> updateTripGroup(
            @PathVariable Long groupId,
            @RequestBody UpdateMeeting.Request request
    ) {
        System.out.println("groupId " + groupId.toString());
        meetingService.updateTripGroup(groupId, request);
        return new BaseResponse<>("모임 수정 성공", true);
    }

    @PostMapping("/{groupId}/join")
    BaseResponse<Boolean> joinTripGroup(
            @PathVariable Long groupId,
            @RequestParam Long memberId,
            @RequestParam LocalDate travelStartDate,
            @RequestParam LocalDate travelEndDate
    ) {
        return new BaseResponse<>("모임 참여 성공", true);
    }
}