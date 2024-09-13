package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.JoinMeeting;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.DailyParticipationEntity;
import com.gbsb.tripmate.entity.MeetingEntity;
import com.gbsb.tripmate.entity.MeetingMemberEntity;
import com.gbsb.tripmate.entity.UserEntity;
import com.gbsb.tripmate.repository.DailyParticipationRepository;
import com.gbsb.tripmate.repository.MeetingMemberRepository;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final MeetingMemberRepository meetingMemberRepository;
    private final DailyParticipationRepository dailyParticipationRepository;

    public void updateTripGroup(Long groupId, UpdateMeeting.Request request) {
        MeetingEntity meetingEntity = meetingRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("groupId가 없습니다."));

        meetingEntity.setTripGroup(request);
        meetingRepository.save(meetingEntity);
    }

    public void joinMeeting(
            Long meetingId,
            JoinMeeting.Request request
    ) {
        // memberId로 회원 찾기
        UserEntity userEntity = userRepository.findById(request.getMemberId())
                .orElseThrow(() -> new RuntimeException("해당하는 유저가 없습니다."));

        // groupId로 참여할 모임 찾기
        MeetingEntity meetingEntity = meetingRepository.findById(meetingId)
                .orElseThrow(() -> new RuntimeException("해당하는 모임이 없습니다."));

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

        // 부분 참여 테이블에 추가하기(시작일부터 마지막날까지 하루씩 추가)
        for (LocalDate date = request.getTravelStartDate(); !date.isAfter(request.getTravelEndDate()); date = date.plusDays(1)) {
            System.out.println("date = " + date);
            // 참여하려는 모임의 여행일 사이의 날짜인지 확인

            DailyParticipationEntity dailyParticipationEntity
                    = DailyParticipationEntity.builder()
                    .userEntity(userEntity)
                    .participationDate(date)
                    .build();
            dailyParticipationRepository.save(dailyParticipationEntity);
        }
    }

    public Optional<UserEntity> getMember(Long userId) {
        return userRepository.findById(userId);
    }
}