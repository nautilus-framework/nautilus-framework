package org.nautilus.web.feature.user.service;

import org.nautilus.web.feature.user.constant.Role;
import org.nautilus.web.feature.user.dto.SignupDTO;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignupService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private BCryptPasswordEncoder passwordEncoder;
	
    public User create(SignupDTO signupDTO) {
        return create(signupDTO.getEmail(), signupDTO.getPassword(), Role.USER);
    }
	
    public User create(String email, String password, Role role) {

        User unsavedUser = new User();

        unsavedUser.setId(null);
        unsavedUser.setEnabled(true);
        unsavedUser.setEmail(email);
        unsavedUser.setRole(role);
        unsavedUser.setPassword(passwordEncoder.encode(password));

        return userRepository.save(unsavedUser);
    }
}
