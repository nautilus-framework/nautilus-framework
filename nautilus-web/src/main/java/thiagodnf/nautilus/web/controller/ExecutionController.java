package thiagodnf.nautilus.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.web.colorize.ByJaccardDistanceColorize;
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

		Parameters parameters = execution.getParameters();
		
		String problemKey = parameters.getProblemKey();
		
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		AbstractPlugin plugin = pluginService.getPlugin(problemKey);
		
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, objectiveKeys);

		List<Solution> solutions = execution.getSolutions();

		if (objectives.size() != 1) {
			solutions = Solutioner.normalize(solutions);
		}
		
		if (execution.getSettings().getColorize() == 1) {
			solutions = new ByEuclideanDistanceColorize(plugin).execute(solutions);
		} else if (execution.getSettings().getColorize() == 2) {
			solutions = new ByJaccardDistanceColorize(plugin).execute(solutions);
		}

		model.addAttribute("objectives", objectives);
		model.addAttribute("plugin", plugin);
		model.addAttribute("solutions", solutions);
		model.addAttribute("execution", execution);
		model.addAttribute("settings", execution.getSettings());
		
		return "execution";
	}
	
	@GetMapping("/execution/{executionId}/delete")
	public String delete(Model model, @PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		executionService.delete(execution);

		return "redirect:/problem/" + execution.getParameters().getProblemKey();
	}
	
	@PostMapping("/execution/{executionId}/save/settings")
	public String settings(Model model, @PathVariable("executionId") String executionId, Settings settings) {

		Execution execution = executionService.findById(executionId);

		execution.setSettings(settings);
		
		executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
