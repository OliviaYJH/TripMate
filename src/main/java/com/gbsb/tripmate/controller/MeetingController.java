package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.*;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.service.CustomUserDetailsService;
import com.gbsb.tripmate.service.MeetingService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequiredArgsConstructor
@RequestMapping("/meetings")
@Tag(name = "Meeting", description = "모임 관련 API")
public class MeetingController {
    private final MeetingService meetingService;

    // 모임 생성
    @PostMapping("/create")
    @Operation(summary = "모임 생성", description = "새로운 모임을 생성합니다.",
                security = @SecurityRequirement(name = "bearer-jwt"))
    public BaseResponse<Boolean> createGroup(
            @Parameter(description = "모임 생성 요청") @RequestBody MeetingCreateRequest request,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
        try {
            Meeting newMeeting = meetingService.createMeeting(user.getId(), request);
            return new BaseResponse<>("모임이 개설되었습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>("모임 개설에 실패했습니다.", false);
        }
    }

    // 모임 목록 조회
    @GetMapping
    @Operation(summary = "모임 목록 조회", description = "모든 모임의 목록을 조회합니다.")
    public Page<MeetingResponse> getMeetings(
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 기준") @RequestParam(defaultValue = "createdDate") String sortBy
    ) {
        return meetingService.getAllMeetings(page, size, sortBy);
    }

    @PutMapping("/{meetingId}")
    @Operation(summary = "모임 수정", description = "기존 모임의 정보를 수정합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    BaseResponse<Boolean> updateMeeting(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId,
            @Parameter(description = "모임 수정 요청") @RequestBody UpdateMeeting.Request request
    ) {
        meetingService.updateMeeting(meetingId, request);
        return new BaseResponse<>("모임 수정 성공", true);
    }

    @PostMapping("/join/{meetingId}")
    @Operation(summary = "모임 참여", description = "특정 모임에 참여합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    BaseResponse<Boolean> joinMeeting(
            @Parameter(description = "모임 ID") @PathVariable Long meetingId,
            @Parameter(description = "모임 참여 요청") @RequestBody JoinMeeting.Request request
    ) {
        meetingService.joinMeeting(meetingId, request);
        return new BaseResponse<>("모임 참여 성공", true);
    }
  
    // 모임 삭제
    @DeleteMapping("/{meetingId}")
    @Operation(summary = "모임 삭제", description = "특정 모임을 삭제합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    public BaseResponse<Boolean> deleteMeeting(
            @Parameter(description = "모임 ID") @PathVariable long meetingId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
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
    @Operation(summary = "모임 탈퇴", description = "특정 모임에서 탈퇴합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    public BaseResponse<Boolean> leaveMeeting(
            @Parameter(description = "모임 ID") @PathVariable long meetingId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user) {
        try{
            meetingService.leaveMeeting(user.getId(), meetingId);
            return new BaseResponse<>("모임에서 탈퇴하였습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>(e.getMessage(), false);
        }
    }

    // 멤버 내보내기
    @PostMapping("/{meetingId}/remove-member")
    @Operation(summary = "멤버 제거", description = "모임에서 특정 멤버를 제거합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    public BaseResponse<Boolean> removeMember(
            @Parameter(description = "모임 ID") @PathVariable long meetingId,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails leader,
            @Parameter(description = "멤버 제거 요청") @RequestBody RemoveMemberRequest request) {
        try {
            meetingService.removeMember(leader.getId(), meetingId, request);
            return new BaseResponse<>("멤버가 탈퇴되었습니다.", true);
        } catch (Exception e) {
            return new BaseResponse<>(e.getMessage(), false);
        }
    }

    // 모임 검색
    @GetMapping("/search")
    @Operation(summary = "모임 검색", description = "모임을 검색합니다.",
            security = @SecurityRequirement(name = "bearer-jwt"))
    public Page<MeetingResponse> searchMeetings(
            @Parameter(description = "검색할 모임 제목")  @RequestParam String meetingTitle,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetailsService.CustomUserDetails user,
            @Parameter(description = "페이지 번호") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size
    ) {
        return meetingService.searchMeetings(user.getId(), meetingTitle, page, size);
    }
}