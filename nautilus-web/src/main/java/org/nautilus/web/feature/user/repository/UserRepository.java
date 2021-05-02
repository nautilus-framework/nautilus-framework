package org.nautilus.web.feature.user.repository;

import java.util.Optional;

import org.nautilus.web.feature.user.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{

    public Optional<User> findByEmail(String email);
}
