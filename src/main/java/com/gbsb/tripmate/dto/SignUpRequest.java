package com.gbsb.tripmate.dto;

import com.gbsb.tripmate.enums.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @NotBlank
    @Size(min = 3, max = 15)
    private String nickname;

    @NotNull
    private Gender gender;

    @NotBlank
    private String birthdate;

    @NotBlank
    @Size(max = 40)
    private String name;

    private String introduce;
}
