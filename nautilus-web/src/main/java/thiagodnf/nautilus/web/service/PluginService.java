package thiagodnf.nautilus.web.service;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thiagodnf.nautilus.core.colorize.AbstractColorize;
import thiagodnf.nautilus.core.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.core.colorize.BySimilarityWithHammingDistanceColorize;
import thiagodnf.nautilus.core.colorize.BySimilarityWithJaccardIndexColorize;
import thiagodnf.nautilus.core.colorize.DontColorize;
import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.correlation.DontCorrelation;
import thiagodnf.nautilus.core.correlation.KendallCorrelation;
import thiagodnf.nautilus.core.correlation.PearsonCorrelation;
import thiagodnf.nautilus.core.correlation.SpearmanCorrelation;
import thiagodnf.nautilus.core.duplicated.AbstractDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.ByObjectivesDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.ByVariablesOrderDoesNotMatterDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.ByVariablesOrderMattersDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.DontDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.AbstractNormalize;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.normalize.DontNormalize;
import thiagodnf.nautilus.core.reduction.AbstractReduction;
import thiagodnf.nautilus.core.reduction.ConfidenceBasedReduction;
import thiagodnf.nautilus.core.reduction.DontReduceObjectivesReduction;
import thiagodnf.nautilus.core.reduction.ImplicitFeedbackObjectiveReduction;
import thiagodnf.nautilus.core.reduction.RandomlyObjectivesReduction;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.BruteForceSearchAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.GAAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.NSGAIIAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.NSGAIIIAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.RNSGAIIAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.RandomSearchAlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.SPEA2AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.crossover.IntegerSBXCrossoverExtension;
import thiagodnf.nautilus.plugin.extension.crossover.SBXCrossoverExtension;
import thiagodnf.nautilus.plugin.extension.crossover.SinglePointCrossoverExtension;
import thiagodnf.nautilus.plugin.extension.mutation.BitFlipMutationExtension;
import thiagodnf.nautilus.plugin.extension.mutation.IntegerPolynomialMutationExtension;
import thiagodnf.nautilus.plugin.extension.mutation.PolynomialMutationExtension;
import thiagodnf.nautilus.plugin.extension.selection.BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension;
import thiagodnf.nautilus.plugin.toy.extension.problem.ToyProblemExtension;
import thiagodnf.nautilus.web.exception.PluginNotFoundException;
import thiagodnf.nautilus.web.exception.ProblemNotFoundException;

@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private final PluginManager pluginManager = new DefaultPluginManager(); 
	
	private Map<String, AbstractNormalize> normalizers = new TreeMap<>();
	
	private Map<String, AbstractColorize> colorizers = new TreeMap<>();
	
	private Map<String, AbstractDuplicatesRemover> duplicatesRemovers = new TreeMap<>();
	
	private Map<String, AbstractCorrelation> correlationers = new TreeMap<>();
	
	private Map<String, AbstractReduction> reducers = new TreeMap<>();
	
	private Map<String, ProblemExtension> problems = new TreeMap<>();
	
	private Map<String, AlgorithmExtension> algorithms = new TreeMap<>();
	
	private Map<String, CrossoverExtension> crossovers = new TreeMap<>();
	
	private Map<String, MutationExtension> mutations = new TreeMap<>();
	
	private Map<String, SelectionExtension> selections = new TreeMap<>();
	
	@PostConstruct
	private void initIt() {

		loadPluginsFromDirectory();
		loadPluginsFromClasspath();
		
		LOGGER.info("Done. Adding Colorizers");

		addColorizer(new DontColorize());
		addColorizer(new ByEuclideanDistanceColorize());
		addColorizer(new BySimilarityWithJaccardIndexColorize());
		addColorizer(new BySimilarityWithHammingDistanceColorize());

		LOGGER.info("Done. Adding Normalizers");

		addNormalizer(new DontNormalize());
		addNormalizer(new ByMaxAndMinValuesNormalize());
		addNormalizer(new ByParetoFrontValuesNormalize());
		
		LOGGER.info("Done. Adding Correlationers");

		addCorrelationer(new DontCorrelation());
		addCorrelationer(new SpearmanCorrelation());
		addCorrelationer(new PearsonCorrelation());
		addCorrelationer(new KendallCorrelation());
		
		LOGGER.info("Done. Adding Duplicate Removers");
		
		addDuplicatesRemover(new DontDuplicatesRemover());
		addDuplicatesRemover(new ByVariablesOrderDoesNotMatterDuplicatesRemover());
		addDuplicatesRemover(new ByVariablesOrderMattersDuplicatesRemover());
		addDuplicatesRemover(new ByObjectivesDuplicatesRemover());
		
		LOGGER.info("Done. Adding Reductions");
		
		addReducer(new ImplicitFeedbackObjectiveReduction());
		addReducer(new DontReduceObjectivesReduction());
		addReducer(new RandomlyObjectivesReduction());
		addReducer(new ConfidenceBasedReduction());

	}
	
	public void loadPluginsFromDirectory() {
		
		LOGGER.info("Loading plugins from directory");
		
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

				LOGGER.info("Creating folder for {}/{}", plugin.getPluginId(), extension.getId());

				fileService.createPluginDirectory(plugin.getPluginId(), extension.getId());
				
				addProblemExtension(extension);
			}
		}
		
		LOGGER.info("Done. All plugins were loaded and started");
	}
	
	public void loadPluginsFromClasspath() {
		
		LOGGER.info("Loading plugins from classpath");
			
		LOGGER.info("Loading problem extensions from classpath");
		
		addProblemExtension(new ToyProblemExtension());
		
		LOGGER.info("Done. Loading algorithms extensions from classpath");
		
		addAlgorithmExtension(new BruteForceSearchAlgorithmExtension());
		addAlgorithmExtension(new GAAlgorithmExtension());
		addAlgorithmExtension(new NSGAIIAlgorithmExtension());
		addAlgorithmExtension(new NSGAIIIAlgorithmExtension());
		addAlgorithmExtension(new RandomSearchAlgorithmExtension());
		addAlgorithmExtension(new RNSGAIIAlgorithmExtension());
		addAlgorithmExtension(new SPEA2AlgorithmExtension());
		
		LOGGER.info("Done. Loading crossover extensions from classpath");
		
		addCrossoverExtension(new IntegerSBXCrossoverExtension());
		addCrossoverExtension(new SBXCrossoverExtension());
		addCrossoverExtension(new SinglePointCrossoverExtension());
		
		LOGGER.info("Done. Loading mutation extensions from classpath");
		
		addMutationExtension(new BitFlipMutationExtension());
		addMutationExtension(new IntegerPolynomialMutationExtension());
		addMutationExtension(new PolynomialMutationExtension());
		
		LOGGER.info("Done. Loading selection extensions from classpath");
		
		addSelectionExtension(new BinaryTournamentWithRankingAndCrowdingDistanceSelectionExtension());
	}
	
	private void addProblemExtension(ProblemExtension problemExtension) {

		if (this.problems.containsKey(problemExtension.getId())) {
			throw new RuntimeException("The problems w");
		}
		
		this.problems.put(problemExtension.getId(), problemExtension);
		
		LOGGER.info("Added '{}' problem extension", problemExtension.getId());
	}
	
	private void addAlgorithmExtension(AlgorithmExtension algorithmExtension) {

		if (this.algorithms.containsKey(algorithmExtension.getId())) {
			throw new RuntimeException("The algorithm w");
		}

		this.algorithms.put(algorithmExtension.getId(), algorithmExtension);
		
		LOGGER.info("Added '{}' algorithm extension", algorithmExtension.getId());
	}
	
	private void addCrossoverExtension(CrossoverExtension crossoverExtension) {

		if (this.crossovers.containsKey(crossoverExtension.getId())) {
			throw new RuntimeException("The crossover w");
		}

		this.crossovers.put(crossoverExtension.getId(), crossoverExtension);
		
		LOGGER.info("Added '{}' crossover extension", crossoverExtension.getId());
	}
	
	private void addMutationExtension(MutationExtension mutationExtension) {

		if (this.mutations.containsKey(mutationExtension.getId())) {
			throw new RuntimeException("The mutation w");
		}

		this.mutations.put(mutationExtension.getId(), mutationExtension);

		LOGGER.info("Added '{}' mutation extension", mutationExtension.getId());
	}
	
	private void addSelectionExtension(SelectionExtension selectionExtension) {

		if (this.selections.containsKey(selectionExtension.getId())) {
			throw new RuntimeException("The selection w");
		}

		this.selections.put(selectionExtension.getId(), selectionExtension);

		LOGGER.info("Added '{}' selection extension", selectionExtension.getId());
	}
	
	
	private void addColorizer(AbstractColorize colorize) {

		this.colorizers.put(colorize.getId(), colorize);
		
		LOGGER.info("Added '{}' colorizer", colorize.getId());
	}

	private void addNormalizer(AbstractNormalize normalize) {

		this.normalizers.put(normalize.getId(), normalize);
		
		LOGGER.info("Added '{}' normalizer", normalize.getId());
	}
	
	private void addCorrelationer(AbstractCorrelation correlation) {

		this.correlationers.put(correlation.getId(), correlation);
		
		LOGGER.info("Added '{}' correlationer", correlation.getId());
	}
	
	private void addDuplicatesRemover(AbstractDuplicatesRemover duplicatesRemover) {

		this.duplicatesRemovers.put(duplicatesRemover.getId(), duplicatesRemover);

		LOGGER.info("Added '{}' normalizer", duplicatesRemover.getId());
	}
	
	private void addReducer(AbstractReduction reducer) {

		this.reducers.put(reducer.getId(), reducer);
		
		LOGGER.info("Added '{}' reducer", reducer.getId());
	}
	
	public Map<String, AbstractNormalize> getNormalizers() {
		return normalizers;
	}
	
	public Map<String, AbstractDuplicatesRemover> getDuplicatesRemovers() {
		return duplicatesRemovers;
	}

	public Map<String, AbstractColorize> getColorizers() {
		return colorizers;
	}
	
	public Map<String, AbstractCorrelation> getCorrelationers() {
		return correlationers;
	}
	
	public Map<String, AbstractReduction> getReducers() {
		return reducers;
	}
	
	public List<PluginWrapper> getStartedPlugins() {
		return pluginManager.getStartedPlugins();
	}
	
	public PluginWrapper getPluginWrapper(String pluginId) {

		PluginWrapper plugin = pluginManager.getPlugin(pluginId);

		if (plugin == null) {
			throw new PluginNotFoundException();
		}

		return plugin;
	}
	
	public List<ProblemExtension> getProblemExtensions(String pluginId) {
		return pluginManager.getExtensions(ProblemExtension.class, pluginId);
	}
	
	public List<AlgorithmExtension> getAlgorithmExtensions(String pluginId) {
		return pluginManager.getExtensions(AlgorithmExtension.class, pluginId);
	}
	
	public List<SelectionExtension> getSelectionExtensions(String pluginId) {
		return pluginManager.getExtensions(SelectionExtension.class, pluginId);
	}
	
	public List<CrossoverExtension> getCrossoverExtensions(String pluginId) {
		return pluginManager.getExtensions(CrossoverExtension.class, pluginId);
	}
	
	public List<MutationExtension> getMutationExtensions(String pluginId) {
		return pluginManager.getExtensions(MutationExtension.class, pluginId);
	}
	
	public List<IndicatorExtension> getIndicatorExtensions(String pluginId) {
		return pluginManager.getExtensions(IndicatorExtension.class, pluginId);
	}
	
	public ProblemExtension getProblemExtension(String pluginId, String problemId) {
		return getProblemExtensions(pluginId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(ProblemNotFoundException::new);
	}

	/**
	 * Delete a given plugin
	 * 
	 * @param pluginId
	 * @return the deleted plugin wrapper
	 */
	public PluginWrapper deletePlugin(String pluginId) {
		
		PluginWrapper plugin = getPluginWrapper(pluginId);
		
		this.pluginManager.deletePlugin(pluginId);
		
		return plugin;
	}

	public Map<String, AlgorithmExtension> getAlgorithms() {
		return algorithms;
	}
	
	public Map<String, ProblemExtension> getProblems() {
		return problems;
	}
	
	public Map<String, SelectionExtension> getSelections() {
		return selections;
	}
	
	public Map<String, CrossoverExtension> getCrossovers() {
		return crossovers;
	}
	
	public Map<String, MutationExtension> getMutations() {
		return mutations;
	}
	
	public ProblemExtension getProblemById(String id) {
		
		if(problems.containsKey(id)) {
			return problems.get(id);
		}
		
		throw new ProblemNotFoundException();
	}
	
	
	public AlgorithmExtension getAlgorithmExtensionById(String id) {

		if (algorithms.containsKey(id)) {
			return algorithms.get(id);
		}

		throw new RuntimeException("The algorithm was not found");
	}
	
	public SelectionExtension getSelectionExtensionById(String id) {

		if (selections.containsKey(id)) {
			return selections.get(id);
		}

		throw new RuntimeException("The selection was not found");
	}
	
	public CrossoverExtension getCrossoverExtensionById(String id) {

		if (crossovers.containsKey(id)) {
			return crossovers.get(id);
		}

		throw new RuntimeException("The crossover was not found");
	}
	
	public MutationExtension getMutationExtensionById(String id) {

		if (mutations.containsKey(id)) {
			return mutations.get(id);
		}

		throw new RuntimeException("The mutations was not found");
	}
	
}
