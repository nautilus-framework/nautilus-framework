package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.HTMLReadonly;

public class HTMLReadonlyValidator implements ConstraintValidator<HTMLReadonly, Object> {

	@Override
	public void initialize(HTMLReadonly contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
