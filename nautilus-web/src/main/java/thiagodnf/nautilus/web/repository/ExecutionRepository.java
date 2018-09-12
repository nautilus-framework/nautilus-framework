package thiagodnf.nautilus.web.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Execution;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	interface IdsAndDatesOnly {

		String getId();
		
		String getName();
		
		Date getDate();
	}
	
	List<IdsAndDatesOnly> findByParametersProblemKey(String problemKey);
}
