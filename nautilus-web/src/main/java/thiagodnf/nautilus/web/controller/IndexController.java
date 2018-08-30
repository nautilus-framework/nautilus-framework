package thiagodnf.nautilus.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.plugin.mip.problem.IntegerProblem;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String index(Model model) {
		
		System.out.println("oi");
		
		Problem<IntegerSolution> problem = new IntegerProblem(10);
		
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
		
		List<IntegerSolution> population = algorithm.getResult() ;
		
		System.out.println(population);
		
		return "index";
	}
}
