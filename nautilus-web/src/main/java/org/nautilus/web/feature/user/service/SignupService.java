package org.nautilus.web.feature.user.service;

import java.util.UUID;

import org.nautilus.web.feature.user.dto.SignupDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private EmailService emailService;
	
	@Value("${app.settings.confirmation-token}")
    private boolean useConfirmationToken;
	
	@Transactional
	public User create(SignupDTO signupDTO) {

		User unsavedUser = new User();

		unsavedUser.setEmail(signupDTO.getEmail());
		unsavedUser.setPassword(signupDTO.getPassword());
		unsavedUser.setConfirmationToken(UUID.randomUUID().toString());
		
		User saved = userService.create(unsavedUser);
		
		if (useConfirmationToken) {
            emailService.sendConfirmationMail(saved);
        }
		
		return saved;
	}
}
