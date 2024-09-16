package entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import enums.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column
    private String userNickname;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column
    private LocalDate birthdate;

    @Column
    private String userName;

    @Transient
    private Integer ageRange;

    @Lob
    private String introduce;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime removedDate;

    @Column
    private LocalDateTime modifiedDate;


    public Integer getAgeRange() {
        if (this.birthdate != null) {
            return Period.between(this.birthdate, LocalDate.now()).getYears() / 10 * 10;
        }
        return null;
    }


}
