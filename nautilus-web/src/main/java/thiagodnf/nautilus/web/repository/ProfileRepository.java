package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Profile;

public interface ProfileRepository extends MongoRepository<Profile, String>{

}
