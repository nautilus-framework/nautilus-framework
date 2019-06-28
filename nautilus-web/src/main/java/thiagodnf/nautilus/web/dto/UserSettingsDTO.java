package thiagodnf.nautilus.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Value;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.web.annotation.Contains;
import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;
import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;
import thiagodnf.nautilus.web.annotation.HTMLRequired;
import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;
import thiagodnf.nautilus.web.annotation.HTMLStep;
import thiagodnf.nautilus.web.annotation.TimeZone;

@Value
public class UserSettingsDTO {
	
    @NotBlank
    @HTMLAutoFocus
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("given-name")
	private String firstname;

	@NotBlank
	@HTMLSpellCheck("false")
	@HTMLAutoComplete("family-name")
	private String lastname;
	
	@Min(1)
    @Max(10)
    @HTMLStep(1)
    @HTMLRequired
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("off")
    private int decimalPlaces;
    
    @NotBlank
    @Contains({"COMMA", "POINT"})
    private String decimalSeparator;
    
    @NotBlank
    @Contains({"en_US", "pt_BR"})
    private String language;
    
    @NotBlank
    @TimeZone
    private String timeZone;
    
    public String toString() {
        return Converter.toJson(this);
    }
}

