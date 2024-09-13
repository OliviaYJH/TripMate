package service;

import dto.UpdateUserProfileRequest;
import entity.Meeting;
import entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import repository.MeetingRepository;
import repository.TravelPlanRepository;
import repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TravelPlanRepository travelPlanRepository;
    private final MeetingRepository meetingRepository;

    public UserService(UserRepository userRepository, TravelPlanRepository travelPlanRepository, MeetingRepository meetingRepository) {
        this.userRepository = userRepository;
        this.travelPlanRepository = travelPlanRepository;
        this.meetingRepository = meetingRepository;
    }

    // 사용자 정보 조회 기능 (D-day 포함)
    public Optional<User> getUserInfo(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 가장 가까운 여행 날짜 조회
            LocalDate nextTravelDate = travelPlanRepository.findNextTravelDateByUserId(userId);
            if (nextTravelDate != null) {
                long dDay = ChronoUnit.DAYS.between(LocalDate.now(), nextTravelDate);
                System.out.println("Next travel D-day: " + dDay);
            } else {
                System.out.println("No upcoming travel plans.");
            }

            return Optional.of(user);
        }
        return Optional.empty();
    }

    // 프로필 업데이트 기능
    public User updateUserProfile(Long userId, UpdateUserProfileRequest dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserNickname(dto.getNickname());
        user.setIntroduce(dto.getIntroduce());
        user.setUserPassword(dto.getPassword());
        user.setModifiedDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    // 사용자 모임 목록 조회 (페이지네이션 적용)
    public Page<Meeting> getMeetingsByUserId(Long userId, Pageable pageable) {
        return meetingRepository.findMeetingsByUserId(userId, pageable);
    }
}

