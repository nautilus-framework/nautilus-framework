package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Value;

@Value
public class LoginDTO {

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 6)
	private String password;
}