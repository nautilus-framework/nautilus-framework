package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.UniqueEmail;
import org.nautilus.web.persistence.model.User;
import org.nautilus.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserService userService;
	
	@Override
	public void initialize(UniqueEmail contraint) {
		
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext cxt) {
		
		User user = userService.findByEmail(email);
		
		if (user == null) {
			return true;
		}
		
		return false;
	}
}
