package thiagodnf.nautilus.web.controller;

import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.colorize.AbstractColorize;
import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.duplicated.AbstractDuplicatesRemover;
import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionUtils;
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
		
		Normalize normalizer = pluginService.getNormalizers().get(settings.getNormalizeId());
		AbstractDuplicatesRemover duplicatesRemover = pluginService.getDuplicatesRemovers().get(settings.getDuplicatesRemoverId());
		AbstractColorize colorizer = pluginService.getColorizers().get(settings.getColorizeId());
		AbstractCorrelation correlation = pluginService.getCorrelationers().get(settings.getCorrelationId());
		
		List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
		
		List<? extends Solution<?>> solutions = execution.getSolutions();
		
		List<? extends Solution<?>> normalizedSolutions = normalizer.normalize(objectives, solutions);
		List<? extends Solution<?>> distinctSolutions = duplicatesRemover.execute(normalizedSolutions);
		List<? extends Solution<?>> colorfulSolutions = colorizer.execute(distinctSolutions);
		
		model.addAttribute("correlations", correlation.execute(objectives, normalizedSolutions));
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", colorfulSolutions);
		model.addAttribute("execution", execution);
		model.addAttribute("normalizers", pluginService.getNormalizers());
		model.addAttribute("duplicatesRemovers", pluginService.getDuplicatesRemovers());
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

		executionService.deleteById(executionId);

		Parameters parameters = execution.getParameters();

		flashMessageService.success(ra, "msg.delete.execution.success", execution.getId());
		
		return "redirect:/plugin/" + parameters.getPluginId() + "/#executions";
	}
	
	@PostMapping("/duplicate")
	public String duplicate(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {

		Execution execution = executionService.findById(executionId);

		execution.setId(null);
		execution.setDate(new Date());
		
		executionService.save(execution);

		flashMessageService.success(ra, "msg.duplicate.execution.success", execution.getId());
		
		return "redirect:/plugin/" + execution.getParameters().getPluginId() + "#executions";
	}
	
	@PostMapping("/save/settings")
	public String saveSettings(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId, 
			Settings settings) {

		Execution execution = executionService.findById(executionId);

		execution.setSettings(settings);
		
		executionService.save(execution);
		
		flashMessageService.success(ra, "msg.settings.save.success");

		return "redirect:/execution/" + executionId;
	}
	
	@PostMapping("/clear/user-feedback")
	public String clearUserFeedback(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {
			
		Execution execution = executionService.findById(executionId);

		for (GenericSolution solution : execution.getSolutions()) {
			SolutionUtils.clearUserFeedback(solution);
		}

		execution = executionService.save(execution);
		
		flashMessageService.success(ra, "msg.cleaned.feedback.all.solutions.success");

		return "redirect:/execution/" + executionId;
	}
}
