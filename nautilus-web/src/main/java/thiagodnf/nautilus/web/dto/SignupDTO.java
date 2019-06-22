package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;
import thiagodnf.nautilus.web.annotation.UniqueEmail;

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
