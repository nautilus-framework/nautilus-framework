package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.Contains;

public class ContainsValidator implements ConstraintValidator<Contains, String> {

	private Contains contains;

	@Override
	public void initialize(Contains contains) {
		this.contains = contains;
	}

    @Override
    public boolean isValid(String field, ConstraintValidatorContext cxt) {

        String[] values = contains.value();

        if (values == null || values.length == 0) {
            return true;
        }

        for (int i = 0; i < values.length; i++) {
            
            if (values[i].equalsIgnoreCase(field)) {
                return true;
            }
        }

        return false;
    }
}
