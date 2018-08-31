package thiagodnf.nautilus.web.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Execution;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	public Execution findByLastId(String lastId);
}
