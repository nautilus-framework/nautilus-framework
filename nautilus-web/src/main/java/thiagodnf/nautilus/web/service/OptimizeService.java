package thiagodnf.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner.Executor;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.GA;
import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;
import thiagodnf.nautilus.web.dto.OptimizeDTO;
import thiagodnf.nautilus.web.model.Execution;

@Service
public class OptimizeService {
	
	public class Stats {
		
		public String content;
		
		public Stats(String content) {
			this.content = content;
		}
	}
	
	public class SuccessStats extends Stats{
		
		public SuccessStats(String content) {
			super(content);
		}
	}
	
	public class ErrorStats extends Stats{
		
		public ErrorStats(String content) {
			super(content);
		}
	}

	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private WebSocketService webSocketService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Async
    public CompletableFuture<Stats> execute(OptimizeDTO optimizeDTO, String sessionId) {
 
    	try {
        	
        	System.out.println(optimizeDTO);
        	
			webSocketService.sendTitle(sessionId, "Initializing...");
        	
        	Thread.currentThread().setName("optimizing-" + sessionId);
        	
        	ProblemExtension problemExtension = pluginService.getProblemById(optimizeDTO.getProblemId());
        	
        	Path path = fileService.getInstance(optimizeDTO.getProblemId(), optimizeDTO.getInstance());
			
			List<AbstractObjective> objectives = problemExtension.getObjectiveByIds(optimizeDTO.getObjectiveIds());
        	
			Instance instance = problemExtension.getInstance(path);
			
			// Factories
			
			AlgorithmExtension algorithmExtension = pluginService.getAlgorithmExtensionById(optimizeDTO.getAlgorithmId());
			SelectionExtension selectionExtension = pluginService.getSelectionExtensionById(optimizeDTO.getSelectionId());
			CrossoverExtension crossoverExtension = pluginService.getCrossoverExtensionById(optimizeDTO.getCrossoverId());
			MutationExtension mutationExtension = pluginService.getMutationExtensionById(optimizeDTO.getMutationId());
			
			// Builder
			
			Builder builder = new Builder();
			
			builder.setPopulationSize(optimizeDTO.getPopulationSize());
			builder.setMaxEvaluations(optimizeDTO.getMaxEvaluations());
			builder.setProblem(problemExtension.getProblem(instance, objectives));
			builder.setReferencePoints(Converter.toReferencePoints(optimizeDTO.getReferencePoints()));
			builder.setEpsilon(optimizeDTO.getEpsilon());
			
			webSocketService.sendTitle(sessionId, "Loading last execution if it exists...");
			
			builder.setInitialPopulation(getInitialPopulation(builder.getProblem(), optimizeDTO.getLastExecutionId()));
			
			builder.setSelection(selectionExtension.getSelection());
			builder.setCrossover(crossoverExtension.getCrossover(optimizeDTO.getCrossoverProbability(), optimizeDTO.getCrossoverDistribution()));
			builder.setMutation(mutationExtension.getMutation(optimizeDTO.getMutationProbability(), optimizeDTO.getMutationDistribution()));
			
			// Algorithm
			
			List<NSolution<?>> rawSolutions = null;
			
			webSocketService.sendTitle(sessionId, "Optimizing...");
			
			Algorithm algorithm = algorithmExtension.getAlgorithm(builder);

			AlgorithmListener alg = (AlgorithmListener) algorithm;

			alg.setOnProgressListener(new OnProgressListener() {

				@Override
				public void onProgress(double progress) {
					webSocketService.sendProgress(sessionId, progress);
				}
			});

			AlgorithmRunner algorithmRunner = new Executor(algorithm).execute();

			if (algorithm instanceof GA) {
				rawSolutions = (List<NSolution<?>>)(Object) Arrays.asList(algorithm.getResult());
			} else {
				rawSolutions = (List<NSolution<?>>) algorithm.getResult();
			}

			webSocketService.sendTitle(sessionId, "Setting ids to solutions...");

			for (int i = 0; i < rawSolutions.size(); i++) {
				rawSolutions.get(i).getAttributes().clear();
				rawSolutions.get(i).setAttribute(SolutionAttribute.ID, String.valueOf(i));
				rawSolutions.get(i).setAttribute(SolutionAttribute.OPTIMIZED_OBJECTIVES, Converter.toJson(optimizeDTO.getObjectiveIds()));
			}
		   	
			webSocketService.sendTitle(sessionId, "Preparing the results...");
			
		   	Execution execution = new Execution();
		   	
		   	execution.setSolutions(rawSolutions);
		   	execution.setUserId(optimizeDTO.getUserId());
		   	
		   	execution.setExecutionTime(algorithmRunner.getComputingTime());
			
			execution.setAlgorithmId(optimizeDTO.getAlgorithmId());
			execution.setProblemId(optimizeDTO.getProblemId());
			execution.setInstance(optimizeDTO.getInstance());
			execution.setPopulationSize(optimizeDTO.getPopulationSize());
			execution.setMaxEvaluations(optimizeDTO.getMaxEvaluations());
			execution.setSelectionId(optimizeDTO.getSelectionId());
			execution.setCrossoverId(optimizeDTO.getCrossoverId());
			execution.setCrossoverProbability(optimizeDTO.getCrossoverProbability());
			execution.setCrossoverDistribution(optimizeDTO.getCrossoverDistribution());
			execution.setMutationId(optimizeDTO.getMutationId());
			execution.setMutationProbability(optimizeDTO.getMutationProbability());
			execution.setMutationDistribution(optimizeDTO.getMutationDistribution());
			execution.setReferencePoints(optimizeDTO.getReferencePoints());
			execution.setEpsilon(optimizeDTO.getEpsilon());
			execution.setObjectiveIds(optimizeDTO.getObjectiveIds());
			execution.setShowToAllUsers(optimizeDTO.isShowToAllUsers());
			execution.setShowLines(optimizeDTO.isShowLines());
			execution.setColorizeId(optimizeDTO.getColorizeId());
			execution.setNormalizeId(optimizeDTO.getNormalizeId());
			execution.setCorrelationId(optimizeDTO.getCorrelationId());
			execution.setDuplicatesRemoverId(optimizeDTO.getDuplicatesRemoverId());
		    execution.setReducerId(optimizeDTO.getReducerId());
		    
			webSocketService.sendTitle(sessionId, "Saving the execution to database...");
			
			execution = executionService.save(execution);
			
			return CompletableFuture.completedFuture(new SuccessStats(execution.getId()));
		} catch (Exception e) {
			return CompletableFuture.completedFuture(new ErrorStats(e.getMessage())); 
		}
    }
	
	public List<NSolution<?>> getInitialPopulation(Problem<?> problem, String previousExecutionId) {

		if (Strings.isBlank(previousExecutionId)) {
			return null;
		}

		Execution previousExecution = executionService.findExecutionById(previousExecutionId);

		List<NSolution<?>> initialPopulation = new ArrayList<>();

		for (NSolution<?> sol : previousExecution.getSolutions()) {

			NSolution<?> copy = (NSolution<?>) sol.copy();

			copy.setObjectives(new double[problem.getNumberOfObjectives()]);

			initialPopulation.add(copy);
		}

		return initialPopulation;
	}
}
