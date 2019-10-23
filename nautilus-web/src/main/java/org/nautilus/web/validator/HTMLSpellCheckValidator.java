package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.HTMLSpellCheck;

public class HTMLSpellCheckValidator implements ConstraintValidator<HTMLSpellCheck, Object> {

	@Override
	public void initialize(HTMLSpellCheck contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
