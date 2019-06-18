package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Value;
import thiagodnf.nautilus.web.annotation.Contains;

@Value
public class SettingsPreferencesDTO {
	
    @NotBlank
	private String id;

	@Min(1)
	@Max(10)
	private int decimalPlaces;
	
	@NotBlank
	@Contains({"COMMA", "POINT"})
	private String decimalSeparator;
	
	@NotBlank
	@Contains({"en_US", "pt_BR"})
	private String language;
}

