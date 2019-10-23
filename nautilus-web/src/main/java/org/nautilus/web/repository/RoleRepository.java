package org.nautilus.web.repository;

import org.nautilus.web.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String>{

	public Role findByName(String name);
}
