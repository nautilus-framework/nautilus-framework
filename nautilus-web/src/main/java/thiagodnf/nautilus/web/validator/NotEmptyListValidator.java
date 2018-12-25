package thiagodnf.nautilus.web.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import thiagodnf.nautilus.web.annotation.NotEmptyList;

public class NotEmptyListValidator implements ConstraintValidator<NotEmptyList, List<?>> {

	@Override
	public void initialize(NotEmptyList contraint) {
		
	}

	@Override
	public boolean isValid(List<?> list, ConstraintValidatorContext cxt) {
		return list != null && !list.isEmpty();
	}
}
