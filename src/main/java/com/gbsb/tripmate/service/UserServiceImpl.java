package com.gbsb.tripmate.service;

import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.dto.UpdateUserProfileRequest;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MeetingRepository meetingRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    @Override
    public Optional<User> getUserInfo(Long userId) {
        return userRepository.findById(userId);
    }


    @Override
    public User updateUserProfile(Long userId, UpdateUserProfileRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNickname(dto.getUserNickname());
        user.setIntroduce(dto.getIntroduce());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setModifiedDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Page<Meeting> getMeetingsByUserId(Long userId, Pageable pageable) {
        return meetingRepository.findMeetingsByUserId(userId, pageable);
    }
}
