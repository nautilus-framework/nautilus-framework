package org.nautilus.web.persistence.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.nautilus.core.util.Converter;
import org.nautilus.web.annotation.Contains;
import org.nautilus.web.annotation.HTMLAutoComplete;
import org.nautilus.web.annotation.HTMLAutoFocus;
import org.nautilus.web.annotation.HTMLRequired;
import org.nautilus.web.annotation.HTMLSpellCheck;
import org.nautilus.web.annotation.HTMLStep;
import org.nautilus.web.annotation.TimeZone;

import lombok.Value;

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

