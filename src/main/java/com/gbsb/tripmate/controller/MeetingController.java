package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.MeetingCreateRequest;
import com.gbsb.tripmate.dto.JoinMeeting;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.MeetingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    // 모임 생성
    @PostMapping("/create")
    public BaseResponse<Boolean> createGroup(@RequestBody MeetingCreateRequest request, @AuthenticationPrincipal User user) {
        try {
            Meeting newMeeting = meetingService.createMeeting(user.getId(), request);
            return new BaseResponse<>("모임이 개설되었습니다.", true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("모임 개설에 실패했습니다.", false);
        }
    }

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
  
    // 모임 삭제
    @DeleteMapping("/{meetingId}")
    public BaseResponse<Boolean> deleteMeeting(@PathVariable long meetingId, @AuthenticationPrincipal User user) {
        try {
            meetingService.deleteMeeting(user.getId(), meetingId);
            return new BaseResponse<>("모임이 삭제되었습니다.", true);
        } catch (RuntimeException e) {
            return new BaseResponse<>(e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            return new BaseResponse<>("모임 삭제에 실패했습니다.", false);
        }
    }
}