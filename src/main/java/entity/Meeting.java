package entity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import java.time.LocalDateTime;
import Enum.*;

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
    private GenderCondition genderCondition;

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
}
