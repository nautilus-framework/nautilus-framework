package thiagodnf.nautilus.web.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Execution.Visibility;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	List<ExecutionSimplifiedDTO> findByUserId(String userId);
	
	List<ExecutionSimplifiedDTO> findByUserIdAndSolutionsNotNull(String userId);
	
	List<ExecutionSimplifiedDTO> findByVisibility(Visibility visibility);
	
	Optional<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOById(String id);
	
}
