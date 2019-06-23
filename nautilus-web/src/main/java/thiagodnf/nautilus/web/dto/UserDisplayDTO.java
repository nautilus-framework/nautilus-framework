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

@Value
public class UserDisplayDTO {
	
    @Min(1)
	@Max(10)
    @HTMLStep(1)
	@HTMLRequired
    @HTMLAutoFocus
    @HTMLSpellCheck("false")
    @HTMLAutoComplete("off")
    private int decimalPlaces;
	
	@NotBlank
	@Contains({"COMMA", "POINT"})
	private String decimalSeparator;
	
	@NotBlank
	@Contains({"en_US", "pt_BR"})
	private String language;
	
	public String toString() {
	    return Converter.toJson(this);
	}
}

