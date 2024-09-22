package com.gbsb.tripmate.entity;
import com.gbsb.tripmate.dto.UpdateMeeting;
import com.gbsb.tripmate.enums.AgeRange;
import com.gbsb.tripmate.enums.Gender;
import com.gbsb.tripmate.enums.TravelStyle;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "group_leader_id")
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
    private AgeRange ageRange;

    @Column
    @Enumerated(EnumType.STRING)
    private TravelStyle travelStyle;

    @Lob
    private String destination;

    @Column
    private Integer memberMax;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    @Column
    private LocalDateTime travelStartDate;

    @Column
    private LocalDateTime travelEndDate;

    public void setTripGroup(UpdateMeeting.Request tripGroup) {
        this.meetingTitle = tripGroup.getMeetingTitle();
        this.meetingDescription = tripGroup.getDescription();
        this.destination = tripGroup.getDestination();
        this.genderCondition = tripGroup.getGender();
        this.ageRange = tripGroup.getAgeRange();
        this.travelStyle = tripGroup.getTravelStyle();
        this.memberMax = tripGroup.getMemberMax();
        this.travelStartDate = tripGroup.getTravelStartDate().atStartOfDay();
        this.travelEndDate = tripGroup.getTravelEndDate().atStartOfDay();
    }
}