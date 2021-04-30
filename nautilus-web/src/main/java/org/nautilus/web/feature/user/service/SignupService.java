package org.nautilus.web.feature.user.service;

import java.util.UUID;

import org.nautilus.web.exception.ConfirmationTokenNotFoundException;
import org.nautilus.web.feature.user.dto.SignupDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.persistence.repository.UserRepository;
import org.nautilus.web.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private EmailService emailService;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
	@Value("${app.settings.use-confirmation-token}")
    private boolean useConfirmationToken;
	
    @Transactional
    public User create(SignupDTO signupDTO) {

        User unsavedUser = new User();

        unsavedUser.setId(null);
        unsavedUser.setEnabled(false);
        unsavedUser.setEmail(signupDTO.getEmail());
        unsavedUser.setPassword(passwordEncoder.encode(signupDTO.getPassword()));
        unsavedUser.setConfirmationToken(UUID.randomUUID().toString());

        User saved = userRepository.save(unsavedUser);

        if (useConfirmationToken) {
            emailService.sendConfirmationMail(saved);
        }

        return saved;
    }
	
	public User findByConfirmationToken(String confirmationToken) {
        return userRepository.findByConfirmationToken(confirmationToken).orElseThrow(ConfirmationTokenNotFoundException::new);
    }
	
	public void confirm(String token) {
	    
	    User user = findByConfirmationToken(token);
        
        user.setEnabled(true);
        
        userRepository.save(user);
    }
}
