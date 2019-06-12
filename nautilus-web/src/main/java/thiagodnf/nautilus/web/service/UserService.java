package thiagodnf.nautilus.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	public MongoTemplate mongoTemplate;

	@Autowired
	private ProfileService profileService;
	
	public User signup(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setProfile(profileService.save(user.getProfile()));
		
		return userRepository.save(user);
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	public User findByConfirmationToken(String confirmationToken) {
		return userRepository.findByConfirmationToken(confirmationToken);
	}

	public boolean contains(String email) {
		return findByEmail(email) != null;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
}
