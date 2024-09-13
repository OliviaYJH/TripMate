package contoller;

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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;


    // 사용자 정보 조회 (D-day 포함)
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId)
                .map(user -> ResponseEntity.ok(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 프로필 업데이트
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserProfile(@PathVariable Long userId, @RequestBody UpdateUserProfileRequest dto) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, dto));
    }

    // 사용자 모임 목록 조회 (페이지네이션 적용)
    @GetMapping("/{userId}/meetings")
    public ResponseEntity<Page<Meeting>> getMeetingsByUserId(@PathVariable Long userId,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Meeting> meetings = userService.getMeetingsByUserId(userId, pageable);
        return ResponseEntity.ok(meetings);
    }
}

