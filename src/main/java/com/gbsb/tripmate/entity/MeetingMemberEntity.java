package com.gbsb.tripmate.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "meeting_member")
public class MeetingMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberId;

    //    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false)
    // private User userId;
    private Long userId; // 사용자 아이디

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private MeetingEntity groupId; // 그룹 아이디

    private LocalDate joinDate; // 모임 참여 일자

    private Boolean isLeader; // 모임장 여부
}
