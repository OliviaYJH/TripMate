package com.gbsb.tripmate.service;

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

        if (request.getTravelStartDate().isAfter(request.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_TRAVEL_DATE);
        }

        if (request.getTravelStartDate().isBefore(LocalDate.now())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_START_DATE);
        }

        meeting.setTripGroup(request);
        meetingRepository.save(meeting);
    }

    public void joinMeeting(
            Long meetingId,
            JoinMeeting.Request request
    ) {
        // 회원 찾기
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        // groupId로 참여할 모임 찾기
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        // 내가 생성한 모임에는 참여 불가능
        if (Objects.equals(user.getId(), meeting.getMeetingLeader().getId()))
            throw new MeetingException(ErrorCode.CREATED_BY_USER);

        // 모임 멤버 테이블에 추가하기
        MeetingMember meetingMember
                = MeetingMember.builder()
                .user(user)
                .meeting(meeting)
                .joinDate(LocalDate.now())
                .isLeader(false)
                // 기본값은 false이고 모임을 생성할 때 모임 테이블과 모임멤버 테이블에 데이터 넣을 때 true로 하기
                .build();
        meetingMemberRepository.save(meetingMember);

        if (request.getTravelStartDate().isAfter(request.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_TRAVEL_DATE);
        }

        if (request.getTravelStartDate().isBefore(meeting.getTravelStartDate()) ||
                request.getTravelStartDate().isAfter(meeting.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_DATE);
        }

        if (request.getTravelEndDate().isBefore(meeting.getTravelStartDate()) ||
                request.getTravelEndDate().isAfter(meeting.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_DATE);
        }

        // 이미 어떠한 날에 참여하고 있는 경우 already joined
        List<LocalDate> dailyParticipationEntities = dailyParticipationRepository.findParticipationDatesById(user.getId());
        for (LocalDate date : dailyParticipationEntities) {
            for (LocalDate travelDate = request.getTravelStartDate(); !travelDate.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
                if (date.equals(travelDate)) {
                    throw new MeetingException(ErrorCode.ALREADY_JOINED_DATE);
                }
            }
        }

        // 부분 참여 테이블에 추가하기(시작일부터 마지막날까지 하루씩 추가)
        for (LocalDate date = request.getTravelStartDate(); !date.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
            DailyParticipation dailyParticipation
                    = DailyParticipation.builder()
                    .user(user)
                    .participationDate(date)
                    .build();
            dailyParticipationRepository.save(dailyParticipation);
        }
    }
}