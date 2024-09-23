package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.enums.AgeGroup;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meetingId;

    @ManyToOne
    @JoinColumn(name = "meeting_leader_id", nullable = false)
    private User meetingLeader;

    @Column(nullable = false)
    private String meetingTitle;

    @Lob
    private String meetingDescription;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender genderCondition;

    @Column
    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Column
    @Enumerated(EnumType.STRING)
    private TravelStyle travelStyle;

    @Lob
    private String destination;

    @Column
    private Integer memberMax;

    @Column
    private LocalDate createdDate;

    @Column
    private LocalDate updatedDate;

    @Column
    private LocalDate travelStartDate;

    @Column
    private LocalDate travelEndDate;

    private Boolean isDeleted = false;
  
    public void setTripGroup(UpdateMeeting.Request tripGroup) {
        this.meetingTitle = tripGroup.getMeetingTitle();
        this.meetingDescription = tripGroup.getDescription();
        this.destination = tripGroup.getDestination();
        this.genderCondition = tripGroup.getGender();
        this.ageRange = tripGroup.getAgeRange();
        this.travelStyle = tripGroup.getTravelStyle();
        this.memberMax = tripGroup.getMemberMax();
        this.travelStartDate = tripGroup.getTravelStartDate();
        this.travelEndDate = tripGroup.getTravelEndDate();
    }
}