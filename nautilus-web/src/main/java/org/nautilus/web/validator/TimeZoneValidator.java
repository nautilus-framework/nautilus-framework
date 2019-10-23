package org.nautilus.web.validator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.logging.log4j.util.Strings;
import org.nautilus.web.annotation.TimeZone;
import org.nautilus.web.util.TimeZones;

public class TimeZoneValidator implements ConstraintValidator<TimeZone, String> {

	@Override
	public void initialize(TimeZone contraint) {
		
	}

	@Override
	public boolean isValid(String timeZone, ConstraintValidatorContext cxt) {
		
	    if(Strings.isBlank(timeZone)) {
	        return false;
	    }
	    
		return TimeZones.getAvailableTimeZones().contains(timeZone);
	}
}
