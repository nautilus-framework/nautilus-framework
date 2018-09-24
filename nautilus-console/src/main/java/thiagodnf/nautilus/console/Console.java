package thiagodnf.nautilus.console;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.rnsgaii.RNSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.SolutionListUtils;
import thiagodnf.nautilus.plugin.mip.MIPPlugin;
import thiagodnf.nautilus.plugin.mip.objective.MaximumNumberObjective;
import thiagodnf.nautilus.plugin.mip.problem.MaximumIntegerProblem;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.Parameters;

public class Console {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {
		
		LOGGER.info("Starting....");
		
		Parameters parameters = new Parameters();
		
		parameters.setFilename("100.txt");
		parameters.setCrossoverDistribution(20.0);
		parameters.setCrossoverName("IntegerSBXCrossover");
		parameters.setCrossoverProbability(0.9);
		
		parameters.setMutationDistribution(20.0);
		parameters.setMutationName("IntegerPolynomialMutation");
		parameters.setMutationProbability(0.005);
		
		parameters.setPopulationSize(100);
		parameters.setMaxEvaluations(500000);
		parameters.setProblemKey(new MIPPlugin().getProblemKey());
		
		Path path = Paths.get("src/main/resources/instances/mip/" + parameters.getFilename());
		
		List<AbstractObjective> objectives = Arrays.asList(
			new MaximumNumberObjective(1),
			new MaximumNumberObjective(2)
//			new MaximumNumberObjective(3),
//			new MaximumNumberObjective(4),
//			new MaximumNumberObjective(5),
//			new MaximumNumberObjective(6),
//			new MaximumNumberObjective(7),
//			new MaximumNumberObjective(8),
//			new MaximumNumberObjective(9),
//			new MaximumNumberObjective(10)
		);		
		
		Problem problem = new MaximumIntegerProblem(path, objectives);
		
		CrossoverOperator<?> crossover = new IntegerSBXCrossover(parameters.getCrossoverProbability(), parameters.getCrossoverDistribution());
		MutationOperator<?> mutation = new IntegerPolynomialMutation(parameters.getMutationProbability(), parameters.getMutationDistribution());
		SelectionOperator selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());

		List<Double> referencePoint = new ArrayList<>();

		referencePoint.add(-0.8);
		referencePoint.add(-0.2);
		
//		referencePoint.add(-0.5);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(0.0);
//		referencePoint.add(-0.5);
		
		double epsilon = 0.001;

		LOGGER.info("Optimizing...");
		
		Algorithm algorithm = new RNSGAIIBuilder(problem, crossover, mutation, referencePoint, epsilon)
				.setSelectionOperator(selection)
				.setMaxEvaluations(parameters.getMaxEvaluations())
				.setPopulationSize(parameters.getPopulationSize())
				.build();

		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();

		List population = (List) algorithm.getResult();
		
		LOGGER.info("Removing repeated solutions...");
		
		List<Solution<?>> noRepeatedSolutions = SolutionListUtils.removeRepeated(population);
		
		List<thiagodnf.nautilus.core.model.Solution> solutions = Converter.toSolutions(noRepeatedSolutions);
		
		for(AbstractObjective objective : objectives) {
			parameters.getObjectiveKeys().add(objective.getKey());
		}
		
		LOGGER.info("Exporting the output...");
		
		Execution execution = new Execution();
		
		execution.setId("75BIksd2CR2");
		execution.setParameters(parameters);
		execution.setSolutions(solutions);
		execution.setExecutionTime(algorithmRunner.getComputingTime());
		execution.getSettings().setNormalize(new ByMaxAndMinValuesNormalize().getKey());
		
		FileUtils.writeStringToFile(new File("output.json"), execution.toString());
		
		LOGGER.info("Done");
	}
}
