package org.nautilus.web.config;

import org.nautilus.web.model.User;
import org.nautilus.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableMongoAuditing
public class DatabaseConfiguration implements ApplicationListener<ContextRefreshedEvent>{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);
	
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

        User adminUser = new User();
        
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(adminPassword);
        adminUser.setFirstname(adminFirstname);
        adminUser.setLastname(adminLastname);
        adminUser.setAdmin(true);
        adminUser.setEditable(false);
        adminUser.setEnabled(true);
        adminUser.setMaxExecutions(Integer.MAX_VALUE);

        createUserIfNotFound(adminUser);
    }
    
    private User createUserIfNotFound(User user) {

        if (userService.findByEmail(user.getEmail()) == null) {
            user = userService.create(user);
        }

        return user;
    }
}
