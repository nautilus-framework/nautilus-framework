package thiagodnf.nautilus.web.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Settings;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	interface IdsAndDatesOnly {

		String getId();
		
		Date getDate();
		
		Settings getSettings();
	}
	
	List<IdsAndDatesOnly> findByParametersProblemKey(String problemKey);
}
