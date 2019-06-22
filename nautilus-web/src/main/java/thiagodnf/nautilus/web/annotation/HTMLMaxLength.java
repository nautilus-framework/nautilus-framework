package thiagodnf.nautilus.web.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import thiagodnf.nautilus.web.validator.HTMLMaxLengthValidator;

@Documented
@Constraint(validatedBy = HTMLMaxLengthValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface HTMLMaxLength {
    
	String message() default "HTMLMinLength";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    int value() default Integer.MAX_VALUE;
}
