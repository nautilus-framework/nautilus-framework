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
import org.springframework.web.bind.annotation.RequestMapping;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.GA;
import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.factory.AlgorithmFactory;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.WebSocketService;

@Controller
@RequestMapping("/optimize/{pluginId:.+}/{problemId:.+}/{filename:.+}")
public class OptimizeController {
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@GetMapping("")
	public String optimize(Model model, 
			@PathVariable("pluginId") String pluginId, 
			@PathVariable("problemId") String problemId, 
			@PathVariable("filename") String filename){
		
		Parameters parameters = new Parameters();
		
		parameters.setPluginId(pluginId);
		parameters.setProblemId(problemId);
		parameters.setFilename(filename);
		
		model.addAttribute("parameters", parameters);
		model.addAttribute("filename", filename);
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		
		model.addAttribute("algorithmFactory", pluginService.getAlgorithmFactory(pluginId));
		model.addAttribute("selections", pluginService.getSelections(pluginId, problemId));
		model.addAttribute("crossovers", pluginService.getCrossovers(pluginId, problemId));
		model.addAttribute("mutations", pluginService.getMutations(pluginId, problemId));
		model.addAttribute("objectiveGroups", pluginService.getObjectivesByGroups(pluginId, problemId));
		
		return "optimize";
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Solution> getInitialPopulation(Problem problem, Execution execution) {

		if (execution == null) {
			return null;
		}

		List<Solution> initialPopulation = new ArrayList<>();

		for (thiagodnf.nautilus.core.model.Solution sol : execution.getSolutions()) {
			
			Solution s = Converter.toJMetalSolutionWithOutObjectives(problem,sol);
			
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
        	
        	String pluginId = parameters.getPluginId();
			String problemId = parameters.getProblemId();
			
			Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
			
			List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveKeys());
        	
			ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
			
			InstanceData data = problemExtension.readInstanceData(instance);
			
			// Factories
			
			AlgorithmFactory algorithmFactory = pluginService.getAlgorithmFactory(pluginId);
			
			// Builder
			
			Builder builder = new Builder();
			
			builder.setPopulationSize(parameters.getPopulationSize());
			builder.setMaxEvaluations(parameters.getMaxEvaluations());
			builder.setProblem(problemExtension.createProblem(data, objectives));
			builder.setInitialPopulation(getInitialPopulation(builder.getProblem(), lastExecution));
        	
			builder.setSelection(pluginService.getSelectionsById(pluginId, problemId, parameters.getSelectionName()));
			
			builder.setCrossover(pluginService.getCrossoversById(pluginId, problemId, parameters.getCrossoverName()));
			builder.getCrossover().setDistributionIndex(parameters.getCrossoverDistribution());
			builder.getCrossover().setProbability(parameters.getCrossoverProbability());

			builder.setMutation(pluginService.getMutationsById(pluginId, problemId, parameters.getMutationName()));
			builder.getMutation().setDistributionIndex(parameters.getMutationDistribution());
			builder.getMutation().setProbability(parameters.getMutationProbability());
			
			// Algorithm
			
			List<? extends Solution<?>> rawSolutions = null;
			
			webSocketService.sendTitle(sessionId, "Optimizing...");
			
			AlgorithmRunner algorithmRunner = null;
			
			if(objectives.size() == 1) {
				
				GA<? extends Solution<?>> ga = new GA<>(builder);
				
				ga.setOnProgressListener(new OnProgressListener() {
					
					@Override
					public void onProgress(double progress) {
						webSocketService.sendProgress(sessionId, progress);
					}
				});
				
				algorithmRunner = new AlgorithmRunner.Executor(ga).execute() ;
				
				rawSolutions = Arrays.asList(ga.getResult());
				
			}else {
				
				Algorithm algorithm = algorithmFactory.getAlgorithm(parameters.getAlgorithmName(), builder);
			    
				AlgorithmListener alg = (AlgorithmListener) algorithm;
				
				alg.setOnProgressListener(new OnProgressListener() {
					
					@Override
					public void onProgress(double progress) {
						webSocketService.sendProgress(sessionId, progress);
					}
				});
				
				algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute() ;
			    
			    rawSolutions = (List<? extends Solution<?>>) algorithm.getResult();
			}
		    
//		   	webSocketService.sendTitle(sessionId, "Removing repeated solutions...");
//		   	
//		   	List<Solution<?>> noRepeatedSolutions = SolutionListUtils.removeRepeated(rawSolutions);
		   	
		   	webSocketService.sendTitle(sessionId, "Converting the solutions...");
		   	
		   	List<thiagodnf.nautilus.core.model.Solution> solutions = Converter.toSolutions(rawSolutions);
		   	
		   	
			//webSocketService.sendTitle(sessionId, "Removing repeated solutions...");
		   	
		   	//List<thiagodnf.nautilus.core.model.Solution> noRepeatedSolutions = SolutionListUtils.removeRepeated(solutions);
		   	
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
