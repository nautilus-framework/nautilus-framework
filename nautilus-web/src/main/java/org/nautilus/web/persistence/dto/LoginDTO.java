package org.nautilus.web.persistence.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLSpellCheck;

import lombok.Value;

@Value
public class LoginDTO {

	@NotBlank
	@Email
	@HTMLAutoFocus
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("current-email")
	private String email;

	@NotBlank
	@Size(min = 6)
	@HTMLAutoComplete("current-password")
	private String password;
}