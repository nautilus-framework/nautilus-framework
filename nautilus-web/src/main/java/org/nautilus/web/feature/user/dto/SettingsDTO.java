package org.nautilus.web.feature.user.dto;

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
import org.nautilus.web.feature.user.model.Settings;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//@Value
public class SettingsDTO {
	
    @Min(1)
    @Max(10)
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
    
    private Settings settings;
    
    public String toString() {
        return Converter.toJson(this);
    }
}

