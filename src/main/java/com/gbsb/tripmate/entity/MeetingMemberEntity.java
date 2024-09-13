package com.gbsb.tripmate.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "meeting_member")
public class MeetingMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingMemberId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId; // 사용자 아이디

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private MeetingEntity meetingId; // 그룹 아이디

    private LocalDate joinDate; // 모임 참여 일자

    private Boolean isLeader; // 모임장 여부
}
