package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.JoinMeeting;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.DailyParticipationEntity;
import com.gbsb.tripmate.entity.MeetingEntity;
import com.gbsb.tripmate.entity.MeetingMemberEntity;
import com.gbsb.tripmate.entity.UserEntity;
import com.gbsb.tripmate.enums.AgeGroup;
import com.gbsb.tripmate.enums.ErrorCode;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import com.gbsb.tripmate.exception.MeetingException;
import com.gbsb.tripmate.repository.DailyParticipationRepository;
import com.gbsb.tripmate.repository.MeetingMemberRepository;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final DailyParticipationRepository dailyParticipationRepository;

    public void updateMeeting(Long meetingId, UpdateMeeting.Request request) {

        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        if (request.getTravelStartDate().isAfter(request.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_TRAVEL_DATE);
        }

        if (request.getTravelStartDate().isBefore(LocalDate.now())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_START_DATE);
        }

        meetingEntity.setTripGroup(request);
        meetingRepository.save(meetingEntity);
    }

    public void joinMeeting(
            Long meetingId,
            JoinMeeting.Request request
    ) {
        // memberId로 회원 찾기
        UserEntity userEntity = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MeetingException(ErrorCode.USER_NOT_FOUNT));

        // groupId로 참여할 모임 찾기
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new MeetingException(ErrorCode.MEETING_NOT_FOUNT));

        // 모임 멤버 테이블에 추가하기
        MeetingMemberEntity meetingMemberEntity
                = MeetingMemberEntity.builder()
                .userEntity(userEntity)
                .meetingEntity(meetingEntity)
                .joinDate(LocalDate.now())
                .isLeader(false)
                // 기본값은 false이고 모임을 생성할 때 모임 테이블과 모임멤버 테이블에 데이터 넣을 때 true로 하기
                .build();
        meetingMemberRepository.save(meetingMemberEntity);

        if (request.getTravelStartDate().isAfter(request.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_TRAVEL_DATE);
        }

        if (request.getTravelStartDate().isBefore(meetingEntity.getTravelStartDate()) ||
                request.getTravelStartDate().isAfter(meetingEntity.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_DATE);
        }

        if (request.getTravelEndDate().isBefore(meetingEntity.getTravelStartDate()) ||
                request.getTravelEndDate().isAfter(meetingEntity.getTravelEndDate())) {
            throw new MeetingException(ErrorCode.INVALID_MEETING_TRAVEL_DATE);
        }

        // 이미 어떠한 날에 참여하고 있는 경우 already joined
        List<LocalDate> dailyParticipationEntities = dailyParticipationRepository.findParticipationDatesByUserId(userEntity.getUserId());
        for (LocalDate date : dailyParticipationEntities) {
            for (LocalDate travelDate = request.getTravelStartDate(); !travelDate.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
                if (date.equals(travelDate)) {
                    throw new MeetingException(ErrorCode.ALREADY_JOINED_DATE);
                }
            }
        }

        // 부분 참여 테이블에 추가하기(시작일부터 마지막날까지 하루씩 추가)
        for (LocalDate date = request.getTravelStartDate(); !date.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
            DailyParticipationEntity dailyParticipationEntity
                    = DailyParticipationEntity.builder()
                    .userEntity(userEntity)
                    .participationDate(date)
                    .build();
            dailyParticipationRepository.save(dailyParticipationEntity);
        }
    }
}