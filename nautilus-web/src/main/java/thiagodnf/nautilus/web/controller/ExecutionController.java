package thiagodnf.nautilus.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
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
		
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", solutions);
		model.addAttribute("execution", execution);
		
		return "execution";
	}
}
