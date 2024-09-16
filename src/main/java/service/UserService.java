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

    public Optional<User> getUserInfo(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

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

    public User updateUserProfile(Long userId, UpdateUserProfileRequest dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserNickname(dto.getUserNickname());
        user.setIntroduce(dto.getIntroduce());
        user.setUserPassword(dto.getPassword());
        user.setModifiedDate(LocalDateTime.now());

        return userRepository.save(user);
    }

    public Page<Meeting> getMeetingsByUserId(Long userId, Pageable pageable) {
        return meetingRepository.findMeetingsByUserId(userId, pageable);
    }
}
