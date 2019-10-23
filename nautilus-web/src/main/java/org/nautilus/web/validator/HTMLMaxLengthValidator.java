package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.HTMLMaxLength;

public class HTMLMaxLengthValidator implements ConstraintValidator<HTMLMaxLength, Object> {

	@Override
	public void initialize(HTMLMaxLength contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
