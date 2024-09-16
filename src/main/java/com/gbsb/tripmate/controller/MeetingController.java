package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.BaseResponse;
import com.gbsb.tripmate.dto.MeetingCreateRequest;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.MeetingService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

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