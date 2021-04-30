package org.nautilus.web.feature.user.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class LoginDTO {

	@Email
	@NotBlank
	private String email;

	@NotBlank
	@Size(min = 6)
	private String password;
}