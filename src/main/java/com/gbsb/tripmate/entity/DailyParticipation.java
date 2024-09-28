package com.gbsb.tripmate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Table(name = "daily_participation")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyParticipation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dailyParticipationId;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = false)
    private LocalDate participationDate;

    @ColumnDefault("false")
    private boolean isDeleted;
}
