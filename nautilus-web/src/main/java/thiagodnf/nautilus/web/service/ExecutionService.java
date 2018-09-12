package thiagodnf.nautilus.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.core.exception.ExecutionNotFoundException;
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
	
	public Execution findById(String executionId) {
		return this.executionRepository.findById(executionId).orElseThrow(ExecutionNotFoundException::new);
	}

	public List<Execution> findAll(String problemKey) {
		return this.executionRepository.findAll();
	}

	public List<IdsAndDatesOnly> findByProblemKey(String problemKey) {
		return this.executionRepository.findByParametersProblemKey(problemKey);
	}
	
	public void delete(Execution execution) {
		executionRepository.delete(execution);
	}
}
