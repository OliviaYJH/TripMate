package service;

import com.gbsb.tripmate.dto.UpdateUserProfileRequest;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.gbsb.tripmate.repository.TravelPlanRepository;
import com.gbsb.tripmate.repository.UserRepository;
import com.gbsb.tripmate.repository.MeetingRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TravelPlanRepository travelPlanRepository;

    @Mock
    private MeetingRepository meetingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserInfo() {
        // Given
        Long userId = 1L;
        User mockUser = User.builder()
                .userId(userId)
                .userNickname("testNick")
                .build();

        // 목 객체 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(travelPlanRepository.findNextTravelDateByUserId(userId)).thenReturn(null);

        // When
        Optional<User> result = userService.getUserInfo(userId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("testNick", result.get().getUserNickname());
    }

    @Test
    void testUpdateUserProfile() {
        // Given
        Long userId = 1L;
        UpdateUserProfileRequest dto = new UpdateUserProfileRequest("newNickname", "newPassword", "newIntroduce");
        User mockUser = User.builder()
                .userId(userId)
                .userNickname("oldNickname")
                .userPassword("oldPassword")
                .introduce("oldIntroduce")
                .build();

        // 목 객체 설정
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(mockUser)).thenReturn(mockUser);

        // When
        User updatedUser = userService.updateUserProfile(userId, dto);

        // Then
        assertEquals("newNickname", updatedUser.getUserNickname());
        assertEquals("newPassword", updatedUser.getUserPassword());
        assertEquals("newIntroduce", updatedUser.getIntroduce());
    }
}
