package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final DailyParticipationRepository dailyParticipationRepository;

    // 모임 생성
    public Meeting createMeeting(Long id, MeetingCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

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

    // 모임 목록 조회
    public Page<MeetingResponse> getAllMeetings(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<Meeting> meetings = meetingRepository.findAll(pageable);

        return meetings.map(this::convertToDto);
    }

    private MeetingResponse convertToDto(Meeting meeting) {
        return new MeetingResponse(
                meeting.getMeetingId(),
                meeting.getMeetingTitle(),
                meeting.getMeetingDescription(),
                meeting.getDestination(),
                meeting.getMeetingLeader().getNickname(),
                meeting.getTravelStartDate(),
                meeting.getTravelEndDate()
        );
    }

    // 모임 삭제
    public void deleteMeeting(Long id, Long meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

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

    // 모임 탈퇴
    public void leaveMeeting(Long id, Long meetingId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() ->  new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        MeetingMember meetingMember = meetingMemberRepository.findByMeetingAndUser(meeting, user)
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        if (meetingMember.getIsLeader()) {
            throw new RuntimeException("모임장은 모임에서 탈퇴할 수 없습니다.");
        }

        List<LocalDate> participationDates = dailyParticipationRepository.findParticipationDatesById(user.getId());

        for (LocalDate participationDate : participationDates) {
            DailyParticipation dailyParticipation = dailyParticipationRepository.findByUserAndParticipationDate(user, participationDate)
                    .orElseThrow(() -> new RuntimeException("해당 날짜의 참여 데이터가 없습니다."));

            dailyParticipationRepository.delete(dailyParticipation);
        }

        meetingMemberRepository.delete(meetingMember);
    }

    // 멤버 내보내기
    public void removeMember(Long leaderId, Long meetingId, RemoveMemberRequest request) {
        // 모임장인지 확인
        Meeting meeting = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        if (!meeting.getMeetingLeader().getId().equals(leaderId)) {
            throw new RuntimeException("모임장만 멤버를 탈퇴시킬 수 있습니다.");
        }

        // 탈퇴할 멤버 확인
        MeetingMember member = meetingMemberRepository.findByMeetingAndUserId(meeting, request.getMeetingMemberId())
                .orElseThrow(() -> new RuntimeException("해당 사용자는 이 모임의 멤버가 아닙니다."));

        if (member.getIsLeader()) {
            throw new RuntimeException("모임장은 자신을 탈퇴시킬 수 없습니다.");
        }

        // 부분참여 테이블에서도 삭제
        List<LocalDate> participationDates = dailyParticipationRepository.findParticipationDatesById(member.getUser().getId());

        for (LocalDate participationDate : participationDates) {
            DailyParticipation dailyParticipation = dailyParticipationRepository.findByUserAndParticipationDate(member.getUser(), participationDate)
                    .orElseThrow(() -> new RuntimeException("해당 날짜의 참여 데이터가 없습니다."));

            dailyParticipationRepository.delete(dailyParticipation);
        }

        // 탈퇴사유 확인 위해 상태변경으로 구현
        member.setRemoveReason(request.getReason());
        member.setIsRemoved(true);
        meetingMemberRepository.save(member);
    }
}