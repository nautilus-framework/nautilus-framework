package thiagodnf.nautilus.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.colorize.AbstractColorize;
import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.duplicated.AbstractDuplicatesRemover;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.SolutionUtils;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.ExecutionSettingsDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FlashMessageService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;

@Controller
@RequestMapping("/execution/{executionId:.+}")
public class ExecutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FlashMessageService flashMessageService;
	
	@GetMapping("")
	public String view(@PathVariable String executionId, Model model) {
		
		LOGGER.info("Displaying '{}'", executionId);
		
		Execution execution = executionService.findExecutionById(executionId);
		
		ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
		
		AbstractNormalize normalizer = pluginService.getNormalizers().get(execution.getNormalizeId());
		AbstractDuplicatesRemover duplicatesRemover = pluginService.getDuplicatesRemovers().get(execution.getDuplicatesRemoverId());
		AbstractColorize colorizer = pluginService.getColorizers().get(execution.getColorizeId());
		AbstractCorrelation correlation = pluginService.getCorrelationers().get(execution.getCorrelationId());
		
		List<AbstractObjective> objectives = problemExtension.getObjectives();
		
		List<NSolution<?>> solutions = execution.getSolutions();
		
		List<NSolution<?>> normalizedSolutions = normalizer.normalize(objectives, solutions);
		List<NSolution<?>> distinctSolutions = duplicatesRemover.execute(normalizedSolutions);
		List<NSolution<?>> colorfulSolutions = colorizer.execute(distinctSolutions);
		
		model.addAttribute("correlations", correlation.execute(objectives, normalizedSolutions));
		model.addAttribute("problem", problemExtension);
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", colorfulSolutions);
		model.addAttribute("execution", execution);
		model.addAttribute("normalizers", pluginService.getNormalizers());
		model.addAttribute("duplicatesRemovers", pluginService.getDuplicatesRemovers());
		model.addAttribute("colorizers", pluginService.getColorizers());
		model.addAttribute("correlationers", pluginService.getCorrelationers());
		model.addAttribute("reducers", pluginService.getReducers());
		model.addAttribute("executionSettingsDTO", executionService.convertToExecutionSettingsDTO(execution));
		
		return "execution";
	}
	
	@PostMapping("/delete")
	public String delete(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {

		Execution execution = executionService.findExecutionById(executionId);

		User user = securityService.getLoggedUser().getUser(); 
		
		if(execution.getUserId().equalsIgnoreCase(user.getId())) {
			
			executionService.deleteById(executionId);
			
			flashMessageService.success(ra, Messages.EXECUTION_DELETE_SUCCESS, execution.getId());
		}else {
			flashMessageService.error(ra, Messages.EXECUTION_DELETE_FAIL_NO_OWNER);
		}
		
		return "redirect:/home/";
	}
	
	@PostMapping("/duplicate")
	public String duplicate(
			@PathVariable("executionId") String executionId,
			RedirectAttributes ra,
			Model model) {

		executionService.duplicate(executionId);
		
		flashMessageService.success(ra, Messages.EXECUTION_DUPLICATE_SUCCESS);
		
		return "redirect:/home/";
	}
	
	@PostMapping("/settings/save")
	public String saveSettings(
			@PathVariable("executionId") String executionId,
			@Valid ExecutionSettingsDTO executionSettingsDTO,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes ra) {

		executionService.updateSettings(executionId, executionSettingsDTO);

		flashMessageService.success(ra, Messages.EXECUTION_SAVE_SETTINGS_SUCCESS);

		return "redirect:/execution/" + executionId+"#settings";
	}
	
	@PostMapping("/clear/user-feedback")
	public String clearUserFeedback(Model model,
			RedirectAttributes ra,
			@PathVariable("executionId") String executionId) {
			
		Execution execution = executionService.findExecutionById(executionId);

		for (NSolution<?> solution : execution.getSolutions()) {
			SolutionUtils.clearUserFeedback(solution);
		}

		execution = executionService.save(execution);
		
		flashMessageService.success(ra, "msg.cleaned.feedback.all.solutions.success");

		return "redirect:/execution/" + executionId;
	}
}
