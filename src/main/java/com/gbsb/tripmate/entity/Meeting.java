package com.gbsb.tripmate.entity;

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
    @JoinColumn(name = "id")
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
    private AgeGroup ageRange;

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
}