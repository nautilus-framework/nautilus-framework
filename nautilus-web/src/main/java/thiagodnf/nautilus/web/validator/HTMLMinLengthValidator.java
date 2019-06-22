package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.HTMLMinLength;

public class HTMLMinLengthValidator implements ConstraintValidator<HTMLMinLength, Object> {

	@Override
	public void initialize(HTMLMinLength contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
