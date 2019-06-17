package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Role;

public interface RoleRepository extends MongoRepository<Role, String>{

	public Role findByName(String name);
}
