package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.HTMLRequired;

public class HTMLRequiredValidator implements ConstraintValidator<HTMLRequired, Object> {

	@Override
	public void initialize(HTMLRequired contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
