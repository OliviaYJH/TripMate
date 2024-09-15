package com.gbsb.tripmate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "meeting_member")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MeetingMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingMemberId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "meeting_id", referencedColumnName = "meetingId")
    private Meeting meeting;

    @Column(nullable = false)
    private LocalDate joinDate;

    private Boolean isLeader;
}
