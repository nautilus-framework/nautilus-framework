package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	public User findByEmail(String email);
}
