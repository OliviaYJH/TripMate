package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.*;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.service.CustomUserDetailsService;
import com.gbsb.tripmate.service.MeetingService;
import org.springframework.data.domain.Page;
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
    public BaseResponse<Boolean> createGroup(@RequestBody MeetingCreateRequest request, @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
        try {
            Meeting newMeeting = meetingService.createMeeting(user.getId(), request);
            return new BaseResponse<>("모임이 개설되었습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>("모임 개설에 실패했습니다.", false);
        }
    }

    // 모임 목록 조회
    @GetMapping
    public Page<MeetingResponse> getMeetings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy
    ) {
        return meetingService.getAllMeetings(page, size, sortBy);
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
    public BaseResponse<Boolean> deleteMeeting(@PathVariable long meetingId, @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
        try {
            meetingService.deleteMeeting(user.getId(), meetingId);
            return new BaseResponse<>("모임이 삭제되었습니다.", true);
        } catch (RuntimeException e) {
            return new BaseResponse<>(e.getMessage(), false);
        } catch (Exception e) {
            return new BaseResponse<>("모임 삭제에 실패했습니다.", false);
        }
    }
  
    // 모임 탈퇴
    @DeleteMapping("/{meetingId}/leave")
    public BaseResponse<Boolean> leaveMeeting(@PathVariable long meetingId, @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
        try{
            meetingService.leaveMeeting(user.getId(), meetingId);
            return new BaseResponse<>("모임에서 탈퇴하였습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>(e.getMessage(), false);
        }
    }

    // 멤버 내보내기
    @PostMapping("/{meetingId}/remove-member")
    public BaseResponse<Boolean> removeMember(@PathVariable long meetingId, @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails leader,
                                              @RequestBody RemoveMemberRequest request) {
        try {
            meetingService.removeMember(leader.getId(), meetingId, request);
            return new BaseResponse<>("멤버가 탈퇴되었습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>(e.getMessage(), false);
        }
    }

    // 모임 검색
    @GetMapping("/search")
    public Page<MeetingResponse> searchMeetings(
            @RequestParam String meetingTitle,
            @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return meetingService.searchMeetings(user.getId(), meetingTitle, page, size);
    }
}