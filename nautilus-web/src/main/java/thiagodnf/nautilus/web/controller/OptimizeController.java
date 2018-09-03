package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Future;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;

import thiagodnf.nautilus.plugin.algorithm.NSGAII;
import thiagodnf.nautilus.plugin.factory.CrossoverFactory;
import thiagodnf.nautilus.plugin.factory.MutationFactory;
import thiagodnf.nautilus.plugin.listener.OnProgressListener;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.util.SolutionListUtils;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.WebSocketService;
import thiagodnf.nautilus.web.util.Converter;

@Controller
public class OptimizeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@GetMapping("/optimize/{problemKey}/{filename:.+}")
	public String optimize(Model model, @PathVariable("problemKey") String problemKey, @PathVariable("filename") String filename) {
		
		Parameters parameters = new Parameters();
		
		parameters.setProblemKey(problemKey);
		parameters.setFilename(filename);
		
		model.addAttribute("parameters", parameters);
		model.addAttribute("objectives", pluginService.getObjectives(problemKey));
		
		return "optimize";
	}
	
	@Async
	@MessageMapping("/hello.{sessionId}")
    public Future<?> execute(@Valid Parameters parameters, @DestinationVariable String sessionId) throws InterruptedException {
        try {
        	
        	webSocketService.sendTitle(sessionId, "Initializing...");
        	
        	Thread.currentThread().setName("optimizing-" + sessionId);
        	
        	String problemKey = parameters.getProblemKey();
        	int populationSize = parameters.getPopulationSize();
			int maxEvaluations = parameters.getMaxEvaluations();
        	
        	Path instance = fileService.getInstancesFile(problemKey, parameters.getFilename());
        	
        	List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
        	
        	Problem problem = pluginService.getProblem(problemKey, instance, objectives);
			
			CrossoverOperator<IntegerSolution> crossover = CrossoverFactory.<IntegerSolution>getCrossover("IntegerSBXCrossover", 0.9, 20.0);
			
			MutationOperator<IntegerSolution> mutation = MutationFactory.<IntegerSolution>getMutation("IntegerPolynomialMutation", 1.0 / problem.getNumberOfVariables(), 20.0);
			
		    NSGAII<? extends Solution<?>> algorithm = new NSGAII<IntegerSolution>(
		    		problem, maxEvaluations, populationSize, crossover, mutation);
			
		    algorithm.setOnProgressListener(new OnProgressListener() {
				
				@Override
				public void onProgress(double progress) {
					webSocketService.sendProgress(sessionId, progress);
				}
			});
		    
		    webSocketService.sendTitle(sessionId, "Optimizing...");
		    
		   	AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
				        .execute() ;
		   	
		   	webSocketService.sendTitle(sessionId, "Removing repeated solutions...");
		   	
		   	List<? extends Solution<?>> solutions = algorithm.getResult();
		   	
		   	List<Solution<?>> noRepeatedSolutions = SolutionListUtils.removeRepeated(solutions);
				
		   	webSocketService.sendTitle(sessionId, "Converting the results...");
			
			Execution execution = new Execution();
			
			execution.setSolutions(Converter.toSolutions(noRepeatedSolutions));
			execution.setExecutionTime(algorithmRunner.getComputingTime());
			execution.setParameters(parameters);
	
			webSocketService.sendTitle(sessionId, "Saving the execution...");
			
			execution = executionService.save(execution);
	
			webSocketService.sendTitle(sessionId, "Done. Redirecting...");
			webSocketService.sendProgress(sessionId, 100);
			webSocketService.sendDone(sessionId, execution.getId());
		} catch (Exception e) {
			webSocketService.sendException(sessionId, e.getMessage());
			throw new InterruptedException();
		}
        
        return new AsyncResult<>("Success");
    }
}
