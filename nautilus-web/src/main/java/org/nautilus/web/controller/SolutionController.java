package org.nautilus.web.controller;

import java.nio.file.Path;
import java.util.List;

import javax.validation.Valid;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.model.SelectedSolution;
import org.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.reduction.AbstractReduction.ItemForEvaluation;
import org.nautilus.core.util.SolutionUtils;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.web.exception.SolutionNotFoundException;
import org.nautilus.web.persistence.dto.UserFeedbackDTO;
import org.nautilus.web.persistence.model.Execution;
import org.nautilus.web.persistence.model.User;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
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

@Controller
@RequestMapping("/solution/{executionId:.+}/{solutionIndex:.+}/{objectiveIndex:.+}")
public class SolutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SolutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
    private FileService fileService;
	
	@Autowired
	private Redirect redirect;
	
	@Autowired
    private UserService userService;
	
	@Autowired
    private SecurityService securityService;
	
	@GetMapping("")
	public String view(Model model, 
			@PathVariable String executionId, 
			@PathVariable int solutionIndex,
			@PathVariable int objectiveIndex) {
		
		LOGGER.info("Displaying SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		Execution execution = executionService.findExecutionById(executionId);
		
		List<NSolution<?>> solutions = execution.getSolutions();

		ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
		
		if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
			throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
		}
		
		List<AbstractObjective> objectives = problemExtension.getObjectiveByIds(execution.getObjectiveIds());
		
		List<NSolution<?>> normalizedSolutions = new ByParetoFrontValuesNormalize().normalize(objectives, execution.getSolutions());
		
		NSolution<?> solution = execution.getSolutions().get(solutionIndex);
		NSolution<?> normalizedSolution = normalizedSolutions.get(solutionIndex);
		
		User user = securityService.getLoggedUser().getUser(); 
		
		Path path = fileService.getInstance(execution.getProblemId(), execution.getInstance());
		
		Instance instance = problemExtension.getInstance(path);
		
		ItemForEvaluation found = findBySolutionIndexAndObjectiveIndex(execution.getItemForEvaluations(), solutionIndex, objectiveIndex);
        
		if(found == null) {
		    model.addAttribute("userFeedbackDTO", new UserFeedbackDTO(objectiveIndex));
		}else {
		    model.addAttribute("userFeedbackDTO", new UserFeedbackDTO(found.getObjectiveIndex(), found.getFeedback()));
		}
		
		model.addAttribute("solution", solution);
		model.addAttribute("normalizedSolution", normalizedSolution);
		model.addAttribute("objectives", objectives);
		model.addAttribute("execution", execution);
		model.addAttribute("variables", problemExtension.getVariablesAsList(instance, solution));
		model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
		model.addAttribute("feedbackForObjectiveIndex", objectiveIndex);
		model.addAttribute("feedbackForObjective", objectives.get(objectiveIndex));
		model.addAttribute("isReadOnly", executionService.isReadOnly(execution));
		model.addAttribute("isSelected", SolutionUtils.isSelected(execution.getSelectedSolutions(), solutionIndex));
		model.addAttribute("isCORNSGAII", execution.getAlgorithmId().equalsIgnoreCase("cor-nsga-ii"));
		
		return "solution";
	}
	
	@PostMapping("/save/feedback")
	public String saveUserFeedback(
			@PathVariable String executionId, 
			@PathVariable int solutionIndex, 
			@Valid UserFeedbackDTO userFeedbackDTO,
			BindingResult bindingResult,
			RedirectAttributes ra,
			Model model) {
		
		LOGGER.info("Saving feedback for SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
		
		if (bindingResult.hasErrors()) {
		    return "solution";
		}
		
		Execution execution = executionService.findExecutionById(executionId);

		ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
		
		List<AbstractObjective> objectives = problemExtension.getObjectiveByIds(execution.getObjectiveIds());
        
		List<NSolution<?>> normalizedSolutions = new ByParetoFrontValuesNormalize().normalize(objectives, execution.getSolutions());
        
        if (solutionIndex < 0 || solutionIndex >= normalizedSolutions.size()) {
            throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
        }
        
        NSolution<?> normalizedSolution = normalizedSolutions.get(solutionIndex);
		
        int objectiveIndex =  userFeedbackDTO.getObjectiveIndex();
        
        //
        
        ItemForEvaluation found = findBySolutionIndexAndObjectiveIndex(execution.getItemForEvaluations(), solutionIndex, objectiveIndex);
        
        if (found != null) {
            execution.getItemForEvaluations().remove(found);
        }
		
		ItemForEvaluation item = new ItemForEvaluation();
		
		item.setSolutionIndex(solutionIndex);
		item.setObjectiveIndex(objectiveIndex);
		item.setObjectiveValue(normalizedSolution.getObjective(objectiveIndex));
		item.setFeedback(userFeedbackDTO.getFeedback());
		
		execution.getItemForEvaluations().add(item);
		
		execution = executionService.save(execution);

		return redirect.to("/execution/" + executionId).withSuccess(ra, Messages.EXECUTION_FEEDBACK_SAVED_SUCCESS);
	}
	
	@GetMapping("/select/{action:.+}")
    public String select(
            @PathVariable String executionId, 
            @PathVariable int solutionIndex,
            @PathVariable int action,
            RedirectAttributes ra,
            Model model) {
        
        LOGGER.info("Selecting solution SolutionIndex {} in ExecutionId {}", solutionIndex, executionId);
        
        Execution execution = executionService.findExecutionById(executionId);

        List<NSolution<?>> solutions = execution.getSolutions();
        
        if (solutionIndex < 0 || solutionIndex >= solutions.size()) {
            throw new SolutionNotFoundException().redirectTo("/execution/" + executionId);
        }
        
        if (action == 0) {
            execution.getSelectedSolutions().removeIf(e -> e.getSolutionIndex() == solutionIndex);
        } else {
            execution.getSelectedSolutions().add(new SelectedSolution(solutionIndex));
        }
        
        execution = executionService.save(execution);
        
        return redirect.to("/execution/" + executionId).withSuccess(ra, Messages.EXECUTION_FEEDBACK_SAVED_SUCCESS);
	}
	
	private ItemForEvaluation findBySolutionIndexAndObjectiveIndex(List<ItemForEvaluation> items, int solutionIndex, int objectiveIndex) {
	    
	    for(ItemForEvaluation item : items) {
	        
	        if(item.getSolutionIndex() == solutionIndex && item.getObjectiveIndex() == objectiveIndex) {
	            return item;
	        }
	    }
	    
	    return null;
	}
}
