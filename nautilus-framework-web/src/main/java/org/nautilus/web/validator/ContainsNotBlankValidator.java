package org.nautilus.web.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.ContainsNotBlank;

public class ContainsNotBlankValidator implements ConstraintValidator<ContainsNotBlank, List<?>> {

	 @Override
    public boolean isValid(List<?> field, ConstraintValidatorContext cxt) {

        for (Object item : field) {

            if (item == null) {
                return false;
            }

            if (item instanceof List) {
                
                if(((List<?>)item).isEmpty()) {
                    return false;
                }
            }
        }
        
        return true;
    }
}
