package org.nautilus.web.feature.user.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.nautilus.web.feature.user.constant.DecimalSeparator;
import org.nautilus.web.feature.user.constant.Language;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SettingsDTO {
	
    @Min(0)
    @Max(10)
    protected int decimalPlaces;
    
    @NotNull
    protected DecimalSeparator decimalSeparator;
    
    @NotNull
    protected Language language;
}

