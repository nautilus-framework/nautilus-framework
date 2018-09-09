package thiagodnf.nautilus.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.model.Solution;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.util.Solutioner;

@Controller
public class ExecutionController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/execution/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		Parameters parameters = execution.getParameters();
		
		String problemKey = parameters.getProblemKey();
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, objectiveKeys);

		List<Solution> solutions = Solutioner.normalize(execution.getSolutions());
		
		// Calculate the distance
		
		List<Solution> selectedSolutions = getSelectedSolutions(solutions);
		
		for (Solution solution : solutions) {
			solution.getProperties().put("distance", getDistance(solution, selectedSolutions));
		}
		
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", solutions);
		model.addAttribute("execution", execution);
		model.addAttribute("settings", execution.getSettings());
		
		return "execution";
	}
	
	public List<Solution> getSelectedSolutions(List<Solution> solutions) {

		List<Solution> selectedSolutions = new ArrayList<>();

		for (Solution sol : solutions) {

			if (sol.isSelected()) {

				selectedSolutions.add(sol);
			}
		}

		return selectedSolutions;
	}
	
	public String getDistance(Solution s, List<Solution> selectedSolutions) {
		
		if (selectedSolutions.isEmpty()) {
			return "0.0";
		}

		double minDistance = Double.MAX_VALUE;

		Solution closeSolution = null;

		for (Solution selected : selectedSolutions) {

			double distance = EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());

			if (distance < minDistance) {
				minDistance = distance;
				closeSolution = selected;
			}
		}
		
		minDistance = Normalizer.normalize(minDistance, 0, Math.sqrt(2));

		double feedback = closeSolution.getUserFeeback();
		
		double distance = 0.0;
		
		if(feedback == 0) {
			distance = 0.0;
		}else if (feedback > 0) {
			distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
		}else {
			distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
		}
		
		return String.valueOf(distance);
	}
	
	@GetMapping("/execution/delete/{executionId}")
	public String delete(Model model, @PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}

		executionService.delete(executionId);

		return "redirect:/";
	}
	
	@PostMapping("/execution/{executionId}/settings")
	public String settings(Model model, @PathVariable("executionId") String executionId, Settings settings) {

		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		execution.setSettings(settings);
		
		executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
