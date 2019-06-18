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

import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.duplicated.AbstractDuplicatesRemover;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.ExecutionSettingsDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.PreferencesService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/execution/{executionId:.+}")
public class ExecutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
    private PreferencesService preferencesService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
    private Redirect redirect;
	
	@GetMapping("")
	public String view(@PathVariable String executionId, Model model) {
		
		LOGGER.info("Displaying '{}'", executionId);
		
		Execution execution = executionService.findExecutionById(executionId);
		
		User user = securityService.getLoggedUser().getUser(); 
		
		ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
		
		AbstractNormalize normalizer = pluginService.getNormalizers().get(execution.getNormalizeId());
		AbstractDuplicatesRemover duplicatesRemover = pluginService.getDuplicatesRemovers().get(execution.getDuplicatesRemoverId());
		AbstractCorrelation correlation = pluginService.getCorrelationers().get(execution.getCorrelationId());
		
		List<AbstractObjective> objectives = problemExtension.getObjectives();
		
		List<NSolution<?>> solutions = execution.getSolutions();
		
		List<NSolution<?>> normalizedSolutions = normalizer.normalize(objectives, solutions);
		List<NSolution<?>> distinctSolutions = duplicatesRemover.execute(normalizedSolutions);
		
		model.addAttribute("correlations", correlation.execute(objectives, normalizedSolutions));
		model.addAttribute("problem", problemExtension);
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", distinctSolutions);
		model.addAttribute("execution", execution);
		model.addAttribute("normalizers", pluginService.getNormalizers());
		model.addAttribute("duplicatesRemovers", pluginService.getDuplicatesRemovers());
		model.addAttribute("colorizers", pluginService.getColorizers());
		model.addAttribute("correlationers", pluginService.getCorrelationers());
		model.addAttribute("reducers", pluginService.getReducers());
		model.addAttribute("settingsPreferencesDTO", preferencesService.findById(user.getId()));
		model.addAttribute("executionSettingsDTO", executionService.convertToExecutionSettingsDTO(execution));
		
		return "execution";
	}
	
	@PostMapping("/delete")
	public String delete(@PathVariable String executionId, RedirectAttributes ra, Model model) {

		Execution execution = executionService.findExecutionById(executionId);

		User user = securityService.getLoggedUser().getUser(); 
		
		if(execution.getUserId().equalsIgnoreCase(user.getId())) {
			
			executionService.deleteById(executionId);
			
			return redirect.to("/home/").withSuccess(ra, Messages.EXECUTION_DELETED_SUCCESS, execution.getId());
		}
		
		return redirect.to("/home/").withError(ra, Messages.EXECUTION_DELETED_FAIL_NO_OWNER);
	}
	
	@PostMapping("/duplicate")
	public String duplicate(@PathVariable String executionId, RedirectAttributes ra, Model model) {

	    User user = securityService.getLoggedUser().getUser(); 
	    
		executionService.duplicate(user.getId(), executionId);
		
		return redirect.to("/home/").withSuccess(ra, Messages.EXECUTION_DUPLICATED_SUCCESS);
	}
	
	@PostMapping("/settings/save")
	public String saveSettings(
			@PathVariable String executionId,
			@Valid ExecutionSettingsDTO executionSettingsDTO,
			BindingResult bindingResult,
			RedirectAttributes ra, Model model) {
	    
	    Execution execution = executionService.findExecutionById(executionId);
	    
	    User user = securityService.getLoggedUser().getUser(); 
        
        if(execution.getUserId().equalsIgnoreCase(user.getId())) {
            
            executionService.updateSettings(executionId, executionSettingsDTO);
            
            return redirect.to("/execution/" + executionId+"#settings").withSuccess(ra, Messages.EXECUTION_SETTINGS_SAVED_SUCCESS);
        }

        return redirect.to("/execution/" + executionId+"#settings").withError(ra, Messages.EXECUTION_DELETED_FAIL_NO_OWNER);
	}
	
//	@PostMapping("/clear/user-feedback")
//	public String clearUserFeedback(Model model,
//			RedirectAttributes ra,
//			@PathVariable("executionId") String executionId) {
//			
//		Execution execution = executionService.findExecutionById(executionId);
//
//		for (NSolution<?> solution : execution.getSolutions()) {
//			SolutionUtils.clearUserFeedback(solution);
//		}
//
//		execution = executionService.save(execution);
//		
//		flashMessageService.success(ra, "msg.cleaned.feedback.all.solutions.success");
//
//		return "redirect:/execution/" + executionId;
//	}
}
