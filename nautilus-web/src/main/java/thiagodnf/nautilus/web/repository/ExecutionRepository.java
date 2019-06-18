package thiagodnf.nautilus.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.model.Execution;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	List<ExecutionSimplifiedDTO> findByUserId(String userId);
	
	List<ExecutionSimplifiedDTO> findByShowToAllUsers(boolean isGlobal);
	
	Optional<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOById(String id);
	
}
