package thiagodnf.nautilus.web.config;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.Transactional;

import thiagodnf.nautilus.web.model.Role;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.RoleService;
import thiagodnf.nautilus.web.service.UserService;
import thiagodnf.nautilus.web.util.Privileges;

@Configuration
public class InitialDataConfiguration implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger LOGGER = LoggerFactory.getLogger(InitialDataConfiguration.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
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
		
		LOGGER.info("Creating the privilegies");

		Role adminRole = createRoleIfNotFound(Role.ADMIN, Privileges.getPrivilegies(), false);
		
		createRoleIfNotFound(Role.USER, new ArrayList<>(), false);

		LOGGER.info("Creating the user");
		
		User adminUser = new User();
		
		adminUser.setEmail(adminEmail);
		adminUser.setPassword(adminPassword);
		adminUser.getProfile().setFirstname(adminFirstname);
		adminUser.getProfile().setLastname(adminLastname);
		adminUser.setRole(adminRole);
		adminUser.setVisible(false);

		createUserIfNotFound(adminUser);
	}
	
	@Transactional
	private Role createRoleIfNotFound(String name, List<String> privileges, boolean isEditable) {

		Role role = roleService.findByName(name);

		if (role == null) {
			role = roleService.save(new Role(name, privileges, isEditable));
		}

		return role;
	}
	
	@Transactional
	private User createUserIfNotFound(User user) {

		if (userService.findByEmail(user.getEmail()) == null) {
			user = userService.signup(user);
		}

		return user;
	}
}
