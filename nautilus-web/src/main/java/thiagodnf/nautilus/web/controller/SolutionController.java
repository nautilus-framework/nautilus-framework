package thiagodnf.nautilus.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/solution/{executionId:.+}/{solutionIndex:.+}")
public class SolutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex) {
		
		LOGGER.info("Displaying SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);

		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solution Index is invalid");
		}
		
		Solution<?> solution = execution.getSolutions().get(solutionIndex);
		
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		
		Map<String, Double> objectivesMap = new HashMap<>();

		for (int i = 0; i < objectives.size(); i++) {
			objectivesMap.put(objectives.get(i).getName(), solution.getObjective(i));
		}
		
		model.addAttribute("objectivesMap", objectivesMap);
		model.addAttribute("solution", solution);
		model.addAttribute("execution", execution);
		
		return "solution";
	}
	
	@PostMapping("/save/feedback")
	public String updateUserFeedback(ModelMap model,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex, 
			@RequestParam Map<String,String> parameters) {
		
		LOGGER.info("Saving feedback for SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);

		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solutionIndex is invalid");
		}
		
		Solution<?> solution = execution.getSolutions().get(solutionIndex);

		solution.setAttribute("selected", "1");

		for (String key : parameters.keySet()) {

			if (key.startsWith("feedback-for-variable")) {
				solution.setAttribute(key, parameters.get(key));
			}
		}

		execution = executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
