package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.crossover.SinglePointCrossoverExtension;
import thiagodnf.nautilus.plugin.extension.mutation.BitFlipMutationExtension;
import thiagodnf.nautilus.plugin.extension.selection.BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension;
import thiagodnf.nautilus.web.dto.ContinueDTO;
import thiagodnf.nautilus.web.dto.ParametersDTO;
import thiagodnf.nautilus.web.exception.InstanceNotFoundException;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Execution.Visibility;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.OptimizeService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/optimize")
public class OptimizeController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizeController.class);
    
    @Autowired
    private FileService fileService;
    
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
    private ExecutionService executionService;
	
	@Autowired
    private OptimizeService optimizeService;
	
	@Autowired
    private List<Execution> pendingExecutions;
	
	@Autowired
	private Redirect redirect;
	
	@GetMapping("/{problemId:.+}/{instance:.+}")
	public String form( 
			@PathVariable String problemId, 
			@PathVariable String instance,
			ParametersDTO parametersDTO,
			Model model){
		
        ProblemExtension problem = pluginService.getProblemById(problemId);

        if (!fileService.containsInstance(problemId, instance)) {
            throw new InstanceNotFoundException();
        }
		
        User user = securityService.getLoggedUser().getUser();
        
        parametersDTO.setReferencePoints(SolutionListUtils.getDefaultReferencePoints(problem.getObjectives().size()));
		
		model.addAttribute("userId", user.getId());
		model.addAttribute("problem", problem);
		model.addAttribute("instance", instance);
		model.addAttribute("algorithms", pluginService.getAlgorithms());
		model.addAttribute("crossovers", pluginService.getCrossovers());
		model.addAttribute("mutations", pluginService.getMutations());
		model.addAttribute("selections", pluginService.getSelections());
		model.addAttribute("parametersDTO", parametersDTO);
		
		return "optimize";
	}
	
    @PostMapping("/save")
    public String optimize(@Valid ParametersDTO parametersDTO, BindingResult bindingResult, RedirectAttributes ra,  Model model) {

        System.out.println(Converter.toJson(parametersDTO));
        
        if (bindingResult.hasErrors()) {
            return form(parametersDTO.getProblemId(), parametersDTO.getInstance(), parametersDTO, model);
        }
        
        if (parametersDTO.getAlgorithmId().equalsIgnoreCase("manually")) {
            throw new RuntimeException("You must not select this algorithm");
        }
        
        LOGGER.debug("Saving {}", Converter.toJson(parametersDTO));
        
        User user = securityService.getLoggedUser().getUser();
        
        Execution execution = new Execution();

        execution.setUserId(user.getId());
        execution.setSolutions(null);
        execution.setAlgorithmId(parametersDTO.getAlgorithmId());
        execution.setProblemId(parametersDTO.getProblemId());
        execution.setInstance(parametersDTO.getInstance());
        execution.setPopulationSize(parametersDTO.getPopulationSize());
        execution.setMaxEvaluations(parametersDTO.getMaxEvaluations());
        execution.setSelectionId(parametersDTO.getSelectionId());
        execution.setCrossoverId(parametersDTO.getCrossoverId());
        execution.setCrossoverProbability(parametersDTO.getCrossoverProbability());
        execution.setCrossoverDistribution(parametersDTO.getCrossoverDistribution());
        execution.setMutationId(parametersDTO.getMutationId());
        execution.setMutationProbability(parametersDTO.getMutationProbability());
        execution.setMutationDistribution(parametersDTO.getMutationDistribution());
        execution.setReferencePoints(parametersDTO.getReferencePoints());
        execution.setEpsilon(parametersDTO.getEpsilon());
        execution.setObjectiveIds(parametersDTO.getObjectiveIds());
        execution.setVisibility(Visibility.PRIVATE);
        
        execution = executionService.save(execution);
        
        pendingExecutions.add(execution);
        
        return redirect.to("/home").withSuccess(ra, Messages.EXECUTION_SCHEDULED_SUCCESS);
    }
    
    @PostMapping("/continue")
    public String optimizeAgain(@Valid ContinueDTO continueDTO, BindingResult bindingResult, RedirectAttributes ra,  Model model) {

        if (bindingResult.hasErrors()) {
            return "continue";
        }
        
        Execution execution = executionService.findExecutionById(continueDTO.getPreviousExecutionId());
        
        execution.setLastExecutionId(execution.getId());
        execution.setId(null);
        execution.setSolutions(null);
        execution.setObjectiveIds(continueDTO.getNextObjectiveIds());
        execution.setVisibility(Visibility.PRIVATE);
        execution.setItemForEvaluations(new ArrayList<>());
        
        execution = executionService.save(execution);
        
        pendingExecutions.add(execution);
        
        return redirect.to("/home").withSuccess(ra, Messages.EXECUTION_SCHEDULED_SUCCESS);
    }
    
    @RequestMapping(value = "/execution/cancel/{executionId:.+}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String cancel(@PathVariable String executionId) {
        
        optimizeService.cancel(executionId);
        
        return Converter.toJson("");
    }
    
    @GetMapping("/{problemId:.+}/{instance:.+}/manually")
    public String manually( 
            @PathVariable String problemId, 
            @PathVariable String instance,
            Model model){
        
        ProblemExtension problemExtension = pluginService.getProblemById(problemId);

        if (!fileService.containsInstance(problemId, instance)) {
            throw new InstanceNotFoundException();
        }
        
        Path path = fileService.getInstance(problemId, instance);
        
        Instance inst = problemExtension.getInstance(path);
        
        List<AbstractObjective> objectives = problemExtension.getObjectives();
        
        NProblem<?> p = (NProblem<?>) problemExtension.getProblem(inst, objectives);
        Solution<?> solution = (Solution<?>) p.createSolution();
        
        for (int i = 0; i < solution.getNumberOfVariables(); i++) {

            if(solution instanceof NBinarySolution) {
                
                NBinarySolution sol = (NBinarySolution) solution;

                BinarySet set = new BinarySet(sol.getTotalNumberOfBits());
                
                for (int j = 0; j < sol.getTotalNumberOfBits(); j++) {
                    set.set(j, true);
                }
                
                ((NBinarySolution) solution).setVariableValue(i, set);
            }
        }
        
        model.addAttribute("execution", new Execution());
        model.addAttribute("objectives", objectives);
        model.addAttribute("solution", solution);
        model.addAttribute("variables", problemExtension.getVariablesAsList(inst, solution));
        
        return "manually";
    }
    
    @PostMapping("/{problemId:.+}/{instance:.+}/manually/save")
    public String manuallySave( 
            @PathVariable String problemId, 
            @PathVariable String instance,
            @RequestParam List<String> selectedVariables,
            RedirectAttributes ra,
            Model model){
        
        if (selectedVariables.isEmpty()) {
            throw new RuntimeException("You must select at least one variable");
        }

        ProblemExtension problemExtension = pluginService.getProblemById(problemId);

        if (!fileService.containsInstance(problemId, instance)) {
            throw new InstanceNotFoundException();
        }

        User user = securityService.getLoggedUser().getUser();
        
        Path path = fileService.getInstance(problemId, instance);

        Instance inst = problemExtension.getInstance(path);

        List<AbstractObjective> objectives = problemExtension.getObjectives();

        NProblem<?> p = (NProblem<?>) problemExtension.getProblem(inst, objectives);

        
        Execution execution = new Execution();
        
        execution.setUserId(user.getId());
        execution.setAlgorithmId("manually");
        execution.setProblemId(problemExtension.getId());
        execution.setInstance(instance);
        execution.setObjectiveIds(objectives.stream().map(e -> e.getId()).collect(Collectors.toList()));
        execution.setCrossoverDistribution(20.0);
        execution.setCrossoverProbability(0.9);
        execution.setMutationDistribution(20.0);
        execution.setMutationProbability(0.9);
        execution.setEpsilon(0.001);
        execution.setSelectionId(new BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension().getId());
        
        if (p instanceof NBinaryProblem) {
            
            execution.setCrossoverId(new SinglePointCrossoverExtension().getId());
            execution.setMutationId(new BitFlipMutationExtension().getId());
            
            NBinaryProblem pro = (NBinaryProblem) p;
            
            NBinarySolution sol = (NBinarySolution) p.createSolution();

            BinarySet set = new BinarySet(sol.getTotalNumberOfBits());

            set.clear();

            for (String variableIndex : selectedVariables) {

                int index = Integer.valueOf(variableIndex);

                set.set(index, true);
            }

            sol.setVariableValue(0, set);
            
            pro.evaluate(sol);
            
            sol.getAttributes().clear();
            sol.setAttribute(SolutionAttribute.ID, String.valueOf(0));
            sol.setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, Converter.toJson(execution.getObjectiveIds()));
            
            execution.setSolutions(Arrays.asList(sol));
        }
        
        executionService.save(execution);
        
        return redirect.to("/home").withSuccess(ra, Messages.EXECUTION_UPLOADED_SUCCESS);
    }
}
