package thiagodnf.nautilus.web.controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.colorize.Colorize;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Settings;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/execution/{executionId:.+}")
public class ExecutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable("executionId") String executionId) {
		
		LOGGER.info("Displaying '{}'", executionId);
		
		Execution execution = executionService.findById(executionId);
		Parameters parameters = execution.getParameters();
		Settings settings = execution.getSettings();
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		
		List<Solution> solutions = execution.getSolutions();

		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalize());
		Colorize colorize = pluginService.getColorizers().get(settings.getColorize());

		if (objectives.size() != 1) {
			solutions = normalizer.normalize(objectives, solutions);
		}
		
//		if (settings.isRemoveDuplicatedSolutions()) {
//			solutions = SolutionListUtils.removeRepeated(solutions);
//		}

		solutions = colorize.execute(solutions);
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", solutions);
		model.addAttribute("execution", execution);
		model.addAttribute("normalizers", pluginService.getNormalizers());
		model.addAttribute("colorizers", pluginService.getColorizers());
		model.addAttribute("correlationers", pluginService.getCorrelationers());
		model.addAttribute("settings", execution.getSettings());
		
		return "execution";
	}
	
	@PostMapping("/delete")
	public String delete(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		executionService.delete(execution);

		Parameters parameters = execution.getParameters();

		flashMessageService.success(ra, "msg.delete.execution.success", execution.getTitle());
		
		return "redirect:/problem/" + parameters.getPluginId() + "/" + parameters.getProblemId()+"#executions";
	}
	
	@GetMapping("/duplicate")
	public String duplicate(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		execution.setId(null);
		execution.setDate(new Date());
		
		executionService.save(execution);

		Parameters parameters = execution.getParameters();

		flashMessageService.success(ra, "msg.duplicate.execution.success", execution.getTitle());
		
		return "redirect:/problem/" + parameters.getPluginId() + "/" + parameters.getProblemId()+"#executions";
	}
	
	@PostMapping("/save/settings")
	public String saveSettings(Model model, 
			@PathVariable("executionId") String executionId, 
			Settings settings) {

		Execution execution = executionService.findById(executionId);

		execution.setSettings(settings);
		
		executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
	
	@GetMapping("/clear/user-feedback")
	public String clearUserFeedback(Model model, 
			@PathVariable("executionId") String executionId) {
			
		Execution execution = executionService.findById(executionId);

		for (Solution solution : execution.getSolutions()) {

			solution.getProperties().remove("feedback");
			solution.getProperties().remove("selected");

			for (Variable variable : solution.getVariables()) {
				variable.getProperties().remove("feedback");
			}
		}

		execution = executionService.save(execution);

		return "redirect:/execution/" + executionId;
	}
}
