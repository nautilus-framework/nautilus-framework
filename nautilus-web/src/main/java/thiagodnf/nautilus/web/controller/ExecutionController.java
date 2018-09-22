package thiagodnf.nautilus.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/execution")
public class ExecutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/{executionId}")
	public String view(Model model, @PathVariable("executionId") String executionId) {
		
		Execution execution = executionService.findById(executionId);

		Parameters parameters = execution.getParameters();
		
		String problemKey = parameters.getProblemKey();
		
		List<String> objectiveKeys = parameters.getObjectiveKeys();
		
		AbstractPlugin plugin = pluginService.getPlugin(problemKey);
		
		List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, objectiveKeys);

		LOGGER.info("Apply the settings to execution");
		
		List<Solution> solutions = execution.getSolutions();
		
		Settings settings = execution.getSettings();

		if (objectives.size() != 1) {
			solutions = plugin.getNormalize(settings.getNormalize()).normalize(objectives, solutions);
		}

		solutions = plugin.getColorize(settings.getColorize()).execute(plugin, solutions);
		
		// Send the objectives to view
		
		model.addAttribute("objectives", objectives);
		model.addAttribute("plugin", plugin);
		model.addAttribute("solutions", solutions);
		model.addAttribute("execution", execution);
		model.addAttribute("settings", execution.getSettings());
		
		return "execution";
	}
	
	@GetMapping("/{executionId}/delete")
	public String delete(Model model, @PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		executionService.delete(execution);

		return "redirect:/problem/" + execution.getParameters().getProblemKey();
	}
	
	@PostMapping("/{executionId}/save/settings")
	public String settings(Model model, @PathVariable("executionId") String executionId, Settings settings) {

		Execution execution = executionService.findById(executionId);

		execution.setSettings(settings);
		
		executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
