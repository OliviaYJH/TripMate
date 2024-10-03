package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.ApiResponse;
import com.gbsb.tripmate.dto.JwtAuthenticationResponse;
import com.gbsb.tripmate.dto.LoginRequest;
import com.gbsb.tripmate.dto.SignUpRequest;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.service.UserService;
import com.gbsb.tripmate.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "인증 관련 API")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    public ResponseEntity<?> registerUser(
            @Parameter(description = "회원가입 정보", required = true)
            @Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByEmail(signUpRequest.getEmail())) {
            throw new MeetingException(ErrorCode.INVALID_REQUEST, "Email is already in use");
        }
        if (userService.existsByNickname(signUpRequest.getNickname())) {
            throw new MeetingException(ErrorCode.INVALID_REQUEST, "Nickname is already in use");
        }
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(signUpRequest.getPassword());
        user.setNickname(signUpRequest.getNickname());
        user.setGender(signUpRequest.getGender());
        user.setBirthdate(LocalDate.parse(signUpRequest.getBirthdate()));
        user.setName(signUpRequest.getName());
        user.setIntroduce(signUpRequest.getIntroduce());

        userService.registerUser(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/signin")
    @Operation(summary = "로그인", description = "사용자 인증을 수행합니다.")
    public ResponseEntity<?> authenticateUser(
            @Parameter(description = "로그인 정보", required = true)
            @Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtil.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @GetMapping("/check-email")
    @Operation(summary = "이메일 중복 확인", description = "이메일의 사용 가능 여부를 확인합니다.")
    public ResponseEntity<?> checkEmailAvailability(
            @Parameter(description = "확인할 이메일", required = true)
            @RequestParam String email) {
        boolean isAvailable = !userService.existsByEmail(email);
        return ResponseEntity.ok(new ApiResponse(isAvailable,
                isAvailable ? "Email is available" : "Email is already in use"));
    }
}