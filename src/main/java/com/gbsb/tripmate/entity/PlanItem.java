package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.enums.TravelStyle;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "plan_item")
public class PlanItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planItemId;

    @ManyToOne
    @JoinColumn(name = "travel_plan_id", nullable = false)
    private TravelPlan travelPlan;

    private String itemName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TravelStyle itemType;

    @Lob
    private String itemDescription;

    @Column(nullable = false)
    private LocalTime startTime;
    private LocalTime endTime;

    @Column(nullable = false)
    private BigDecimal x;
    @Column(nullable = false)
    private BigDecimal y;

    private int itemOrder;

    @ColumnDefault("false")
    private boolean isDeleted;
}
