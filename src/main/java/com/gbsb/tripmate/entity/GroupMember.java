package com.gbsb.tripmate.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;


@Entity
@Getter
public class GroupMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name =  "user_id", nullable = false)
    private User user;

    private LocalDate joinDate;
    private Boolean isLeader;
}
