package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class SettingsProfileDTO {
	
    @NotBlank
	private String id;

	@NotBlank
	private String firstname;

	@NotBlank
	private String lastname;
}

