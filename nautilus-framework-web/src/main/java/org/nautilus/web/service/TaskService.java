package org.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.core.remover.ObjectivesRemover;
import org.nautilus.core.util.SolutionListUtils;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.algorithm.ParetoFrontApproxExtension;
import org.nautilus.web.model.Execution;
import org.nautilus.web.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private InstanceService instanceService;

    @Autowired
    private FileService fileService;
    
    @Autowired
    private ExecutionService executionService;

    public void generateApproxParetoFronts() {

        User user = securityService.getLoggedUser().getUser();

        Map<ProblemExtension, List<Path>> problems = instanceService.getProblemAndInstances();

        for (Entry<ProblemExtension, List<Path>> entry : problems.entrySet()) {

            ProblemExtension problemExtension = entry.getKey();

            for (Path file : entry.getValue()) {
                
                String filename = file.getFileName().toString();

                List<Execution> executions = executionService.findByProblemIdAndInstance(problemExtension.getId(), filename);

                if (executions.isEmpty()) {
                    continue;
                }

                List<AbstractObjective> objectives = problemExtension.getObjectives();

                List<String> objectiveIds = objectives.stream().map(e -> e.getId()).collect(Collectors.toList());

                Path path = fileService.getInstanceLocation(problemExtension.getId(), filename);

                Instance instance = problemExtension.getInstance(path);

                NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);

                List<NSolution<?>> allSolutions = new ArrayList<>();

                for (Execution execution : executions) {

                    List<NSolution<?>> solutions = execution.getSolutions();

                    if (solutions != null) {
                        allSolutions.addAll(SolutionListUtils.recalculate(problem, solutions));
                    }
                }

                List<NSolution<?>> pfApprox = SolutionListUtils.getNondominatedSolutions(allSolutions);

                AbstractRemover remover = new ObjectivesRemover();

                pfApprox = remover.execute(pfApprox);

                Execution execution = executionService.findParetoFrontApprox(problemExtension.getId(), filename);

                if (execution == null) {

                    execution = new Execution();

                    execution.setUserId(user.getId());
                    execution.setAlgorithmId(new ParetoFrontApproxExtension().getId());
                    execution.setProblemId(problemExtension.getId());
                    execution.setInstance(filename);
                    execution.setMaxEvaluations(0);
                    execution.setReferencePoints(new ArrayList<>());
                    execution.setObjectiveIds(objectiveIds);
                }

                execution.setCreationDate(new Date());
                execution.setSolutions(pfApprox);
                execution.setPopulationSize(pfApprox.size());

                executionService.save(execution);
            }
        }
    }

}
