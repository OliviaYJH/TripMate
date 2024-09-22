package com.gbsb.tripmate.controller;

import com.gbsb.tripmate.dto.ApiResponse;
import com.gbsb.tripmate.dto.JwtAuthenticationResponse;
import com.gbsb.tripmate.dto.LoginRequest;
import com.gbsb.tripmate.dto.SignUpRequest;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.UserService;
import com.gbsb.tripmate.service.UserRegistrationService;
import com.gbsb.tripmate.util.JwtUtil;
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
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRegistrationService userServiceImpl;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = new User();
        user.setUserEmail(signUpRequest.getEmail());
        user.setUserPassword(signUpRequest.getPassword());
        user.setUserNickname(signUpRequest.getNickname());
        user.setGender(signUpRequest.getGender());
        user.setBirthdate(LocalDate.parse(signUpRequest.getBirthdate()));
        user.setUserName(signUpRequest.getName());
        user.setIntroduce(signUpRequest.getIntroduce());

        userServiceImpl.registerUser(user);

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
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
    public ResponseEntity<?> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = !userServiceImpl.existsByEmail(email);
        return ResponseEntity.ok(new ApiResponse(isAvailable,
                isAvailable ? "Email is available" : "Email is already in use"));
    }
}