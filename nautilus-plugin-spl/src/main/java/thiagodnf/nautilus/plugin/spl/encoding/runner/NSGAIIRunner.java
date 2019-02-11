package thiagodnf.nautilus.plugin.spl.encoding.runner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.problem.SPLProblem;
import thiagodnf.nautilus.plugin.spl.extension.instance.SPLInstanceDataExtension;
import thiagodnf.nautilus.plugin.spl.extension.objective.SPLObjectiveExtension;

public class NSGAIIRunner {

	private static Path path = Paths.get("src", "main", "resources", "instances", "drupal.txt");
	
	public static void main(String[] args) {
		
		System.out.println("Loading...");
		
		List<AbstractObjective> objectives = new SPLObjectiveExtension().getObjectives();
		InstanceData instance = new SPLInstanceDataExtension().getInstanceData(path);
		
		Problem<BinarySolution> problem = new SPLProblem(instance, objectives);
		
		CrossoverOperator<BinarySolution> crossover = new SinglePointCrossover(0.9) ;
	    MutationOperator<BinarySolution> mutation = new BitFlipMutation(0.005) ;
	    SelectionOperator<List<BinarySolution>, BinarySolution> selection = new BinaryTournamentSelection<BinarySolution>(
	        new RankingAndCrowdingDistanceComparator<BinarySolution>());

	    Algorithm<List<BinarySolution>> algorithm = new NSGAIIBuilder<BinarySolution>(problem, crossover, mutation)
	        .setSelectionOperator(selection)
	        .setMaxEvaluations(10000)
	        .setPopulationSize(100)
	        .build() ;
	    
	    System.out.println("Optimizing...");
	    
	    new AlgorithmRunner.Executor(algorithm).execute() ;
	    
	    List<BinarySolution> population = algorithm.getResult() ;
	    
	    for(BinarySolution solution : population) {
	    	System.out.println(Arrays.toString(solution.getObjectives()));
	    }
	    
	    System.out.println("Done");
	}
}
