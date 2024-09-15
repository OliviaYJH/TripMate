package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.JoinMeeting;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.service.MeetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/groups")
public class MeetingController {
    private final MeetingService meetingService;

    @PutMapping("/{meetingId}")
    BaseResponse<Boolean> updateMeeting(
            @PathVariable Long meetingId,
            @RequestBody UpdateMeeting.Request request
    ) {
        meetingService.updateMeeting(meetingId, request);
        return new BaseResponse<>("모임 수정 성공", true);
    }

    @PostMapping("/join/{meetingId}")
    BaseResponse<Boolean> joinMeeting(
            @PathVariable Long meetingId,
            @RequestBody JoinMeeting.Request request
    ) {
        meetingService.joinMeeting(meetingId, request);
        return new BaseResponse<>("모임 참여 성공", true);
    }

//    @GetMapping("/{userId}")
//    Optional<UserEntity> getMember(
//            @PathVariable Long userId
//    ) {
//        return meetingService.getMember(userId);
//    }
}