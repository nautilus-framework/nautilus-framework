package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Value;

@Value
public class PreferencesDTO {
	
	private String id;

	@Min(0)
	@Max(10)
	private int decimalPlaces;
	
	@NotBlank
	private String language;
}

