package thiagodnf.nautilus.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Solution;
import thiagodnf.nautilus.web.model.Variable;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class SolutionController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/solution/{executionId}/{solutionIndex}")
	public String view(Model model, 
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The execution Id was not found");
		}
		
		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solution Index is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		solution.getProperties().put("selected", "1");
		
		execution = executionService.save(execution);
		
		Parameters parameters = execution.getParameters();
		
		AbstractPlugin plugin = pluginService.getPlugin(execution.getParameters().getProblemKey());
		
		String problemKey = parameters.getProblemKey();
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
		
		Map<String, Double> objectivesMap = new HashMap<>();

		for (int i = 0; i < objectives.size(); i++) {
			objectivesMap.put(objectives.get(i).getName(), solution.getObjectives().get(i));
		}
		
		model.addAttribute("objectivesMap", objectivesMap);
		model.addAttribute("solution", solution);
		model.addAttribute("plugin", plugin);
		model.addAttribute("execution", execution);
		
		return "solution";
	}
	
	@PostMapping("/solution/feedback/{executionId}/{solutionIndex}")
	public String updateUserFeedback(ModelMap model,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex, 
			@RequestParam Map<String,String> parameters) {
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solutionIndex is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		double sum = 0.0;
		
		for(String key : parameters.keySet()) {
			
			if(key.startsWith("variable-feedback")) {
				
				double feedback = Double.valueOf(parameters.get(key));
				
				int index = Integer.valueOf(key.split("-")[2]);
				
				sum += feedback;
				
				solution.getVariables().get(index).setUserFeedback(feedback);				
			}
		}
		
		double feedback = (double) sum / (double) solution.getVariables().size();
		
		solution.setUserFeedback(feedback);
		
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
			solution.getProperties().remove("selected");

			for (Variable variable : solution.getVariables()) {
				variable.getProperties().remove("feedback");
			}
		}
		
		execution = executionService.save(execution);
			
		return "redirect:/execution/"+executionId;
	}
}
