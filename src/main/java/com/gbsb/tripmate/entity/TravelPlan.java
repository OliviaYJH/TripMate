package com.gbsb.tripmate.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plan")
public class TravelPlan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long travelPlanId;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    private String planTitle;

    @Column(nullable = false)
    private LocalDate planDate;

    @ColumnDefault("false")
    private boolean isDeleted;
}
