package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.model.SelectedSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.ManuallyExtension;
import thiagodnf.nautilus.web.dto.CompareDTO;
import thiagodnf.nautilus.web.dto.ExecutionSimplifiedDTO;
import thiagodnf.nautilus.web.dto.UserDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;
import thiagodnf.rnsgaii.util.PointSolutionUtils;

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
    public String form(CompareDTO compareDTO, Model model) {

        User user = securityService.getLoggedUser().getUser();

        Map<String, ProblemExtension> problems = pluginService.getProblems();
        
        List<UserDTO> users = userService.findAll();
        
        ProblemExtension problem = pluginService.getProblemById("spl-problem");
        
        model.addAttribute("users", users);
        model.addAttribute("objectives", problem.getObjectives());
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        model.addAttribute("executions", findAllExecutions(users));
        model.addAttribute("problems", problems);
        model.addAttribute("compareDTO", compareDTO);
        
        return "form-compare";
    }
    
    private List<ExecutionSimplifiedDTO> findAllExecutions( List<UserDTO> users){
        
        List<ExecutionSimplifiedDTO> executions = new ArrayList<>();
        
        for(UserDTO userDTO : users) {
            
            List<ExecutionSimplifiedDTO> found = executionService.findExecutionSimplifiedDTOByUserId(userDTO.getId());
            
            found = found.stream()
                    .filter(e -> !e.getSelectedSolutions().isEmpty())
                    .collect(Collectors.toList());
            
            executions.addAll(found);
        }
        
        return executions;
    }

    @PostMapping("/result")
    public String show(@Valid CompareDTO dto, BindingResult bindingResult, Model model) {
        
        System.out.println(dto);

        if (bindingResult.hasErrors()) {
            return form(dto, model);
        }
        
        User user = securityService.getLoggedUser().getUser();
        
        String problemId = "spl-problem";
        
        String instanceId = "eshop.txt";
        
        ProblemExtension problemExtension = pluginService.getProblemById(problemId);
        
        List<AbstractObjective> objectives = problemExtension.getObjectives();
        
        Path path = fileService.getInstance(problemId, instanceId);

        Instance instance = problemExtension.getInstance(path);

        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
        
        Execution pfApproxExecution = executionService.findParetoFrontApprox(problemId, instanceId);
        
        if(pfApproxExecution == null) {
            throw new RuntimeException("The pareto-front approx for "+problemId+" and "+instanceId+ "was not found");
        }
        
        List<NSolution<?>> pfApprox = pfApproxExecution.getSolutions();
        
        
        List<Double> rpValues = new ArrayList<>();
        
        for (int i = 0; i < objectives.size(); i++) {

            String objectiveId = objectives.get(i).getId();

            if (dto.getObjectiveIds().contains(objectiveId)) {
                rpValues.add(0.0);
            } else {
                if (dto.isRestrictedRP()) {
                    rpValues.add(1.0);
                } else {
                    rpValues.add(0.5);
                }
            }
        }
        
        LOGGER.debug("RP: "+ rpValues);
        
        PointSolution zr = PointSolutionUtils.createSolution(rpValues);
        
        List<Execution> executions = new ArrayList<>();
        
        for (String executionId : dto.getExecutionIds()) {

            Execution execution = executionService.findExecutionById(executionId);

            List<NSolution<?>> solutions = execution.getSolutions();

            if (dto.isFilterBySelectedSolutions()) {
                solutions = filterBySelectedSolutions(execution);
            }

            List<NSolution<?>> recalculated = SolutionListUtils.recalculate(problem, solutions);

            Map<String, Number> metrics = SolutionListUtils.calculateMetrics(problem, pfApprox, recalculated, zr, dto.getDelta());
            
            long executionTime = getExecutionTime(execution);
            
            metrics.put("execution-time", executionTime);

            if (execution.getSelectedSolutions().size() == 1) {

                Execution parent = executionService.getParent(execution.getId());

                Date selectedDate = execution.getSelectedSolutions().get(0).getSelectionDate();

                long diff = selectedDate.getTime() - parent.getCreationDate().getTime();

                metrics.put("all-time", diff);
            }
            
            execution.setSolutions(recalculated);
            execution.getAttributes().put("metrics", metrics);
            
            executions.add(execution);
        }
        
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        model.addAttribute("executions", executions);
        model.addAttribute("objectiveIds", Converter.toJson(objectives.stream().map(e -> e.getId()).collect(Collectors.toList())));
  
        return "compare";
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

        Execution parent = executionService.findById(execution.getLastExecutionId());

        while (parent != null) {

            executionTime += parent.getExecutionTime();

            if (parent.getLastExecutionId() == null) {
                parent = null;
            } else {
                parent = executionService.findById(parent.getLastExecutionId());
            }
        }

        return executionTime;
    }
   
}
