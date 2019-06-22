package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.HTMLSpellCheck;

public class HTMLSpellCheckValidator implements ConstraintValidator<HTMLSpellCheck, Object> {

	@Override
	public void initialize(HTMLSpellCheck contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
