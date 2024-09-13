package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.MeetingCreateRequest;
import com.gbsb.tripmate.entity.User;
import com.gbsb.tripmate.entity.MeetingEntity;
import com.gbsb.tripmate.repository.MeetingMemberRepository;
import com.gbsb.tripmate.repository.MeetingRepository;
import com.gbsb.tripmate.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;
    private final UserRepository userRepository;
    private final MeetingMemberRepository meetingMemberRepository;

    // 모임 생성
    public MeetingEntity createMeeting(Long id, MeetingCreateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        MeetingEntity meeting = MeetingEntity.builder()
                .meetingLeader(user)
                .meetingTitle(request.getMeetingTitle())
                .description(request.getDescription())
                .destination(request.getDestination())
                .gender(request.getGender())
                .ageGroup(request.getAgeGroup())
                .travelStyle(request.getTravelStyle())
                .travelStartDate(request.getTravelStartDate())
                .travelEndDate(request.getTravelEndDate())
                .memberMax(request.getMemberMax())
                .createdAt(LocalDateTime.now())
                .build();

        return meetingRepository.save(meeting);
    }

    // 모임 삭제
    public void deleteMeeting(Long id, Long meetingId) {
        MeetingEntity meeting = meetingRepository.findById(meetingId)
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
        if (meeting.getTravelEndDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("여행기간 중에는 모임을 삭제할 수 없습니다.");
        }

        meeting.setIsDeleted(true);
        meetingRepository.save(meeting);
    }
}