package org.nautilus.web.config;

import org.modelmapper.ModelMapper;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
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
    
    @Value("${app.admin.name}")
    private String adminName;
    
    @Value("${app.admin.email}")
    private String adminEmail;
    
    @Value("${app.admin.password}")
    private String adminPassword;
    
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        
        LOGGER.info("Creating the default users and privilegies");

        if (userService.findByEmail(adminEmail) == null) {
            userService.create(adminName, adminEmail, adminPassword, Role.ADMIN);
        }
    }
}
