package org.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.indicator.HypervolumeApprox;
import org.nautilus.core.model.Instance;
import org.nautilus.core.model.SelectedSolution;
import org.nautilus.core.normalize.AbstractNormalize;
import org.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.util.Converter;
import org.nautilus.core.util.SolutionListUtils;
import org.nautilus.plugin.extension.NormalizerExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.algorithm.ManuallyExtension;
import org.nautilus.plugin.extension.normalizer.ByMaxAndMinValuesNormalizerExtension;
import org.nautilus.plugin.toy.extension.problem.ToyProblemExtension;
import org.nautilus.web.dto.CompareDTO;
import org.nautilus.web.dto.ExecutionSimplifiedDTO;
import org.nautilus.web.dto.FormCompareDTO;
import org.nautilus.web.dto.UserDTO;
import org.nautilus.web.model.Execution;
import org.nautilus.web.model.User;
import org.nautilus.web.service.ExecutionService;
import org.nautilus.web.service.FileService;
import org.nautilus.web.service.PluginService;
import org.nautilus.web.service.SecurityService;
import org.nautilus.web.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.qualityindicator.impl.InvertedGenerationalDistance;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.point.PointSolution;

import ufpr.gres.rnsgaii.util.PointSolutionUtils;

@Controller
@RequestMapping("/compare")
public class CompareController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CompareController.class);

    @Autowired
    private ExecutionService executionService;

    @Autowired
    private UserService userService;

    @Autowired
    private PluginService pluginService;
    
    @Autowired
    private FileService fileService;

    @Autowired
    private SecurityService securityService;

    @GetMapping("/form")
    public String form(CompareDTO compareDTO,  FormCompareDTO formCompareDTO, Model model) {

        User user = securityService.getLoggedUser().getUser();

        List<UserDTO> users = userService.findAll();
        
        List<ExecutionSimplifiedDTO> executions = null;
        
        if (securityService.isAdmin()) {
            executions = findAllExecutions(users, true);
        } else {
            executions = executionService.findExecutionSimplifiedDTOByUserId(user.getId());
        }
        
        Map<String, ProblemExtension> problems = pluginService.getProblems();

        List<Path> instances = new ArrayList<>();

        for (String problemId : problems.keySet()) {
            instances.addAll(fileService.getInstances(problemId));
        }
        
        executions = executions.stream()
            .filter(e -> e.getProblemId().equalsIgnoreCase(formCompareDTO.getProblemId()))
            .filter(e -> e.getInstance().equalsIgnoreCase(formCompareDTO.getInstanceId()))
            .collect(Collectors.toList());
        
        String problemId = new ToyProblemExtension().getId();
        
        ProblemExtension problem = pluginService.getProblemById(problemId);
        
        Map<String, Integer> numberOfReductions = new HashMap<>();

//        for (ExecutionSimplifiedDTO executionSimplified : executions) {
//            
//            int numberOfReduction = 0;
//            
//            if(executionSimplified.getAlgorithmId().equalsIgnoreCase(new NSGAIIWithConfidenceBasedReductionAlgorithmExtension().getId())) {
//                
//                Execution execution = executionService.findExecutionById(executionSimplified.getId());
//                
//                numberOfReduction = getNumberOfReductions(execution);
//            }
//            
//            numberOfReductions.put(executionSimplified.getId(), numberOfReduction);
//        }
        
        
        model.addAttribute("instances", instances);
        model.addAttribute("users", users);
        model.addAttribute("objectives", problem.getObjectives());
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        model.addAttribute("executions", executions);
        model.addAttribute("problems", problems);
        model.addAttribute("compareDTO", compareDTO);
        model.addAttribute("formCompareDTO", formCompareDTO);
        model.addAttribute("numberOfReductions", numberOfReductions);
        
        return "form-compare-2";
    }
    
    private List<ExecutionSimplifiedDTO> findAllExecutions(List<UserDTO> users, boolean filterSelectedSolutions){
        
        List<ExecutionSimplifiedDTO> executions = new ArrayList<>();
        
        for (UserDTO userDTO : users) {

            List<ExecutionSimplifiedDTO> found = executionService.findExecutionSimplifiedDTOByUserId(userDTO.getId());

            if (filterSelectedSolutions) {
                found = found.stream().filter(e -> !e.getSelectedSolutions().isEmpty()).collect(Collectors.toList());
            }

            executions.addAll(found);
        }
        
        return executions;
    }

//    @PostMapping("/result")
//    public String show(@Valid CompareDTO dto, @Valid FormCompareDTO formCompareDTO, BindingResult bindingResult, Model model) {
//        
//        if (bindingResult.hasErrors()) {
//            return form(dto, formCompareDTO, model);
//        }
//        
//        User user = securityService.getLoggedUser().getUser();
//        
//        String problemId = new ToyProblemExtension().getId();
//        
//        String instanceId = "james.txt";
//        
//        ProblemExtension problemExtension = pluginService.getProblemById(problemId);
//        
//        List<AbstractObjective> objectives = problemExtension.getObjectives();
//        
//        Path path = fileService.getInstance(problemId, instanceId);
//
//        Instance instance = problemExtension.getInstance(path);
//
//        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
//        
//        Execution pfApproxExecution = executionService.findParetoFrontApprox(problemId, instanceId);
//        
//        if(pfApproxExecution == null) {
//            throw new RuntimeException("The pareto-front approx for "+problemId+" and "+instanceId+ "was not found");
//        }
//        
//        List<NSolution<?>> pfApprox = pfApproxExecution.getSolutions();
//        
//        
//        List<Double> rpValues = new ArrayList<>();
//        
//        for (int i = 0; i < objectives.size(); i++) {
//
//            String objectiveId = objectives.get(i).getId();
//
//            if (dto.getObjectiveIds().contains(objectiveId)) {
//                rpValues.add(0.0);
//            } else {
//                if (dto.isRestrictedRP()) {
//                    rpValues.add(1.0);
//                } else {
//                    rpValues.add(0.5);
//                }
//            }
//        }
//        
//        LOGGER.debug("RP: "+ rpValues);
//        
//        PointSolution zr = PointSolutionUtils.createSolution(rpValues);
//        
//        List<Execution> executions = new ArrayList<>();
//        
//        for (String executionId : dto.getExecutionIds()) {
//
//            Execution execution = executionService.findExecutionById(executionId);
//
//            List<NSolution<?>> solutions = execution.getSolutions();
//
//            if (dto.isFilterBySelectedSolutions()) {
//                solutions = filterBySelectedSolutions(execution);
//            }
//
//            List<NSolution<?>> recalculated = SolutionListUtils.recalculate(problem, solutions);
//
//            Map<String, Number> metrics = SolutionListUtils.calculateMetrics(problem, pfApprox, recalculated, zr, dto.getDelta());
//            
//            long executionTime = getExecutionTime(execution);
//            
//            metrics.put("execution-time", executionTime);
//            metrics.put("#-of-reductions", getNumberOfReductions(execution));
//            
//
//            if (execution.getSelectedSolutions().size() == 1) {
//
//                Execution parent = executionService.getParent(execution.getId());
//
//                Date selectedDate = execution.getSelectedSolutions().get(0).getSelectionDate();
//
//                long diff = selectedDate.getTime() - parent.getCreationDate().getTime();
//
//                metrics.put("all-time", diff);
//            }
//            
//            metrics.put("interation-time", (long)metrics.get("all-time") - (long)metrics.get("execution-time"));
//            
//            execution.setSolutions(recalculated);
//            execution.getAttributes().put("metrics", metrics);
//            
//            executions.add(execution);
//        }
//        
//        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
//        model.addAttribute("executions", executions);
//        model.addAttribute("objectiveIds", Converter.toJson(objectives.stream().map(e -> e.getId()).collect(Collectors.toList())));
//  
//        return "compare";
//    }
    
    @PostMapping("/result/2")
    public String resultAll(@Valid CompareDTO dto, BindingResult bindingResult, Model model, FormCompareDTO formCompareDTO) {
        
        if (bindingResult.hasErrors()) {
            return form(dto, formCompareDTO, model);
        }
        
        List<Execution> executions = new ArrayList<>();

        for (String executionId : dto.getExecutionIds()) {
            executions.add(executionService.findExecutionById(executionId));
        }
        
        User user = securityService.getLoggedUser().getUser();
        
        String problemId = executions.get(0).getProblemId();
        
        String instanceId = executions.get(0).getInstance();
        
        NormalizerExtension normalizerExtension = pluginService.getNormalizerExtensionById(new ByMaxAndMinValuesNormalizerExtension().getId());
        ProblemExtension problemExtension = pluginService.getProblemById(problemId);
        
        List<AbstractObjective> objectives = problemExtension.getObjectives();
        
        Path path = fileService.getInstance(problemId, instanceId);

        Instance instance = problemExtension.getInstance(path);

        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
        
        Execution pfApproxExecution = executionService.findParetoFrontApprox(problemId, instanceId);
        
        if(pfApproxExecution == null) {
            throw new RuntimeException("The pareto-front approx for "+problemId+" and "+instanceId+ " was not found");
        }
        
        List<NSolution<?>> pfApprox = pfApproxExecution.getSolutions();
        
        for (Execution execution : executions) {

            List<NSolution<?>> solutions = execution.getSolutions();

            if (dto.isFilterBySelectedSolutions()) {
                solutions = filterBySelectedSolutions(execution);
            }
            
            if (solutions.isEmpty()) {
                continue;
            }
            
            solutions = normalizerExtension.getNormalizer().normalize(objectives, solutions);

            Map<String, Number> metrics = calculateMetrics(problem, pfApprox, solutions);
            
            long executionTime = getExecutionTime(execution);
            
            metrics.put("execution-time", executionTime);
            
            execution.setSolutions(solutions);
            execution.getAttributes().put("metrics", metrics);
        }
        
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        model.addAttribute("executions", executions);
        model.addAttribute("objectiveIds", Converter.toJson(objectives.stream().map(e -> e.getId()).collect(Collectors.toList())));
  
        return "compare-2";
    }
    
    public static Map<String, Number> calculateMetrics(NProblem<?> problem, List<NSolution<?>> pfApprox,
            List<NSolution<?>> solutions) {
        
        AbstractNormalize normalizer = new ByMaxAndMinValuesNormalize();
        
        List<NSolution<?>> normalizedPf = (List<NSolution<?>>) normalizer.normalize(problem.getObjectives(), solutions);
        List<NSolution<?>> normalizedPfApprox = (List<NSolution<?>>) normalizer.normalize(problem.getObjectives(), pfApprox);
      
        List<PointSolution> pfSolutions = SolutionListUtils.getPointSolutions(normalizedPf);
        
        Map<String, Number> values = new HashMap<>();
        
//        values.put("hypervolume", new PISAHypervolume<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        values.put("igd", new InvertedGenerationalDistance<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        values.put("hypervolume-approx", new HypervolumeApprox<PointSolution>(new ArrayFront(normalizedPfApprox)).evaluate(pfSolutions));
        
        values.put("number-of-solutions", normalizedPf.size());
        
        return values;
    }
    
    private List<NSolution<?>> filterBySelectedSolutions(Execution execution) {

        List<NSolution<?>> selectedSolutions = new ArrayList<>();

        for (SelectedSolution selectedSolution : execution.getSelectedSolutions()) {

            int solutionIndex = selectedSolution.getSolutionIndex();

            NSolution<?> solution = execution.getSolutions().get(solutionIndex);

            selectedSolutions.add(solution);
        }

        return selectedSolutions;
    }
    
    private long getExecutionTime(Execution execution) {
        
        if (execution.getAlgorithmId().equalsIgnoreCase(new ManuallyExtension().getId())) {

            Date selectedDate = execution.getSelectedSolutions().get(0).getSelectionDate();

            return selectedDate.getTime() - execution.getCreationDate().getTime();
        }

        if (Strings.isBlank(execution.getLastExecutionId())) {
            return execution.getExecutionTime();
        }

        long executionTime = execution.getExecutionTime();

        Execution parent = executionService.findByIdOrNull(execution.getLastExecutionId());

        while (parent != null) {

            executionTime += parent.getExecutionTime();

            if (parent.getLastExecutionId() == null) {
                parent = null;
            } else {
                parent = executionService.findByIdOrNull(parent.getLastExecutionId());
            }
        }

        return executionTime;
    }
    
    private int getNumberOfReductions(Execution execution) {
        
        if (execution.getAlgorithmId().equalsIgnoreCase(new ManuallyExtension().getId())) {
            return 0;
        }

        if (Strings.isBlank(execution.getLastExecutionId())) {
            return 0;
        }

        int numberOfReductions = 0;

        Execution parent = executionService.findByIdOrNull(execution.getLastExecutionId());

        while (parent != null) {

            numberOfReductions += 1;

            if (parent.getLastExecutionId() == null) {
                parent = null;
            } else {
                parent = executionService.findByIdOrNull(parent.getLastExecutionId());
            }
        }

        return numberOfReductions;
    }
   
}
