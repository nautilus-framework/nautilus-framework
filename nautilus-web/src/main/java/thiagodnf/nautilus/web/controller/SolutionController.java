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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionUtils;
import thiagodnf.nautilus.web.exception.SolutionNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/solution/{executionId:.+}/{solutionIndex:.+}/{objectiveIndex:.+}")
public class SolutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex,
			@PathVariable("objectiveIndex") int objectiveIndex) {
		
		LOGGER.info("Displaying SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);
		
		List<NSolution<?>> solutions = execution.getSolutions();

		if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
			throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
		}
		
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		List<String> objectiveIds = parameters.getObjectiveIds();
		
		NSolution<?> solution = solutions.get(solutionIndex);

		solution.setAttribute(SolutionAttribute.VISUALIZED, true);
		
		execution = executionService.save(execution);

		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, objectiveIds);
		
		Map<String, Double> objectivesMap = new HashMap<>();

		for (int i = 0; i < objectives.size(); i++) {
			objectivesMap.put(objectives.get(i).getName(), solution.getObjective(i));
		}
		
		if (objectiveIndex < 0 || objectiveIndex >= objectives.size()) {
			throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
		}
		
		model.addAttribute("objectivesMap", objectivesMap);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("solution", solution);
		model.addAttribute("variables", SolutionUtils.getVariablesAsList(solution));
		model.addAttribute("execution", execution);
		model.addAttribute("feedbackForObjectiveIndex", objectiveIndex);
		model.addAttribute("feedbackForObjective", objectives.get(objectiveIndex));
		
		return "solution";
	}
	
	@PostMapping("/save/feedback")
	public String saveUserFeedback(ModelMap model,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex, 
			@PathVariable("objectiveIndex") int objectiveIndex,
			@RequestParam Map<String,String> parameters) {
		
		LOGGER.info("Saving feedback for SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);

		List<NSolution<?>> solutions = execution.getSolutions();
		
		if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
			throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
		}
		
		NSolution<?> solution = solutions.get(solutionIndex);
		
		solution.setAttribute(SolutionAttribute.SELECTED, true);

		System.out.println(parameters);
				
		double feedback = Double.valueOf(parameters.get("feedback"));
		
		solution.setAttribute(SolutionAttribute.FEEDBACK_FOR_OBJECTIVE+objectiveIndex, feedback);

		execution = executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
	
	@PostMapping("/clear/user-feedback")
	public String clearUserFeedback(Model model, 
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex) {
		
		Execution execution = executionService.findById(executionId);

		List<NSolution<?>> solutions = execution.getSolutions();
		
		if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
			throw new SolutionNotFoundException();
		}
		
		NSolution<?> solution = solutions.get(solutionIndex);

		SolutionUtils.clearUserFeedback(solution);
		
		execution = executionService.save(execution);
		
		flashMessageService.success(ra, "msg.cleaned.feedback.single.solution.success", String.valueOf(solutionIndex));
		
		return "redirect:/execution/" + executionId;
	}
}
