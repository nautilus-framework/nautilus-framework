package thiagodnf.nautilus.web.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Console {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {

//		LOGGER.info("Starting....");
//		
//		Parameters parameters = new Parameters();
//		
//		parameters.setFilename("050.txt");
//		parameters.setCrossoverDistribution(20.0);
//		parameters.setCrossoverName("IntegerSBXCrossover");
//		parameters.setCrossoverProbability(0.9);
//		
//		parameters.setMutationDistribution(20.0);
//		parameters.setMutationName("IntegerPolynomialMutation");
//		parameters.setMutationProbability(0.005);
//		
//		parameters.setPopulationSize(100);
//		parameters.setMaxEvaluations(500000);
//		parameters.setProblemKey(new MIPPlugin().getProblemKey());
//		
//		Path path = Paths.get("files/instances/maximum-integer-problem/" + parameters.getFilename());
//		
//		List<AbstractObjective> objectives = Arrays.asList(
//			new MaximumNumberObjective(1),
//			new MaximumNumberObjective(2)
////			new MaximumNumberObjective(3),
////			new MaximumNumberObjective(4)
//		);
//		
//		Problem problem = new MaximumIntegerProblem(path, objectives);
//		
//		CrossoverOperator crossover = new IntegerSBXCrossover(parameters.getCrossoverProbability(), parameters.getCrossoverDistribution());
//		MutationOperator mutation = new IntegerPolynomialMutation(parameters.getMutationProbability(), parameters.getMutationDistribution());
//		SelectionOperator selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
//
//		List<Double> referencePoint = new ArrayList<>();
//
//		referencePoint.add(0.6);
//		referencePoint.add(0.1);
////		referencePoint.add(-0.25);
////		referencePoint.add(-0.25);
//		
//		double epsilon = 0.001;
//
//		LOGGER.info("Optimizing...");
//		
////		RNSGAIITHIAGO<? extends Solution<?>> algorithm = new RNSGAIITHIAGO<>(
////	    		problem, 
////	    		parameters.getMaxEvaluations(), 
////	    		parameters.getPopulationSize(), 
////	    		crossover, 
////	    		mutation
////		    );
////		
////		algorithm.setEpsilon(epsilon);
////		algorithm.setReferencePoint(referencePoint);
//		
//		Algorithm algorithm = new RNSGAIIBuilder(problem, crossover, mutation, referencePoint, epsilon)
//				.setSelectionOperator(selection)
//				.setMaxEvaluations(parameters.getMaxEvaluations())
//				.setPopulationSize(parameters.getPopulationSize())
//				.build();
//
//		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
//
//		List population = (List) algorithm.getResult();
//		
//		LOGGER.info("Removing repeated solutions...");
//		
//		//List<Solution<?>> noRepeatedSolutions = SolutionListUtils.removeRepeated(population);
//		
//		List<thiagodnf.nautilus.core.model.Solution> solutions = Converter.toSolutions(population);
//		
//		for(AbstractObjective objective : objectives) {
//			parameters.getObjectiveKeys().add(objective.getKey());
//		}
//		
//		LOGGER.info("Exporting the output...");
//		
//		Execution execution = new Execution();
//		
//		execution.setId("75BIksd100VARIAVEIS");
//		execution.setParameters(parameters);
//		execution.setSolutions(solutions);
//		execution.setExecutionTime(algorithmRunner.getComputingTime());
//		execution.getSettings().setNormalize(new ByMaxAndMinValuesNormalize().getKey());
//		
//		FileUtils.writeStringToFile(new File("output.json"), execution.toString());
//		
//		LOGGER.info("Done");
	}

}
