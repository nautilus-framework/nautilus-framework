package thiagodnf.nautilus.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
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
import thiagodnf.nautilus.plugin.mip.problem.MinimumIntegerProblem;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.util.Converter;

@Controller
public class OptimizeController {
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private SimpMessageSendingOperations messaging;
	
	@GetMapping("/optimize")
	public String optimize(Model model) {
		
		model.addAttribute("parameters", new Parameters());
		
		return "optimize";
	}
	
	@Async
	@MessageMapping("/hello.{sessionId}")
    public void execute(@Valid Parameters parameters, @DestinationVariable String sessionId) throws Exception {
        try {
        	
        	messaging.convertAndSend("/topic/optimize/title." + sessionId, "Initializing...");
        	
        	Thread.currentThread().setName("optimizing-" + sessionId);
			
			Problem<IntegerSolution> problem = new MinimumIntegerProblem(10);
			
			int populationSize = parameters.getPopulationSize();
			int maxEvaluations = parameters.getMaxEvaluations();
			
			CrossoverOperator<IntegerSolution> crossover = CrossoverFactory.<IntegerSolution>getCrossover("IntegerSBXCrossover", 0.9, 20.0);
			
			MutationOperator<IntegerSolution> mutation = MutationFactory.<IntegerSolution>getMutation("IntegerPolynomialMutation", 1.0 / problem.getNumberOfVariables(), 20.0);
			
		    NSGAII<? extends Solution<?>> algorithm = new NSGAII<IntegerSolution>(
		    		problem, maxEvaluations, populationSize, crossover, mutation);
			
		    algorithm.setOnProgressListener(new OnProgressListener() {
				
				@Override
				public void onProgress(double progress) {
					messaging.convertAndSend("/topic/optimize/progress." + sessionId, progress);
				}
			});
		    
		    messaging.convertAndSend("/topic/optimize/title." + sessionId, "Optimizing...");
		    
			AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
			        .execute() ;
			
			messaging.convertAndSend("/topic/optimize/title." + sessionId, "Converting the results...");
			
			Execution execution = new Execution();
			
			execution.setSolutions(Converter.toSolutions(algorithm.getResult()));
			execution.setExecutionTime(algorithmRunner.getComputingTime());
	
			messaging.convertAndSend("/topic/optimize/title." + sessionId, "Saving the execution...");
			
			execution = executionService.save(execution);
	
			messaging.convertAndSend("/topic/optimize/title." + sessionId, "Done");
			messaging.convertAndSend("/topic/optimize/done." + sessionId, execution.getId());
		} catch (Exception e) {
			messaging.convertAndSend("/topic/optimize/exception." + sessionId, e.getMessage());
		}
    }
}
