package org.nautilus.web.service;

import java.util.UUID;

import org.nautilus.web.dto.SignupDTO;
import org.nautilus.web.model.Role;
import org.nautilus.web.model.User;
import org.nautilus.web.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;

	@Transactional
	public User create(SignupDTO signupDTO) {

		User unsavedUser = new User();

		unsavedUser.setEmail(signupDTO.getEmail());
		unsavedUser.setPassword(signupDTO.getPassword());
		unsavedUser.setRoleId(roleRepository.findByName(Role.USER).getId());
		unsavedUser.setFirstname(signupDTO.getFirstname());
		unsavedUser.setLastname(signupDTO.getLastname());
		unsavedUser.setConfirmationToken(UUID.randomUUID().toString());

		return userService.create(unsavedUser);
	}
}
