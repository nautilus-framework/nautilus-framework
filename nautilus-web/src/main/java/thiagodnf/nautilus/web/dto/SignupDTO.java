package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.UniqueEmail;

@Value
public class SignupDTO {

	@NotBlank
	private String firstname;
	
	@NotBlank
	private String lastname;
	
	@NotBlank
	@Email
	@UniqueEmail
	private String email;

	@NotBlank
	@Size(min = 6)
	private String password;
}
