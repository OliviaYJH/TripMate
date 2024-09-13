package com.gbsb.tripmate.service;

import com.gbsb.tripmate.dto.GroupCreateRequest;
import com.gbsb.tripmate.entity.Group;
import com.gbsb.tripmate.repository.GroupMemberRepository;
import com.gbsb.tripmate.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final GroupMemberRepository groupMemberRepository;

    // 모임 생성
    public Group createGroup(Long userId, GroupCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("사용자를 찾을 수 없습니다."));

        Group group = Group.builder()
                .groupLeader(user)
                .meetingTitle(request.getMeetingTitle())
                .description(request.getDescription())
                .destination(request.getDestination())
                .gender(String.valueOf(request.getGender()))
                .ageRange(String.valueOf(request.getAgeRange()))
                .travelStyle(String.valueOf(request.getTravelStyle()))
                .travelStartDate(request.getTravelStartDate())
                .travelEndDate(request.getTravelEndDate())
                .memberMax(request.getMemberMax())
                .createdAt(LocalDateTime.now())
                .build();

        return groupRepository.save(group);
    }

    // 모임 삭제
    public void deleteGroup(Long userId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("모임을 찾을 수 없습니다."));

        if (!group.getGroupLeader().getUserId().eqauls(userId)) {
            throw new RuntimeException("모임장만 모임을 삭제할 수 있습니다.");
        }

        int memberCount = groupMemberRepository.countMembersByGroupId(groupId);

        // 모임장 외 모임멤버가 존재할 경우 삭제 제한
        if (memberCount > 1) {
            throw new RuntimeException("참여 멤버가 존재해 모임을 삭제할 수 없습니다.");
        }

        if (group.getTravelEndDate().isAfter(LocalDate.now())) {
            throw new RuntimeException("여행기간 중에는 모임을 삭제할 수 없습니다.");
        }

        groupRepository.delete(group);
    }
}
