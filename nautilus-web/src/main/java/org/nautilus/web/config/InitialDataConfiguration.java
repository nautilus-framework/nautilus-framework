package org.nautilus.web.config;

import org.nautilus.web.feature.user.constant.Role;
import org.nautilus.web.feature.user.model.User;
import org.nautilus.web.feature.user.service.UserService;
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

		User user = new User();
		
		user.setEmail(adminEmail);
		user.setPassword(adminPassword);
		user.setRole(Role.ADMIN);
		user.setEnabled(true);
		
		createUserIfNotFound(user);
	}
	
	@Transactional
	private User createUserIfNotFound(User user) {

		if (userService.findByEmail(user.getEmail()) == null) {
			user = userService.create(user);
		}

		return user;
	}
}
