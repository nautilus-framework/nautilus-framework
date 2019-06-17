package thiagodnf.nautilus.web.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.model.Execution;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	List<ExecutionSimplifiedDTO> findByUserId(String userId);
	
	List<ExecutionSimplifiedDTO> findByShowToAllUsers(boolean isGlobal);
	
//	List<ExecutionSimplifiedDTO> findByParametersProblemId(String problemId);
	
//	List<ExecutionSimplified> findByParametersPluginIdAndParametersProblemIdAndSettingsName(String pluginId, String problemId, String name);
//	
//	List<ExecutionSimplified> findByParametersPluginIdAndParametersProblemId(String pluginId, String problemId);
	
//	List<ExecutionSimplified> findByParametersPluginId(String pluginId);
}
