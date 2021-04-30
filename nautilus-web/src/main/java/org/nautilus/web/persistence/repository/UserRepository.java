package org.nautilus.web.persistence.repository;

import java.util.Optional;

import org.nautilus.web.feature.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{

	public User findByEmail(String email);
	
	public Optional<User> findByConfirmationToken(String confirmationToken);
}
