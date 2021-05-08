package org.nautilus.web.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.nautilus.core.util.Converter;
import org.nautilus.web.annotation.Contains;
import org.nautilus.web.annotation.TimeZone;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SettingsDTO {
	
    @NotBlank
    protected String name;

	@Min(1)
    @Max(10)
	protected int decimalPlaces;
    
    @NotBlank
    @Contains({"COMMA", "POINT"})
    protected String decimalSeparator;
    
    @NotBlank
    @Contains({"en_US", "pt_BR"})
    protected String language;
    
    @NotBlank
    @TimeZone
    protected String timeZone = "";
    
    public String toString() {
        return Converter.toJson(this);
    }
}

