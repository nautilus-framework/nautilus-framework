package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.HTMLAutoComplete;

public class HTMLAutoCompleteValidator implements ConstraintValidator<HTMLAutoComplete, Object> {

	@Override
	public void initialize(HTMLAutoComplete contains) {
		
	}

    @Override
    public boolean isValid(Object field, ConstraintValidatorContext cxt) {
        return true;
    }
}
