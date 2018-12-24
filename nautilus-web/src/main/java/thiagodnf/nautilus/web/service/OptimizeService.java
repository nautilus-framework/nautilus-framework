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
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.factory.AlgorithmFactory;
import thiagodnf.nautilus.plugin.factory.CrossoverFactory;
import thiagodnf.nautilus.plugin.factory.MutationFactory;
import thiagodnf.nautilus.plugin.factory.SelectionFactory;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;

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
    public CompletableFuture<Stats> execute(Parameters parameters, String sessionId) {
 
    	try {
        	
        	System.out.println(parameters);
        	
			webSocketService.sendTitle(sessionId, "Initializing...");
        	
        	Thread.currentThread().setName("optimizing-" + sessionId);
        	
        	String pluginId = parameters.getPluginId();
			String problemId = parameters.getProblemId();
			String filename = parameters.getFilename();
			
			Path instance = fileService.getInstanceFile(pluginId, problemId, filename);
			
			List<AbstractObjective> objectives = pluginService.getObjectivesByIds(pluginId, problemId, parameters.getObjectiveIds());
        	
			ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
			InstanceDataExtension instanceDataExtension = pluginService.getInstanceDataExtension(pluginId, problemId);
			
			InstanceData instanceData = instanceDataExtension.getInstanceData(instance);
			
			// Factories
			
			AlgorithmFactory algorithmFactory = pluginService.getAlgorithmFactory(pluginId);
			SelectionFactory selectionFactory = pluginService.getSelectionFactory(pluginId);
			CrossoverFactory crossoverFactory = pluginService.getCrossoverFactory(pluginId);
			MutationFactory mutationFactory = pluginService.getMutationFactory(pluginId);
			
			// Builder
			
			Builder builder = new Builder();
			
			builder.setPopulationSize(parameters.getPopulationSize());
			builder.setMaxEvaluations(parameters.getMaxEvaluations());
			builder.setProblem(problemExtension.getProblem(instanceData, objectives));
			builder.setReferencePoints(Converter.toReferencePoints(parameters.getReferencePoints()));
			builder.setEpsilon(parameters.getEpsilon());
			
			webSocketService.sendTitle(sessionId, "Loading last execution if it exists...");
			
			builder.setInitialPopulation(getInitialPopulation(builder.getProblem(), parameters.getLastExecutionId()));
			
			builder.setSelection(selectionFactory.getSelection(parameters.getSelectionId()));
			builder.setCrossover(crossoverFactory.getCrossover(parameters.getCrossoverId(), parameters.getCrossoverProbability(), parameters.getCrossoverDistribution()));
			builder.setMutation(mutationFactory.getMutation(parameters.getMutationId(), parameters.getMutationProbability(), parameters.getMutationDistribution()));
			
			// Algorithm
			
			List<NSolution<?>> rawSolutions = null;
			
			webSocketService.sendTitle(sessionId, "Optimizing...");
			
			Algorithm algorithm = algorithmFactory.getAlgorithm(parameters.getAlgorithmId(), builder);

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
			}
		   	
			webSocketService.sendTitle(sessionId, "Preparing the results...");
			
		   	Execution execution = new Execution();
		   			
			execution.setSolutions(rawSolutions);
			execution.setParameters(parameters);
			execution.setExecutionTime(algorithmRunner.getComputingTime());
	
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

		Execution previousExecution = executionService.findById(previousExecutionId);

		List<NSolution<?>> initialPopulation = new ArrayList<>();

		for (NSolution<?> sol : previousExecution.getSolutions()) {

			NSolution<?> copy = (NSolution<?>) sol.copy();

			copy.setObjectives(new double[problem.getNumberOfObjectives()]);

			initialPopulation.add(copy);
		}

		return initialPopulation;
	}
}
