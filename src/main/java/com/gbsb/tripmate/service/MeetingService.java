package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.entity.MeetingEntity;
import com.gbsb.tripmate.repository.MeetingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MeetingService {
    private final MeetingRepository meetingRepository;

    public MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void updateTripGroup(Long groupId, UpdateMeeting.Request request) {
        MeetingEntity meetingEntity =  meetingRepository.findById(groupId)
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
}