package controller;

import dto.UpdateUserProfileRequest;
import entity.Meeting;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody UpdateUserProfileRequest dto) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, dto));
    }

    @GetMapping("/{userId}/meetings")
    public ResponseEntity<Page<Meeting>> getMeetingsByUserId(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Meeting> meetings = userService.getMeetingsByUserId(userId, pageable);
        return ResponseEntity.ok(meetings);
    }
}
