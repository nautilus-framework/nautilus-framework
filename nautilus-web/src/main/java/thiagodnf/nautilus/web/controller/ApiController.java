package thiagodnf.nautilus.web.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.ObjectiveValue;
import thiagodnf.nautilus.web.service.PluginService;

@RestController
public class ApiController {
	
    @Autowired
    private PluginService pluginService;
    
    
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
