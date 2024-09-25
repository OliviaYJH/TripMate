package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.MeetingCreateRequest;
import com.gbsb.tripmate.dto.JoinMeeting;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.DailyParticipation;
import com.gbsb.tripmate.entity.Meeting;
import com.gbsb.tripmate.entity.MeetingMember;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.DailyParticipationRepository;
import com.gbsb.tripmate.repository.MeetingMemberRepository;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final DailyParticipationRepository dailyParticipationRepository;

    // 모임 생성
    public Meeting createMeeting(Long id, MeetingCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Meeting meeting = Meeting.builder()
                .meetingLeader(user)
                .meetingTitle(request.getMeetingTitle())
                .meetingDescription(request.getDescription())
                .destination(request.getDestination())
                .genderCondition(request.getGender())
                .ageGroup(request.getAgeGroup())
                .travelStyle(request.getTravelStyle())
                .travelStartDate(request.getTravelStartDate())
                .travelEndDate(request.getTravelEndDate())
                .memberMax(request.getMemberMax())
                .createdDate(LocalDate.now())
                .build();

        Meeting savedMeeting = meetingRepository.save(meeting);

        // 모임장을 모임 멤버에 추가
        MeetingMember leader = MeetingMember.builder()
                .meeting(savedMeeting)
                .user(user)
                .isLeader(true)
                .joinDate(LocalDate.now())
                .build();

        meetingMemberRepository.save(leader);
        return savedMeeting;
    }

    // 모임 삭제
    public void deleteMeeting(Long id, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new ResourceNotFoundException("모임을 찾을 수 없습니다."));

        if (!meeting.getMeetingLeader().getId().equals(id)) {
            throw new RuntimeException("모임장만 모임을 삭제할 수 있습니다.");
        }

        int memberCount = meetingMemberRepository.countMembersByGroupId(meetingId);

        // 모임장 외 모임멤버가 존재할 경우 삭제 제한
        if (memberCount > 1) {
            throw new RuntimeException("참여 멤버가 존재해 모임을 삭제할 수 없습니다.");
        }

        // 여행 기간 중 삭제 제한
        if (!LocalDate.now().isBefore(meeting.getTravelStartDate()) && !LocalDate.now().isAfter(meeting.getTravelEndDate())) {
            throw new RuntimeException("여행기간 중에는 모임을 삭제할 수 없습니다.");
        }

        meeting.setIsDeleted(true);
        meetingRepository.save(meeting);
    }

    // 날짜 검증 메서드 추가
    private void validateTravelDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new MeetingException(ErrorCode.INVALID_TRAVEL_DATE);
        }
        if (startDate.isBefore(LocalDate.now())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_START_DATE);
        }
    }

    // 모임 수정
    public void updateMeeting(Long meetingId, UpdateMeeting.Request request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        if (!Objects.equals(user.getId(), meeting.getMeetingLeader().getId())) {
            throw new MeetingException(ErrorCode.USER_AND_MEETING_UNMATCHED);
        }

        // 날짜 검증 호출
        validateTravelDates(request.getTravelStartDate(), request.getTravelEndDate());

        meeting.setTripGroup(request);
        meetingRepository.save(meeting);
    }

    // 모임 참여
    public void joinMeeting(Long meetingId, JoinMeeting.Request request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        if (Objects.equals(user.getId(), meeting.getMeetingLeader().getId()))
            throw new MeetingException(ErrorCode.CREATED_BY_USER);

        meetingMemberRepository.save(MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .joinDate(LocalDate.now())
                .isLeader(false)
                .build());

        // 날짜 검증 호출
        validateTravelDates(request.getTravelStartDate(), request.getTravelEndDate());

        if (request.getTravelStartDate().isBefore(meeting.getTravelStartDate()) ||
                request.getTravelStartDate().isAfter(meeting.getTravelEndDate()) ||
                request.getTravelEndDate().isBefore(meeting.getTravelStartDate()) ||
                request.getTravelEndDate().isAfter(meeting.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_DATE);
        }

        // 이미 참여하고 있는 경우 예외 처리
        List<LocalDate> dailyParticipationEntities = dailyParticipationRepository.findParticipationDatesById(user.getId());
        for (LocalDate date : dailyParticipationEntities) {
            for (LocalDate travelDate = request.getTravelStartDate(); !travelDate.isAfter(request.getTravelEndDate()); travelDate = travelDate.plusDays(1)) {
                if (date.equals(travelDate)) {
                    throw new MeetingException(ErrorCode.ALREADY_JOINED_DATE);
                }
            }
        }

        for (LocalDate date = request.getTravelStartDate(); !date.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
            dailyParticipationRepository.save(DailyParticipation.builder()
                    .user(user)
                    .participationDate(date)
                    .build());
        }
    }
}
