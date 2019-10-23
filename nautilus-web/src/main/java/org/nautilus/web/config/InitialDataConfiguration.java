package org.nautilus.web.config;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.web.model.Role;
import org.nautilus.web.model.User;
import org.nautilus.web.repository.RoleRepository;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Privileges;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class InitialDataConfiguration implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataConfiguration.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Value("${admin.email}")
	private String adminEmail;
	
	@Value("${admin.password}")
	private String adminPassword;
	
	@Value("${admin.firstname}")
	private String adminFirstname;
	
	@Value("${admin.lastname}")
	private String adminLastname;
	
	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		LOGGER.info("Creating the default users and privilegies");

		createRoleIfNotFound(Role.ADMIN, Privileges.getPrivilegies(), false);
		createRoleIfNotFound(Role.USER, new ArrayList<>(), false);

		User adminUser = new User();
		
		adminUser.setEmail(adminEmail);
		adminUser.setPassword(adminPassword);
		adminUser.setFirstname(adminFirstname);
		adminUser.setLastname(adminLastname);
		adminUser.setRoleId(roleRepository.findByName(Role.ADMIN).getId());
		adminUser.setEditable(false);
		adminUser.setEnabled(true);
		adminUser.setMaxExecutions(Integer.MAX_VALUE);

		createUserIfNotFound(adminUser);
	}
	
	@Transactional
	private Role createRoleIfNotFound(String name, List<String> privileges, boolean isEditable) {

		Role role = roleRepository.findByName(name);

		if (role == null) {

			role = new Role();

			role.setName(name);
			role.setPrivileges(privileges);
			role.setEditable(isEditable);

			return roleRepository.save(role);
		}

		return role;
	}
	
	@Transactional
	private User createUserIfNotFound(User user) {

		if (userService.findByEmail(user.getEmail()) == null) {
			user = userService.create(user);
		}

		return user;
	}
}
