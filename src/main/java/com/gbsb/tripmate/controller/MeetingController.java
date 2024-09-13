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

    @PutMapping("/{groupId}")
    BaseResponse<Boolean> updateMeeting(
            @PathVariable Long groupId,
            @RequestBody UpdateMeeting.Request request
    ) {
        // 하나라도 Null이면 error 처리
        // enum은 타입 맞는지 확인
        // 여행 시작일은 오늘로부터 3일 이후부터 가능
        meetingService.updateTripGroup(groupId, request);
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