package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.HTMLStep;

public class HTMLStepValidator implements ConstraintValidator<HTMLStep, Object> {

	@Override
	public void initialize(HTMLStep contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
