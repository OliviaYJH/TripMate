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

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final DailyParticipationRepository dailyParticipationRepository;

    public void updateMeeting(Long groupId, UpdateMeeting.Request request) {
        validateUpdateMeeting(request);

        MeetingEntity meetingEntity = meetingRepository.findById(groupId)
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

    private void validateUpdateMeeting(UpdateMeeting.Request request) {
        // 하나라도 Null이면 error 처리
        if (request.getMeetingTitle().isBlank() || request.getDescription().isBlank()
                || request.getDestination().isBlank() || request.getGender() == null || request.getMemberMax() <= 1)
            throw new MeetingException(ErrorCode.INVALID_INPUT);

        // enum은 타입 맞는지 확인
        if (!isValidGender(request.getGender()) || !isValidTravelStyle(request.getTravelStyle())
                || !isValidAgeGroup(request.getAgeRange()))
            throw new MeetingException(ErrorCode.INVALID_INPUT);
    }

    private boolean isValidGender(Gender gender) {
        return (gender == Gender.MALE || gender == Gender.FEMALE || gender == Gender.OTHER);
    }

    private boolean isValidTravelStyle(TravelStyle travelStyle) {
        return (travelStyle == TravelStyle.SHOPPING || travelStyle == TravelStyle.TOURISM || travelStyle == TravelStyle.HEALING || travelStyle == TravelStyle.EXPERIENCE);
    }

    private boolean isValidAgeGroup(AgeGroup ageGroup) {
        return (ageGroup == AgeGroup.TEENS || ageGroup == AgeGroup.TWENTIES || ageGroup == AgeGroup.THIRTIES ||
                ageGroup == AgeGroup.FORTIES || ageGroup == AgeGroup.FIFTIES || ageGroup == AgeGroup.SIXTIES_PLUS);
    }

}