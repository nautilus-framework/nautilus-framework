package org.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.nautilus.web.annotation.UniqueEmail;
import org.nautilus.web.feature.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void initialize(UniqueEmail contraint) {
	}

	@Override
	public boolean isValid(String email, ConstraintValidatorContext cxt) {
	    return !userRepository.findByEmail(email).isPresent();		
	}
}
