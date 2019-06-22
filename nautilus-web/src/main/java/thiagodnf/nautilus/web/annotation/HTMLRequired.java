package thiagodnf.nautilus.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import thiagodnf.nautilus.web.validator.HTMLRequiredValidator;

@Documented
@Constraint(validatedBy = HTMLRequiredValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLRequired {
    
	String message() default "required";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    String value() default "";
}
