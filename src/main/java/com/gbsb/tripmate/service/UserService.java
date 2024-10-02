package com.gbsb.tripmate.service;

import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.dto.UpdateUserProfileRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    User registerUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> getUserInfo(Long userId);
    User updateUserProfile(Long userId, UpdateUserProfileRequest dto);
    Page<Meeting> getMeetingsByUserId(Long userId, Pageable pageable);
    boolean existsByNickname(String nickname);
}
