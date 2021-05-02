package org.nautilus.web.feature.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.nautilus.web.annotation.UniqueEmail;

import lombok.Value;

@Value
public class SignupDTO {

    @Email
    @NotBlank
    @UniqueEmail
	private String email;

    @NotBlank
    @Size(min = 6)
	private String password;
}
