package thiagodnf.nautilus.web.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.plugin.mip.problem.MinimumIntegerProblem;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Population;
import thiagodnf.nautilus.web.service.PopulationService;
import thiagodnf.nautilus.web.util.Converter;
import thiagodnf.nautilus.web.websocket.WebSocketOnConnected;

@Controller
public class OptimizeController {
	
	private static final Logger logger = LoggerFactory.getLogger(WebSocketOnConnected.class);
	
	@Autowired
	private PopulationService parentoFrontService;
	
	//@Autowired
	//private WebSocketEventConfiguration event;
	@Autowired
	private SimpMessageSendingOperations messaging;
	
	@MessageMapping("/hello.{sessionId}")
   // @SendTo("/user/topic/greetings.{id}")
	@Async
    public void greeting(@Valid Parameters parameters, @DestinationVariable String sessionId) throws Exception {
        
		messaging.convertAndSend("/topic/optimize/title." + sessionId, "Optimizing...");
		
		logger.info("Optimizing....");
		logger.info(sessionId);
		
		Thread.currentThread().setName("optimizing-" + sessionId);
		
		//System.out.println(Thread.currentThread());
		
		for (int i = 1; i <= 100; i++) {
			Thread.sleep(parameters.getMaxEvaluations()); // simulated delay
			messaging.convertAndSend("/topic/optimize/progress." + sessionId, i);
			System.out.println(i);
			
			System.out.println(Thread.currentThread().isInterrupted());
		}
		
		
		
		
		logger.info("Done");
		
		//System.out.println(principal);
		//messaging.convertAndSend(payload);
		//System.out.println(parameters);
		
        //return "oi";
    }
	
//	@PostMapping("/optimize")
//	public void optimize(@Valid Parameters parameters, Principal principal) {
//		System.out.println("oi");
//		
//		
//		//messaging.convertAndSendToUser(principal.getName(), "/topic/optimize.title." + sessionId, "Optimizing...");
//		
//	}
	
	
//	@PostMapping("/optimize")
//	public String optimize(@Valid Parameters parameters) {
//		System.out.println("oi");
//		
//		
//		return "result";
//	}

	//@GetMapping("/execute")
	public String index(Model model) {
		
		System.out.println("oi");
		
		Problem<IntegerSolution> problem = new MinimumIntegerProblem(10);
		
		double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 20.0 ;
	    CrossoverOperator<IntegerSolution> crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
	    MutationOperator<IntegerSolution> mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex) ;
		
	    SelectionOperator<List<IntegerSolution>, IntegerSolution> selection = new BinaryTournamentSelection<IntegerSolution>(
		        new RankingAndCrowdingDistanceComparator<IntegerSolution>());
		
	    Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation)
		        .setSelectionOperator(selection)
		        .setMaxEvaluations(25000)
		        .setPopulationSize(100)
		        .build() ;
		
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
		        .execute() ;
		
		Population population = new Population();

		population.setSolutions(Converter.toSolutions(algorithm.getResult()));
		population.setExecutionTime(algorithmRunner.getComputingTime());

		population = parentoFrontService.save(population);
		
		System.out.println(population.getId());
				
		//System.out.println(population);
		
		return "index";
	}
}
