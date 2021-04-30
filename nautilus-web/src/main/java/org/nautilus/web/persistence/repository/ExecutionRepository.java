package org.nautilus.web.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.nautilus.web.persistence.dto.ExecutionSimplifiedDTO;
import org.nautilus.web.persistence.model.Execution;
import org.nautilus.web.persistence.model.Execution.Visibility;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExecutionRepository extends MongoRepository<Execution, String> {
    
    List<Execution> findByProblemIdAndInstance(String problemId, String instance);
    
    Execution findByProblemIdAndInstanceAndAlgorithmId(String problemId, String instance, String algorithmId);
    
    List<ExecutionSimplifiedDTO> findByUserId(String userId);
	
	List<ExecutionSimplifiedDTO> findByUserIdAndSolutionsNotNull(String userId);
	
	List<ExecutionSimplifiedDTO> findByVisibility(Visibility visibility);
	
	Optional<ExecutionSimplifiedDTO> findExecutionSimplifiedDTOById(String id);
	
}
