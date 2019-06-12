package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Privilege;

public interface PrivilegeRepository extends MongoRepository<Privilege, String>{

	public Privilege findByName(String name);
	
}
