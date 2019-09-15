package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
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
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.CorrelationExtension;
import thiagodnf.nautilus.plugin.extension.NormalizerExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.RemoverExtension;
import thiagodnf.nautilus.web.dto.ExecutionSettingsDTO;
import thiagodnf.nautilus.web.exception.ExecutionNotPublicException;
import thiagodnf.nautilus.web.exception.ExecutionNotReadyException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;
import thiagodnf.nautilus.web.util.Color;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/execution/{executionId:.+}")
public class ExecutionController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutionController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
    private FileService fileService;
	
	@Autowired
    private Redirect redirect;
	
	@GetMapping("")
	public String view(@PathVariable String executionId, Model model) {
		
		LOGGER.debug("Displaying '{}'", executionId);
		
		User user = securityService.getLoggedUser().getUser(); 
		
        Execution execution = executionService.findExecutionById(executionId);

        if (execution.getSolutions() == null)
            throw new ExecutionNotReadyException();
        
        if (!execution.getUserId().equalsIgnoreCase(user.getId()) && execution.getVisibility() == Visibility.PRIVATE) {
            throw new ExecutionNotPublicException();
        }
        
		ProblemExtension problemExtension = pluginService.getProblemById(execution.getProblemId());
		NormalizerExtension normalizerExtension = pluginService.getNormalizerExtensionById(execution.getNormalizeId());
		CorrelationExtension correlationExtension = pluginService.getCorrelationExtensionById(execution.getCorrelationId());
		AlgorithmExtension algorithmExtension = pluginService.getAlgorithmExtensionById(execution.getAlgorithmId());
		RemoverExtension duplicatesRemover = pluginService.getRemoverExtensionById(execution.getRemoverId());
		
		
		List<NSolution<?>> solutions = null;
		List<AbstractObjective> objectives = null;
		
        if (execution.isShowOriginalObjectiveValues()) {

            objectives = problemExtension.getObjectives();

            Path path = fileService.getInstance(execution.getProblemId(), execution.getInstance());

            Instance instance = problemExtension.getInstance(path);

            NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
            
            solutions = new ArrayList<>();

            for (Solution<?> solution : execution.getSolutions()) {

                Solution<?> newSolution = Converter.toSolutionWithOutObjectives(problem, solution);

                if (problem instanceof NBinaryProblem) {
                    ((NBinaryProblem) problem).evaluate((NBinarySolution) newSolution);
                }
                if (problem instanceof NIntegerProblem) {
                    ((NIntegerProblem) problem).evaluate((NIntegerSolution) newSolution);
                }
                
                ((NSolution<?>)newSolution).getAttributes().clear();
                ((NSolution<?>)newSolution).setAttribute(SolutionAttribute.ID, solution.getAttribute(SolutionAttribute.ID));
                ((NSolution<?>)newSolution).setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, solution.getAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES));
                
                solutions.add((NSolution<?>) newSolution);
            }
        }else {
            solutions = execution.getSolutions();
            objectives = problemExtension.getObjectiveByIds(execution.getObjectiveIds());
        }
		
		List<NSolution<?>> normalizedSolutions = normalizerExtension.getNormalizer().normalize(objectives, solutions);
		List<NSolution<?>> distinctSolutions = duplicatesRemover.getRemover(problemExtension).execute(normalizedSolutions);
		
		model.addAttribute("correlations", correlationExtension.getCorrelation().execute(objectives, normalizedSolutions));
		model.addAttribute("problem", problemExtension);
		model.addAttribute("algorithm", algorithmExtension);
		model.addAttribute("objectives", objectives);
		model.addAttribute("solutions", distinctSolutions);
		model.addAttribute("execution", execution);
		model.addAttribute("normalizers", pluginService.getNormalizers());
		model.addAttribute("removers", pluginService.getRemovers());
		model.addAttribute("correlationers", pluginService.getCorrelations());
		model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
		model.addAttribute("executionSettingsDTO", executionService.convertToExecutionSettingsDTO(execution));
		model.addAttribute("isReadOnly", executionService.isReadOnly(execution));
		model.addAttribute("colors", Color.values());
		model.addAttribute("visibilities", Visibility.values());
		
		return "execution";
	}
	
	@PostMapping("/delete")
	public String delete(@PathVariable String executionId, RedirectAttributes ra, Model model) {

	    executionService.deleteById(executionId);
			
		return redirect.to("/home/").withSuccess(ra, Messages.EXECUTION_DELETED_SUCCESS, executionId);
	}
	
	@PostMapping("/duplicate")
	public String duplicate(
	        @PathVariable String executionId, 
	        RedirectAttributes ra, 
	        Model model) {

	    User user = securityService.getLoggedUser().getUser(); 
	    
		executionService.duplicate(user.getId(), executionId);
		
		return redirect.to("/home/").withSuccess(ra, Messages.EXECUTION_DUPLICATED_SUCCESS);
	}
	
	@PostMapping("/settings/save")
	public String saveSettings(
			@PathVariable String executionId,
			@Valid ExecutionSettingsDTO executionSettingsDTO,
			BindingResult bindingResult,
			RedirectAttributes ra, 
			Model model) {
	    	    
        executionService.updateSettings(executionId, executionSettingsDTO);
            
        return redirect.to("/execution/" + executionId+"#settings").withSuccess(ra, Messages.EXECUTION_SETTINGS_SAVED_SUCCESS);
	}
	
	@PostMapping("/clear/user-feedback")
	public String clearUserFeedback(
			@PathVariable String executionId,
			RedirectAttributes ra,
			Model model) {
			
		Execution execution = executionService.findExecutionById(executionId);

		execution.getItemForEvaluations().clear();
		
		execution = executionService.save(execution);
		
		return redirect.to("/execution/" + executionId).withSuccess(ra, Messages.EXECUTION_FEEDBACK_CLEANED_SUCCESS);
	}
}
