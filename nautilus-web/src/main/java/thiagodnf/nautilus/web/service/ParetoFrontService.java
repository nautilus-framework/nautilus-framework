package thiagodnf.nautilus.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.util.SolutionListUtils;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.repository.ExecutionRepository.ExecutionSimplified;

@Service
public class ParetoFrontService {
	
	@Autowired
	private ExecutionService executionService;
	
	public void generateApproxParetoFront(String pluginId, String problemId) {

		List<ExecutionSimplified> executions = executionService.findByPluginIdAndProblemId(pluginId, problemId);

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
			executions.add(executionService.findById(e.getId()));
		}

		List<NSolution<?>> allSolutions = new ArrayList<>();

		for (Execution e : executions) {
			allSolutions.addAll(e.getSolutions());
		}

		List<NSolution<?>> nonDominateds = SolutionListUtils.getNondominatedSolutions(allSolutions);
		
		for (int i = 0; i < nonDominateds.size(); i++) {
			nonDominateds.get(i).getAttributes().clear();
			nonDominateds.get(i).setAttribute(SolutionAttribute.ID, String.valueOf(i));
		}
		
		String name = getParetoFrontName(problemId, filename, objectives);
		
		Execution execution = new Execution();
		
		Parameters parameters = executions.get(0).getParameters();
		
		parameters.setPopulationSize(nonDominateds.size());
		
		execution.setParameters(parameters);
		execution.setSolutions(nonDominateds);
		execution.getSettings().setName(name);
		
		List<ExecutionSimplified> oldExecutions = executionService.findByName(pluginId, problemId, name);

		for (ExecutionSimplified e : oldExecutions) {
			executionService.deleteById(e.getId());
		}

		execution = executionService.save(execution);
	}
	
	public String getParetoFrontName(String problemId, String filename, String objectives) {
		return "PF-" + problemId + "-" + filename + "-" + objectives;
	}
	
	
}
