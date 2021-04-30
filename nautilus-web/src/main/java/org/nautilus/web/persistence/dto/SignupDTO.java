package org.nautilus.web.persistence.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLSpellCheck;
import org.nautilus.web.annotation.UniqueEmail;

import lombok.Value;

@Value
public class SignupDTO {

	@NotBlank
	@HTMLAutoFocus
	@HTMLSpellCheck("false")
    @HTMLAutoComplete("given-name")
	private String firstname;
	
	@NotBlank
	@HTMLSpellCheck("false")
    @HTMLAutoComplete("family-name")
	private String lastname;
	
	@NotBlank
	@Email
	@UniqueEmail
	@HTMLSpellCheck("false")
	@HTMLAutoComplete("email")
	private String email;

	@NotBlank
	@Size(min = 6)
	@HTMLAutoComplete("new-password")
	private String password;
}
