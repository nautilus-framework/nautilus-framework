package org.nautilus.web.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.NotNullList;

public class NotNullListValidator implements ConstraintValidator<NotNullList, List<?>> {

	@Override
	public void initialize(NotNullList contraint) {
		
	}

	@Override
	public boolean isValid(List<?> list, ConstraintValidatorContext cxt) {
		return list != null;
	}
}
