package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;

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