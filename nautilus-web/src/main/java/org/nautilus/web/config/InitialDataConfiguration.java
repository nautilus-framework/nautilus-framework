package org.nautilus.web.config;

import org.nautilus.web.feature.user.constant.Role;
import org.nautilus.web.feature.user.repository.UserRepository;
import org.nautilus.web.feature.user.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;

@Configuration
public class InitialDataConfiguration implements ApplicationListener<ContextRefreshedEvent>{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private SignupService signupService;
    
	@Value("${app.admin.email}")
	private String adminEmail;
	
	@Value("${app.admin.password}")
	private String adminPassword;
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
	    if (userRepository.findByEmail(adminEmail).isPresent()) {
            return;
        }

	    signupService.create(adminEmail, adminPassword, Role.ADMIN);
	}
}
