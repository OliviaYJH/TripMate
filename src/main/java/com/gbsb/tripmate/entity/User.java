package com.gbsb.tripmate.entity;

import com.gbsb.tripmate.enums.AgeRange;
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
    private AgeRange ageRange;

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
            this.ageRange = getAgeGroupFromAge(age);
        }
    }

    private AgeRange getAgeGroupFromAge(int age) {
        if (age < 20) return AgeRange.TEEN;
        else if (age < 30) return AgeRange.TWENTIES;
        else if (age < 40) return AgeRange.THIRTIES;
        else if (age < 50) return AgeRange.FORTIES;
        else if (age < 60) return AgeRange.FIFTIES;
        else return AgeRange.SIXTIES_PLUS;
    }
}

