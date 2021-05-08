package org.nautilus.web.repository;

import org.nautilus.web.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>{

	public User findByEmail(String email);
}
