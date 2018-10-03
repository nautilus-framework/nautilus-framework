package thiagodnf.nautilus.web.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
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
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;

import thiagodnf.nautilus.core.algorithm.GA;
import thiagodnf.nautilus.core.algorithm.NSGAII;
import thiagodnf.nautilus.core.factory.CrossoverFactory;
import thiagodnf.nautilus.core.factory.MutationFactory;
import thiagodnf.nautilus.core.listener.OnProgressListener;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.WebSocketService;

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
	
	@GetMapping("/optimize/{problemKey:.+}/{filename:.+}")
	public String optimize(Model model, 
			@PathVariable("problemKey") String problemKey, 
			@PathVariable("filename") String filename) {
		
		Parameters parameters = new Parameters();
		
		parameters.setProblemKey(problemKey);
		parameters.setFilename(filename);
		
		model.addAttribute("parameters", parameters);
		model.addAttribute("plugin", pluginService.getPlugin(problemKey));
		
		model.addAttribute("objectiveAdapter", pluginService.getObjectiveAdapter(problemKey));
		model.addAttribute("operatorAdapter", pluginService.getOperatorAdapter(problemKey));
		
		return "optimize";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Solution> getInitialPopulation(Problem problem, Execution execution) {

		if (execution == null) {
			return null;
		}

		List<Solution> initialPopulation = new ArrayList<>();

		for (thiagodnf.nautilus.core.model.Solution sol : execution.getSolutions()) {
			
			Solution s = Converter.toSolutionWithOutObjectives(problem, sol);
			
			problem.evaluate(s);
			
			initialPopulation.add(s);
		}

		return initialPopulation;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Async
	@MessageMapping("/hello.{sessionId}")
    public Future<?> execute(
    		@Valid Parameters parameters, 
    		@DestinationVariable String sessionId) throws InterruptedException {
		
        try {

        	System.out.println(parameters);
        	
			String lastExecutionId = parameters.getLastExecutionId();

			Execution lastExecution = null;

			if (lastExecutionId != null) {
				lastExecution = executionService.findById(lastExecutionId);
			}
			
			webSocketService.sendTitle(sessionId, "Initializing...");
        	
        	Thread.currentThread().setName("optimizing-" + sessionId);
        	
        	String problemKey = parameters.getProblemKey();
        	int populationSize = parameters.getPopulationSize();
			int maxEvaluations = parameters.getMaxEvaluations();
			
			Path instance = fileService.getInstancesFile(problemKey, parameters.getFilename());
        	
			List<AbstractObjective> objectives = pluginService.getObjectives(problemKey, parameters.getObjectiveKeys());
        	
			Problem problem = pluginService.getProblem(problemKey, instance, objectives);
			
			List<?> initialPopulation = getInitialPopulation(problem, lastExecution);
        	
			AbstractPlugin plugin = pluginService.getPlugin(problemKey);
			
			CrossoverOperator crossover = plugin.getOperatorAdapter().getCrossover(parameters.getCrossoverName());
			
			
			MutationOperator mutation = MutationFactory.getMutation(parameters.getMutationName(), parameters.getMutationProbability(), parameters.getMutationDistribution());
			
			
			
			List<? extends Solution<?>> rawSolutions = null;
			
			AlgorithmRunner algorithmRunner = null;
			
			if(objectives.size() == 1) {
				
				GA<? extends Solution<?>> ga = new GA<>(
		    		problem, 
		    		maxEvaluations, 
		    		populationSize, 
		    		crossover, 
		    		mutation, 
		    		initialPopulation
			    );
				
				ga.setOnProgressListener(new OnProgressListener() {
					
					@Override
					public void onProgress(double progress) {
						webSocketService.sendProgress(sessionId, progress);
					}
				});
				
				webSocketService.sendTitle(sessionId, "Optimizing...");
				
				algorithmRunner = new AlgorithmRunner.Executor(ga).execute() ;
				
				rawSolutions = Arrays.asList(ga.getResult());
				
			}else {
			    
				NSGAII<? extends Solution<?>> nsgaii = new NSGAII<>(
		    		problem, 
		    		maxEvaluations, 
		    		populationSize, 
		    		crossover, 
		    		mutation, 
		    		initialPopulation
			    );
			    
			    nsgaii.setOnProgressListener(new OnProgressListener() {
					
					@Override
					public void onProgress(double progress) {
						webSocketService.sendProgress(sessionId, progress);
					}
				});
			    
			    webSocketService.sendTitle(sessionId, "Optimizing...");
			    
			    algorithmRunner = new AlgorithmRunner.Executor(nsgaii).execute() ;
			    
			    rawSolutions = nsgaii.getResult();
			}
		    
		   	webSocketService.sendTitle(sessionId, "Removing repeated solutions...");
		   	
		   	List<Solution<?>> noRepeatedSolutions = SolutionListUtils.removeRepeated(rawSolutions);
		   	
		   	webSocketService.sendTitle(sessionId, "Converting the solutions...");
		   	
		   	List<thiagodnf.nautilus.core.model.Solution> solutions = Converter.toSolutions(noRepeatedSolutions);
		   	
		   	webSocketService.sendTitle(sessionId, "Preparing the results...");
			
		   	Execution execution = new Execution();
		   			
			execution.setSolutions(solutions);
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
	
	@MessageExceptionHandler
	public void handleException(Throwable exception, @DestinationVariable String sessionId) {
		webSocketService.sendException(sessionId, exception.getMessage());
    }
}
