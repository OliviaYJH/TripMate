package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.UpdateUserProfileRequest;
import com.gbsb.tripmate.dto.UserResponse;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.UserService;
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
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
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
    public ResponseEntity<User> updateUserProfile(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateUserProfileRequest dto) {
        return ResponseEntity.ok(userService.updateUserProfile(Long.valueOf(userDetails.getUsername()), dto));
    }

    @GetMapping("/{userId}/meetings")
    public ResponseEntity<Page<Meeting>> getMeetingsByUserId(@AuthenticationPrincipal UserDetails userDetails,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Meeting> meetings = userService.getMeetingsByUserId(Long.valueOf(userDetails.getUsername()), pageable);
        return ResponseEntity.ok(meetings);
    }
}