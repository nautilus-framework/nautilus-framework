package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NProblem;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.encoding.problem.NBinaryProblem;
import thiagodnf.nautilus.core.encoding.problem.NIntegerProblem;
import thiagodnf.nautilus.core.encoding.solution.NBinarySolution;
import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.model.SelectedSolution;
import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.CompareDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.service.UserService;

@Controller
@RequestMapping("/compare")
public class CompareController {

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
        
        model.addAttribute("userSettingsDTO", userService.findUserSettingsDTOById(user.getId()));
        //model.addAttribute("executions", executionService.findExecutionSimplifiedDTOByUserId(user.getId()));
        model.addAttribute("problems", problems);
        model.addAttribute("compareDTO", compareDTO);
        
        return "form-compare";
    }

    @PostMapping("/result")
    public String show(@Valid CompareDTO dto, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return form(dto, model);
        }
        
        ProblemExtension problemExtension = pluginService.getProblemById(dto.getProblemId());
        
        List<AbstractObjective> objectives = problemExtension.getObjectives();
        
        List<Execution> executions = new ArrayList<>();
        
        for (String executionId : dto.getExecutionIds()) {

            Execution execution = executionService.findExecutionById(executionId);

            List<NSolution<?>> selectedSolutions = new ArrayList<>();

            for (SelectedSolution selectedSolution: execution.getSelectedSolutions()) {
                
                int solutionIndex = selectedSolution.getSolutionIndex();
                
                NSolution<?> solution = execution.getSolutions().get(solutionIndex);
                
                selectedSolutions.add(solution);
            }

            execution.setSolutions(selectedSolutions);

            executions.add(execution);
        }
        
        for (Execution execution : executions) {
            
            Path path = fileService.getInstance(execution.getProblemId(), execution.getInstance());
    
            Instance instance = problemExtension.getInstance(path);
    
            NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
            
            List<NSolution<?>> recalculated = new ArrayList<>();

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
                
                recalculated.add((NSolution<?>) newSolution);
            }
            
            execution.setSolutions(recalculated);
        }
        
        AbstractNormalize normalizer = new ByMaxAndMinValuesNormalize();

        for (Execution execution : executions) {
            execution.setSolutions(normalizer.normalize(objectives, execution.getSolutions()));
        }

        model.addAttribute("executions", executions);
        model.addAttribute("objectiveIds", objectives);

        return "compare";
    }
}
