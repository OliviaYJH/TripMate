package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.dto.UpdateTripGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class TripGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId; // 모임 id (PK)

//    @ManyToOne
//    @JoinColumn(name = "group_leader_id", nullable = false)
//    //private User groupLeader; // 모임장 id
//    private String groupLeader;

    private String meetingTitle; // 모임 제목
    private String description; // 모임 설명
    private String destination; // 여행지
    private String gender; // 성별 조건
    private String ageRange; // 연령대 조건
    private String travelStyle; // 여행 스타일

    private LocalDate travelStartDate; // 여행 시작일
    private LocalDate travelEndDate; // 여행 종료일
    private int memberMax; // 최대 참여자 수

    private LocalDateTime createdAt; // 모임 생성일
    private LocalDateTime updatedAt; // 모임 수정일

    public void setTripGroup(UpdateTripGroup.Request tripGroup) {
        this.meetingTitle = tripGroup.getMeetingTitle();
        this.description = tripGroup.getDescription();
        this.destination = tripGroup.getDestination();
        this.gender = tripGroup.getGender();
        this.ageRange = tripGroup.getAgeRange();
        this.travelStyle = tripGroup.getTravelStyle();
        this.memberMax = tripGroup.getMemberMax();
        this.travelStartDate = tripGroup.getTravelStartDate();
        this.travelEndDate = tripGroup.getTravelEndDate();
    }
}