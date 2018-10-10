package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
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
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.VariableExtension;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/solution/{executionId:.+}/{solutionIndex:.+}")
public class SolutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex) {
		
		LOGGER.info("Displaying SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);

		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solution Index is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		execution = executionService.save(execution);
		
		Parameters parameters = execution.getParameters();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
		InstanceData data = pluginService.getProblemExtension(pluginId, problemId).readInstanceData(instance);
		
		Problem problem = pluginService.getProblemExtension(pluginId, problemId).createProblem(data, objectives);
		
		VariableExtension extension = pluginService.getVariableExtension(pluginId);
		
		
		
		Map<String, Double> objectivesMap = new HashMap<>();

		for (int i = 0; i < objectives.size(); i++) {
			objectivesMap.put(objectives.get(i).getName(), solution.getObjectives().get(i));
		}
		
		model.addAttribute("objectivesMap", objectivesMap);
		model.addAttribute("solution", solution);
		model.addAttribute("execution", execution);
		
		model.addAttribute("extension", extension);
		model.addAttribute("problem", problem);
		
		return "solution";
	}
	
	@PostMapping("/save/feedback")
	public String updateUserFeedback(ModelMap model,
			@PathVariable("executionId") String executionId, 
			@PathVariable("solutionIndex") int solutionIndex, 
			@RequestParam Map<String,String> parameters) {
		
		LOGGER.info("Saving feedback for SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findById(executionId);

		if (execution == null) {
			throw new RuntimeException("The executionId was not found");
		}
		
		if (solutionIndex < 0 || solutionIndex >= execution.getSolutions().size()) {
			throw new RuntimeException("The solutionIndex is invalid");
		}
		
		Solution solution = execution.getSolutions().get(solutionIndex);
		
		solution.getProperties().put("selected", "1");
		
		double sum = 0.0;
		
		for (String key : parameters.keySet()) {

			if (key.startsWith("variable-feedback")) {

				double feedback = Double.valueOf(parameters.get(key));

				int index = Integer.valueOf(key.split("-")[2]);

				sum += feedback;

				solution.getVariables().get(index).setUserFeedback(feedback);
			}
		}
		
		double feedback = (double) sum / (double) solution.getVariables().size();
		
		solution.setUserFeedback(feedback);
		
		execution = executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
