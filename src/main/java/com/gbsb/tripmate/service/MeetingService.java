package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.MeetingEntity;
import com.gbsb.tripmate.entity.UserEntity;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;

    public void updateTripGroup(Long groupId, UpdateMeeting.Request request) {
        MeetingEntity meetingEntity = meetingRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("groupId가 없습니다."));

        meetingEntity.setTripGroup(request);
        meetingRepository.save(meetingEntity);
    }

    public void joinTripGroup(Long groupId, Long memberId) {
        // memberId로 회원 찾기

        // groupId로 참여할 모임 찾기

        // 모임 멤버 테이블에 추가하기

        // 부분 참여 테이블에 추가하기(시작일부터 마지막날까지 하루씩 추가)
    }

    public Optional<UserEntity> getMember(Long userId) {
        return userRepository.findById(userId);
    }
}