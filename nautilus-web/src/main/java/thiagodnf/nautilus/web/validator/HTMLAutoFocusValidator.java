package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.HTMLAutoFocus;

public class HTMLAutoFocusValidator implements ConstraintValidator<HTMLAutoFocus, Object> {

	@Override
	public void initialize(HTMLAutoFocus contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
