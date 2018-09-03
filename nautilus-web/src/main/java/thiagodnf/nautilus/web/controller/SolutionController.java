package thiagodnf.nautilus.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Solution;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class SolutionController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/solution/{executionId}/{solutionIndex}")
	public String view(Model model, @PathVariable("executionId") String executionId, @PathVariable("solutionIndex") int solutionIndex) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solutionIndex is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		solution.getProperties().put("selected", "1");
		
		execution = executionService.save(execution);
		
		Parameters parameters = execution.getParameters();
		
		String problemKey = parameters.getProblemKey();
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
		
		Map<String, Double> objectivesMap = new HashMap<>();

		for (int i = 0; i < objectives.size(); i++) {
			objectivesMap.put(objectives.get(i).getName(), solution.getObjectives().get(i));
		}
		
		model.addAttribute("objectivesMap", objectivesMap);
		model.addAttribute("solution", solution);
		model.addAttribute("executionId", executionId);
		
		return "solution";
	}
	
	@PostMapping("/solution/feedback/{executionId}/{solutionIndex}")
	public String updateUserFeedback(Model model, 
			@RequestParam("user-feedback") String userFeedback,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solutionIndex is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		solution.getProperties().put("feedback", userFeedback);
		
		execution = executionService.save(execution);
		
		return "redirect:/execution/"+executionId;
	}
	
	@GetMapping("/solution/clear/user-selection/{executionId}")
	public String clearUserSelection(Model model, @PathVariable("executionId") String executionId) {
			
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}

		for (Solution solution : execution.getSolutions()) {
			solution.getProperties().remove("selected");
		}
		
		execution = executionService.save(execution);
			
		return "redirect:/execution/"+executionId;
	}
	
	@GetMapping("/solution/clear/user-feedback/{executionId}")
	public String clearUserFeedback(Model model, @PathVariable("executionId") String executionId) {
			
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}

		for (Solution solution : execution.getSolutions()) {
			solution.getProperties().remove("feedback");
		}
		
		execution = executionService.save(execution);
			
		return "redirect:/execution/"+executionId;
	}
}
