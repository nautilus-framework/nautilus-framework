package thiagodnf.nautilus.web.service;

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

	public void save(User user) {

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		
		userRepository.save(user);
	}

	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean contains(String email) {
		return findByEmail(email) != null;
	}
}
