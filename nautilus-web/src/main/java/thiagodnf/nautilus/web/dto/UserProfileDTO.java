package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;

@Value
public class UserProfileDTO {
	
    @NotBlank
    @HTMLAutoFocus
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("given-name")
	private String firstname;

	@NotBlank
	@HTMLSpellCheck("false")
	@HTMLAutoComplete("family-name")
	private String lastname;
}

