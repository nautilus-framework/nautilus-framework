package thiagodnf.nautilus.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import thiagodnf.nautilus.web.annotation.UniqueEmail;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.UserService;

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
