package thiagodnf.nautilus.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	public User findByEmail(String email);
	
	public Optional<User> findByConfirmationToken(String confirmationToken);
	
	public List<User> findByRoleId(String id);
}
