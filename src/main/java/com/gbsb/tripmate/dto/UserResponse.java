package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.AgeRange;
import com.gbsb.tripmate.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private Gender gender;
    private LocalDate birthdate;
    private AgeRange ageRange;
    private String name;
    private String introduce;

}
