package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.enums.AgeGroup;
import com.gbsb.tripmate.enums.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgeGroup ageGroup;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    private LocalDateTime createdDate;
    private LocalDateTime removedDate;
    private LocalDateTime modifiedDate;

    @PrePersist
    @PreUpdate
    private void calculateAgeGroup() {
        if (this.birthdate != null) {
            int age = LocalDate.now().getYear() - this.birthdate.getYear();
            this.ageGroup = getAgeGroupFromAge(age);
        }
    }

    private AgeGroup getAgeGroupFromAge(int age) {
        if (age < 20) return AgeGroup.TEENS;
        else if (age < 30) return AgeGroup.TWENTIES;
        else if (age < 40) return AgeGroup.THIRTIES;
        else if (age < 50) return AgeGroup.FORTIES;
        else if (age < 60) return AgeGroup.FIFTIES;
        else return AgeGroup.SIXTIES_PLUS;
    }
}
