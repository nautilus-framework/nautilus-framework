package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.remover.AbstractRemover;
import thiagodnf.nautilus.core.remover.ObjectivesRemover;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.ParetoFrontApproxExtension;
import thiagodnf.nautilus.web.dto.GenerateParetoFrontApproxDTO;
import thiagodnf.nautilus.web.exception.InstanceNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/pareto-front")
public class ParetoFrontController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
    private Redirect redirect;
	
	@Autowired
    private FileService fileService;
	
	@Autowired
    private SecurityService securityService;
	
	@Autowired
    private ExecutionService executionService;
	
	@GetMapping("/generate/approx")
	public String generateApprox(GenerateParetoFrontApproxDTO generateParetoFrontApproxDTO, Model model) {
		
	    Map<String, ProblemExtension> problems = pluginService.getProblems();

        Map<String, List<Path>> instances = new HashMap<>();

        for (String key : problems.keySet()) {
            instances.put(key, fileService.getInstances(key));
        }

        model.addAttribute("generateParetoFrontApproxDTO", generateParetoFrontApproxDTO);
        model.addAttribute("problems", problems);
        model.addAttribute("instances", instances);
	    
		return "generate-pareto-front-approx";
	}
	
	@GetMapping("/generate/approx/{problemId:.+}/{instanceId:.+}")
    public String generateApprox(@PathVariable String problemId, @PathVariable String instanceId, RedirectAttributes ra) {
	    
	    User user = securityService.getLoggedUser().getUser();
	    
	    ProblemExtension problemExtension = pluginService.getProblemById(problemId);
	    
	    if (!fileService.containsInstance(problemId, instanceId)) {
            throw new InstanceNotFoundException();
        }
	    
	    List<Execution> executions = executionService.findByProblemIdAndInstance(problemId, instanceId);
	    
        if (executions.isEmpty()) {
            return redirect.to("/pareto-front/generate/approx").withError(ra, "msg.executions-not-found");
        }
                
        List<AbstractObjective> objectives = problemExtension.getObjectives();

        List<String> objectiveIds = objectives
                .stream()
                .map(e -> e.getId())
                .collect(Collectors.toList());
        
        Path path = fileService.getInstance(problemId, instanceId);

        Instance instance = problemExtension.getInstance(path);

        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
        
        List<NSolution<?>> allSolutions = new ArrayList<>();

        for (Execution execution : executions) {

            List<NSolution<?>> solutions = execution.getSolutions();

            if (solutions != null) {
                allSolutions.addAll(SolutionListUtils.recalculate(problem, solutions));
            }
        }
        
        List<NSolution<?>> pfApprox =  SolutionListUtils.getNondominatedSolutions(allSolutions);
        
        AbstractRemover remover = new ObjectivesRemover();
        
        pfApprox = remover.execute(pfApprox);
        
        Execution execution = executionService.findParetoFrontApprox(problemId, instanceId);

        if (execution == null) {

            execution = new Execution();

            execution.setUserId(user.getId());
            execution.setAlgorithmId(new ParetoFrontApproxExtension().getId());
            execution.setProblemId(problemId);
            execution.setInstance(instanceId);
            execution.setMaxEvaluations(0);
            execution.setReferencePoints(new ArrayList<>());
            execution.setObjectiveIds(objectiveIds);
            execution.setVisibility(Visibility.PUBLIC);
        }
        
        execution.setCreationDate(new Date());
        execution.setSolutions(pfApprox);
        execution.setPopulationSize(pfApprox.size());
        
        executionService.save(execution);
        
        return "redirect:/";
	}
}
