package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.UpdateUserProfileRequest;
import com.gbsb.tripmate.dto.UserResponse;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "사용자 관리 API")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @Operation(summary = "사용자 정보 조회", description = "인증된 사용자의 정보를 조회합니다")
    public ResponseEntity<UserResponse> getUserInfo(
            @Parameter(description = "인증된 사용자 정보", required = true)
            @AuthenticationPrincipal UserDetails userDetails) {
        return userService.getUserInfo(Long.valueOf(userDetails.getUsername()))
                .map(user -> ResponseEntity.ok(new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getNickname(),
                        user.getGender(),
                        user.getBirthdate(),
                        user.getAgeRange(),
                        user.getName(),
                        user.getIntroduce())))
                .orElseGet(() -> ResponseEntity.notFound().build());
        }

        @PutMapping("/{userId}")
        @Operation(summary = "사용자 프로필 업데이트", description = "인증된 사용자의 프로필을 업데이트합니다")
        public ResponseEntity<User> updateUserProfile(
                @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetails userDetails,
                @Parameter(description = "업데이트할 사용자 프로필 정보", required = true) @RequestBody UpdateUserProfileRequest dto) {
        return ResponseEntity.ok(userService.updateUserProfile(Long.valueOf(userDetails.getUsername()), dto));
        }

        @GetMapping("/{userId}/meetings")
        @Operation(summary = "사용자의 모임 조회", description = "인증된 사용자의 모임 목록을 페이지네이션하여 조회합니다")
        public ResponseEntity<Page<Meeting>> getMeetingsByUserId(
                @Parameter(description = "인증된 사용자 정보", required = true) @AuthenticationPrincipal UserDetails userDetails,
                @Parameter(description = "페이지 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
                @Parameter(description = "페이지당 항목 수") @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<Meeting> meetings = userService.getMeetingsByUserId(Long.valueOf(userDetails.getUsername()), pageable);
                return ResponseEntity.ok(meetings);
            }
        }