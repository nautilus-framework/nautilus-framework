package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Population;

public interface PopulationRepository extends MongoRepository<Population, String> {

	public Population findByLastId(String lastId);
}
