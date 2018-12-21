package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.SolutionListUtils;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.web.exception.ExecutionNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.repository.ExecutionRepository;
import thiagodnf.nautilus.web.repository.ExecutionRepository.ExecutionSimplified;

@Service
public class ExecutionService {
	
	@Autowired
	private ExecutionRepository executionRepository;
	
	public Execution save(Execution execution) {
		return this.executionRepository.save(execution);
	}
	
	public Execution findById(String executionId) {
		return this.executionRepository.findById(executionId)
				.orElseThrow(ExecutionNotFoundException::new);
	}

	public List<Execution> findAll(String problemKey) {
		return this.executionRepository.findAll();
	}

	public List<ExecutionSimplified> findByProblemId(String problemId) {
		return this.executionRepository.findByParametersProblemId(problemId);
	}
	
	public List<ExecutionSimplified> findByName(String pluginId, String problemId, String name) {
		return executionRepository.findByParametersPluginIdAndParametersProblemIdAndSettingsName(pluginId, problemId, name);
	}
	
	public void deleteById(String executionId) {
		executionRepository.deleteById(executionId);
	}
	
	public boolean existsById(String executionId) {
		return executionRepository.existsById(executionId);
	}

	public List<ExecutionSimplified> findByPluginIdAndProblemId(String pluginId, String problemId) {
		return this.executionRepository.findByParametersPluginIdAndParametersProblemId(pluginId, problemId);
	}
	
	public List<ExecutionSimplified> findByPluginId(String pluginId) {
		return this.executionRepository.findByParametersPluginId(pluginId);
	}

	public void generateApproxParetoFront(String pluginId, String problemId) {

		List<ExecutionSimplified> executions = findByPluginIdAndProblemId(pluginId, problemId);

		Map<String, List<ExecutionSimplified>> map = new HashMap<>();

		for (ExecutionSimplified e : executions) {

			String filename = e.getParameters().getFilename();

			if (!map.containsKey(filename)) {
				map.put(filename, new ArrayList<>());
			}

			map.get(filename).add(e);
		}

		for (Entry<String, List<ExecutionSimplified>> entry : map.entrySet()) {
			generateApproxParetoFront(pluginId, problemId, entry.getKey(), entry.getValue());
		}
	}
	
	public void generateApproxParetoFront(String pluginId, String problemId, String filename, List<ExecutionSimplified> executions) {
		
		Map<String, List<ExecutionSimplified>> map = new HashMap<>();
		
		for (ExecutionSimplified e : executions) {

			String objectives = e.getParameters()
					.getObjectiveIds()
					.stream()
					.sorted()
					.reduce((a,b) -> a + "-" + b).get();

			if (!map.containsKey(objectives)) {
				map.put(objectives, new ArrayList<>());
			}

			map.get(objectives).add(e);
		}
		
		for (Entry<String, List<ExecutionSimplified>> entry : map.entrySet()) {
			generateApproxParetoFront(pluginId, problemId, filename, entry.getKey(), entry.getValue());
		}
	}
	
	public void generateApproxParetoFront(String pluginId, String problemId, String filename, String objectives, List<ExecutionSimplified> simplifiedExecutions) {
		
		List<Execution> executions = new ArrayList<>();

		for (ExecutionSimplified e : simplifiedExecutions) {
			executions.add(findById(e.getId()));
		}

		List<Solution<?>> allSolutions = new ArrayList<>();

		for (Execution e : executions) {
			allSolutions.addAll(e.getSolutions());
		}

		List<Solution<?>> nonDominateds = SolutionListUtils.getNondominatedSolutions(allSolutions);
		
		String name = "PF-" + problemId + "-" + filename + "-" + objectives;
		
		Execution execution = new Execution();
		
		execution.setParameters(executions.get(0).getParameters());
		execution.setSolutions(Converter.toGenericSolutions(nonDominateds));
		execution.getSettings().setName("PF-" + problemId + "-" + filename + "-" + objectives);
		
		List<ExecutionSimplified> oldExecutions = findByName(pluginId, problemId, name);

		for (ExecutionSimplified e : oldExecutions) {
			deleteById(e.getId());
		}

		execution = save(execution);
	}
}
