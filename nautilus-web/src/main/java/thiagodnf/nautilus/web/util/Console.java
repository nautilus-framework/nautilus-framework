package thiagodnf.nautilus.web.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

public class Console {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);

	private final static PluginService pluginService = new PluginService(); 
	
	private final static FileService fileService = new FileService();
	
	private final static PluginManager pluginManager = new DefaultPluginManager(); 
	
	public static List<ProblemExtension> getProblemExtensions(String pluginId) {
		return pluginManager.getExtensions(ProblemExtension.class, pluginId);
	}
	
	public static List<PluginWrapper> getStartedPlugins() {
		return pluginManager.getStartedPlugins();
	}
	
	public static void loadingPlugins() {
		
		fileService.createDirectories(fileService.getPluginsLocation());
		
		List<String> files = fileService.getJarPlugins();

		LOGGER.info("Done. It was found {} .jar files. Loading all of them", files.size());

		for (String file : files) {
			pluginManager.loadPlugin(Paths.get(file));
		}

		LOGGER.info("Done. Starting the loaded plugins");

		pluginManager.startPlugins();
		
		LOGGER.info("Done. It was started {} plugins. Creating the folders", pluginManager.getStartedPlugins().size());

		for (PluginWrapper plugin : getStartedPlugins()) {

			for (ProblemExtension extension : getProblemExtensions(plugin.getPluginId())) {

				LOGGER.info("Creating folder for {} / {}", plugin.getPluginId(), extension.getId());

				fileService.createPluginDirectory(plugin.getPluginId(), extension.getId());
			}
		}
		
		LOGGER.info("Done. All plugins were loaded and started");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) throws IOException {

		LOGGER.info("Starting....");
		
		loadingPlugins();
		
		List<String> files = fileService.getJarPlugins();
		
		
		Parameters parameters = new Parameters();
		
		parameters.setPluginId("nautilus-plugin-spl");
		parameters.setProblemId("product-selection-problem");
		parameters.setPopulationSize(100);
		parameters.setMaxEvaluations(100000);
		parameters.setAlgorithmName("nsga-ii");
		
		String pluginId = parameters.getPluginId();
		String problemId = parameters.getProblemId();
		
		ProblemExtension problemExtension = pluginService.getProblemExtension(pluginId, problemId);
		
		Path instance = fileService.getInstanceFile(pluginId, problemId, parameters.getFilename());
		
		InstanceData data = problemExtension.readInstanceData(instance);
//		Builder builder = new Builder();
//		
//		
//		
//		builder.setPopulationSize(parameters.getPopulationSize());
//		builder.setMaxEvaluations(parameters.getMaxEvaluations());
//		builder.setProblem(problemExtension.createProblem(data, objectives));
//		builder.setInitialPopulation(null);
//    	
//		builder.setSelection(pluginService.getSelectionsById(pluginId, problemId, parameters.getSelectionName()));
//		
//		builder.setCrossover(pluginService.getCrossoversById(pluginId, problemId, parameters.getCrossoverName()));
//		builder.getCrossover().setDistributionIndex(parameters.getCrossoverDistribution());
//		builder.getCrossover().setProbability(parameters.getCrossoverProbability());
//
//		builder.setMutation(pluginService.getMutationsById(pluginId, problemId, parameters.getMutationName()));
//		builder.getMutation().setDistributionIndex(parameters.getMutationDistribution());
//		builder.getMutation().setProbability(parameters.getMutationProbability());
		
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
		LOGGER.info("Done");
	}

}
