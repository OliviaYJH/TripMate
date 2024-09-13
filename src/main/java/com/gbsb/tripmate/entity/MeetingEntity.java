package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.enums.AgeGroup;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity(name = "Meeting")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId; // 모임 id (PK)

    @ManyToOne
    @JoinColumn(name = "meeting_leader_id", nullable = false)
    private User meetingLeader; // 모임장 id

    private String meetingTitle; // 모임 제목
    private String description; // 모임 설명
    private String destination; // 여행지

    @Enumerated(EnumType.STRING)
    private Gender gender; // 성별 조건

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup; // 연령대 조건

    @Enumerated(EnumType.STRING)
    private TravelStyle travelStyle; // 여행 스타일

    private LocalDate travelStartDate; // 여행 시작일
    private LocalDate travelEndDate; // 여행 종료일
    private int memberMax; // 최대 참여자 수

    private LocalDateTime createdAt; // 모임 생성일
    private LocalDateTime updatedAt; // 모임 수정일

    private Boolean isDeleted = false;

}