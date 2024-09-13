package entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planItemId;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private TravelPlan travelPlan;

    @Column(nullable = false)
    private String itemName;

    @Lob
    private String itemDescription;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;
}
