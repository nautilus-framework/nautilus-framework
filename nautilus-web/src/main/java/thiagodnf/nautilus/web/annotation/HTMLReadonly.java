package thiagodnf.nautilus.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import thiagodnf.nautilus.web.validator.HTMLReadonlyValidator;

@Documented
@Constraint(validatedBy = HTMLReadonlyValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLReadonly {
    
	String message() default "disabled";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}
