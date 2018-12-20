package thiagodnf.nautilus.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.web.exception.ExecutionNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.repository.ExecutionRepository;
import thiagodnf.nautilus.web.repository.ExecutionRepository.IdsAndDatesOnly;

@Service
public class ExecutionService {
	
	@Autowired
	private ExecutionRepository executionRepository;
	
	public Execution save(Execution execution) {
		return this.executionRepository.save(execution);
	}
	
	@Cacheable("executions")
	public Execution findById(String executionId) {
		return this.executionRepository.findById(executionId)
				.orElseThrow(ExecutionNotFoundException::new);
	}

	public List<Execution> findAll(String problemKey) {
		return this.executionRepository.findAll();
	}

	public List<IdsAndDatesOnly> findByProblemId(String problemId) {
		return this.executionRepository.findByParametersProblemId(problemId);
	}
	
	public void deleteById(String executionId) {
		executionRepository.deleteById(executionId);
	}
	
	public boolean existsById(String executionId) {
		return executionRepository.existsById(executionId);
	}

	public List<IdsAndDatesOnly> findByPluginIdAndProblemId(String pluginId, String problemId) {
		return this.executionRepository.findByParametersPluginIdAndParametersProblemId(pluginId, problemId);
	}
	
	public List<IdsAndDatesOnly> findByPluginId(String pluginId) {
		return this.executionRepository.findByParametersPluginId(pluginId);
	}
}
