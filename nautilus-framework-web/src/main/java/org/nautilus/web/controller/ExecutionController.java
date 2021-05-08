package org.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.encoding.problem.NIntegerProblem;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.encoding.solution.NIntegerSolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.util.Converter;
import org.nautilus.core.util.SolutionAttribute;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.CorrelationExtension;
import org.nautilus.plugin.extension.NormalizerExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.RemoverExtension;
import org.nautilus.web.dto.ExecutionSettingsDTO;
import org.nautilus.web.dto.UploadFileDTO;
import org.nautilus.web.exception.AbstractRedirectException;
import org.nautilus.web.exception.ExecutionNotPublicException;
import org.nautilus.web.exception.ExecutionNotReadyException;
import org.nautilus.web.model.Execution;
import org.nautilus.web.model.User;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.FlashMessageService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.nautilus.web.util.Color;
import org.nautilus.web.util.Messages;
import org.nautilus.web.util.Redirect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.uma.jmetal.solution.Solution;

@Controller
@RequestMapping("/executions")
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
	
	@Autowired
    private FlashMessageService flashMessageService;
	
	@GetMapping("/{executionId:.+}")
	public String view(@PathVariable String executionId, Model model) {
		
		LOGGER.debug("Displaying '{}'", executionId);
		
		User user = securityService.getLoggedUser().getUser(); 
		
        Execution execution = executionService.findExecutionById(executionId);

        if (execution.getSolutions() == null)
            throw new ExecutionNotReadyException();
        
        if (!execution.getUserId().equalsIgnoreCase(user.getId())) {
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

            Path path = fileService.getInstanceLocation(execution.getProblemId(), execution.getInstance());

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
        
        List<String> selectedIndexes = execution.getSelectedSolutions()
                .stream()
                .map(e -> String.valueOf(e.getSolutionIndex()))
                .collect(Collectors.toList());
		
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
		model.addAttribute("userSettingsDTO", userService.getSettingsDTO());
		model.addAttribute("executionSettingsDTO", executionService.convertToExecutionSettingsDTO(execution));
		model.addAttribute("isReadOnly", executionService.isReadOnly(execution));
		model.addAttribute("colors", Color.values());
		model.addAttribute("selectedIndexes", selectedIndexes);
		
		return "executions/execution";
	}
	
	@ResponseBody
	@GetMapping("/{executionId:.+}/download")    
    public ResponseEntity<Resource> download(@PathVariable("executionId") String executionId) {

        LOGGER.info("Downloading as json file the execution id {}", executionId);

        Execution execution = executionService.findExecutionById(executionId);
        
        String content = Converter.toJson(execution);

        Resource file = new ByteArrayResource(content.getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + executionId + ".json\"")
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
                .body(file);
    }
	
	@PostMapping("/{executionId:.+}/delete")
	public String delete(@PathVariable String executionId, RedirectAttributes ra, Model model) {

	    executionService.deleteById(executionId);
			
		return redirect.to("/home/").withSuccess(ra, Messages.EXECUTION_DELETED_SUCCESS, executionId);
	}
	
	@PostMapping("/{executionId:.+}/settings/save")
	public String saveSettings(Model model, RedirectAttributes ra, 
			@PathVariable String executionId,
			@Valid ExecutionSettingsDTO executionSettingsDTO,
			BindingResult bindingResult) {
	    	    
        executionService.updateSettings(executionId, executionSettingsDTO);
            
        return redirect.to("/executions/" + executionId+"#settings").withSuccess(ra, Messages.EXECUTION_SETTINGS_SAVED_SUCCESS);
	}
	
	@PostMapping("/{executionId:.+}/clear-user-feedback")
	public String clearUserFeedback(Model model, RedirectAttributes ra, @PathVariable String executionId) {
			
		Execution execution = executionService.findExecutionById(executionId);

		execution.getItemForEvaluations().clear();
		
		execution = executionService.save(execution);
		
		return redirect.to("/executions/" + executionId).withSuccess(ra, Messages.EXECUTION_FEEDBACK_CLEANED_SUCCESS);
	}
	
	@PostMapping("/{executionId:.+}/unselect-all")
    public String unselectAll(Model model, RedirectAttributes ra, @PathVariable String executionId) {
            
        Execution execution = executionService.findExecutionById(executionId);

        execution.getSelectedSolutions().clear();
        
        execution = executionService.save(execution);
        
        return redirect.to("/executions/" + executionId).withSuccess(ra, Messages.EXECUTION_FEEDBACK_CLEANED_SUCCESS);
    }
	
	@GetMapping("/upload")
    public String formUploadExecution(Model model, UploadFileDTO uploadFileDTO) {
        
        model.addAttribute("uploadFileDTO", uploadFileDTO);
        
        return "executions/form-upload-execution";
    }
    
    @PostMapping("/upload")
    public String uploadExecution(
            @Valid UploadFileDTO uploadFileDTO, 
            BindingResult result, 
            RedirectAttributes ra,
            Model model) {

        LOGGER.info("Uploading file: {}", uploadFileDTO.getFile().getOriginalFilename());

        if (result.hasErrors()) {
            return formUploadExecution(model, uploadFileDTO);
        }
            
        User user = securityService.getLoggedUser().getUser(); 
        
        try {

            MultipartFile file = uploadFileDTO.getFile();

            String content = null;

            try {
                content = new String(file.getBytes(), "UTF-8");
            } catch (Exception e) {
                throw new RuntimeException(e);
            } 

            Execution execution = Converter.fromJson(content, Execution.class);

            execution.setId(null);
            execution.setUserId(user.getId());
            execution.setCreationDate(null);
            execution.setLastChangeDate(null);
            
            executionService.save(execution);
            
            flashMessageService.success(ra, Messages.EXECUTION_UPLOADED_SUCCESS);
        } catch (AbstractRedirectException ex) {
            flashMessageService.error(ra, ex);
        }
        
        return "redirect:/home/";
    }
}
