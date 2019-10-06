package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.exception.ProblemNotFoundException;
import thiagodnf.nautilus.web.exception.UserNotFoundException;
import thiagodnf.nautilus.web.model.ObjectiveValue;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;

@RestController
public class ApiController {
	
    @Autowired
    private PluginService pluginService;
    
    @Autowired
    private FileService fileService;
    
    @Autowired
    private SecurityService securityService;
    
    @Autowired
    private ExecutionService executionService;
    
    @GetMapping("/api/problem/{problemId:.+}/instances")
    public Object getInstancesGivenAProblem(@PathVariable String problemId) {

        if (Strings.isNullOrEmpty(problemId)) {
            throw new ProblemNotFoundException();
        }

        pluginService.getProblemById(problemId);

        List<Path> paths = fileService.getInstances(problemId);

        List<String> filenames = paths.stream().map(e -> e.toFile().getName()).collect(Collectors.toList());

        return filenames;
    }
    
    @GetMapping("/api/execution/{problemId:.+}/{instance:.+}/{userId:.+}")
    public Object getExecutions(
            @PathVariable String problemId, 
            @PathVariable String instance,
            @PathVariable String userId,
            @RequestParam(value="selectedSolutions", defaultValue="0") String selectedSolutions) {

        if (Strings.isNullOrEmpty(problemId)) {
            throw new ProblemNotFoundException();
        }
        
        if (Strings.isNullOrEmpty(userId)) {
            throw new UserNotFoundException();
        }
        
        List<ExecutionSimplifiedDTO> executions = executionService.findExecutionSimplifiedDTOByUserId(userId);
        
        executions = executions.stream()
                .filter(e -> e.getProblemId().equalsIgnoreCase(problemId))
                .filter(e -> e.getInstance().equalsIgnoreCase(instance))
                .collect(Collectors.toList());
        
        if (selectedSolutions.equalsIgnoreCase("1")) {
            
            executions = executions.stream()
                    .filter(e -> !e.getSelectedSolutions().isEmpty())
                    .collect(Collectors.toList());
        }
        
        return executions;
    }
    
	@GetMapping("/api/calculate/objective/values")
    public ObjectiveValue calculateObjectiveValues(@RequestParam(value="obj", defaultValue="World") String obAsString) {
	    
	    ObjectiveValue ob = Converter.fromJson(obAsString, ObjectiveValue.class);
	    
	    ProblemExtension problemExtension = pluginService.getProblemById(ob.getProblemId());
	    
	    NSolution<?> sol = (NSolution<?>) pluginService.getSolution(ob.getProblemId(), ob.getInstance(), ob.getVariables());
        
	    List<AbstractObjective> objectives = problemExtension.getObjectives();
        
	    List<NSolution<?>> normalizedSolutions = new ByMaxAndMinValuesNormalize().normalize(objectives, Arrays.asList(sol));
        
        NSolution<?> normalizedSolution = normalizedSolutions.get(0);
        
        ob.setObjectiveIds(objectives.stream().map(e -> e.getId()).collect(Collectors.toList()));
        ob.setObjectiveValues(Converter.toDoubleList(sol.getObjectives()));
        ob.setNormalizedObjectiveValues(Converter.toDoubleList(normalizedSolution.getObjectives()));
        
        return ob;
    }
}
