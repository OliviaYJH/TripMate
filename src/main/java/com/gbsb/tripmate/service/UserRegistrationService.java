package com.gbsb.tripmate.service;


import com.gbsb.tripmate.entity.User;

public interface UserRegistrationService {
    User registerUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}