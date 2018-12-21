package thiagodnf.nautilus.web.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;

public interface ExecutionRepository extends MongoRepository<Execution, String> {

	interface ExecutionSimplified {

		String getId();
		
		Date getDate();
		
		Parameters getParameters();
		
		Settings getSettings();
	}
	
	List<ExecutionSimplified> findByParametersProblemId(String problemId);
	
	List<ExecutionSimplified> findByParametersPluginIdAndParametersProblemIdAndSettingsName(String pluginId, String problemId, String name);
	
	List<ExecutionSimplified> findByParametersPluginIdAndParametersProblemId(String pluginId, String problemId);
	
	List<ExecutionSimplified> findByParametersPluginId(String pluginId);
}
