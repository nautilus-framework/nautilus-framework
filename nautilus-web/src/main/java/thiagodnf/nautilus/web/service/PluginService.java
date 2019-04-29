package thiagodnf.nautilus.web.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Ordering;

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
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.reducer.AbstractReducer;
import thiagodnf.nautilus.core.reducer.ConfiabilityBasedReduction;
import thiagodnf.nautilus.core.reducer.ImplicitFeedbackObjectiveReducer;
import thiagodnf.nautilus.core.reducer.InfluenceOfVariableBasedReducer;
import thiagodnf.nautilus.core.reducer.DontReduceObjectivesReducer;
import thiagodnf.nautilus.core.reducer.KeepOriginalObjectivesReducer;
import thiagodnf.nautilus.core.reducer.RandomlyObjectivesReducer;
import thiagodnf.nautilus.core.reducer.VariableBasedReducer;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.IndicatorExtension;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;
import thiagodnf.nautilus.plugin.factory.AlgorithmFactory;
import thiagodnf.nautilus.plugin.factory.CrossoverFactory;
import thiagodnf.nautilus.plugin.factory.IndicatorFactory;
import thiagodnf.nautilus.plugin.factory.MutationFactory;
import thiagodnf.nautilus.plugin.factory.SelectionFactory;
import thiagodnf.nautilus.web.exception.InstanceDataNotFoundException;
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
	
	private Map<String, AbstractReducer> reducers = new TreeMap<>();
	
	@PostConstruct
	private void initIt() {

		loadPluginsFromDirectory();

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
		
		LOGGER.info("Done. Adding Reducers");
		
		addReducer(new ImplicitFeedbackObjectiveReducer());
		addReducer(new DontReduceObjectivesReducer());
		addReducer(new KeepOriginalObjectivesReducer());
		addReducer(new VariableBasedReducer());
		addReducer(new RandomlyObjectivesReducer());
		addReducer(new InfluenceOfVariableBasedReducer());
		addReducer(new ConfiabilityBasedReduction());
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
			}
		}
		
		LOGGER.info("Done. All plugins were loaded and started");
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
	
	private void addReducer(AbstractReducer reducer) {

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
	
	public Map<String, AbstractReducer> getReducers() {
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
	
	public List<ObjectiveExtension> getObjectiveExtensions(String pluginId) {
		return pluginManager.getExtensions(ObjectiveExtension.class, pluginId);
	}
	
	public List<InstanceDataExtension> getInstanceDataExtensions(String pluginId) {
		return pluginManager.getExtensions(InstanceDataExtension.class, pluginId);
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
	
	public InstanceDataExtension getInstanceDataExtension(String pluginId, String problemId) {
		return getInstanceDataExtensions(pluginId)
				.stream()
				.filter(p -> p.getProblemIds().contains(problemId))
				.findFirst()
				.orElseThrow(InstanceDataNotFoundException::new);
	}
	
	public ProblemExtension getProblemExtension(String pluginId, String problemId) {
		return getProblemExtensions(pluginId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(ProblemNotFoundException::new);
	}
	
	public ObjectiveExtension getObjectiveExtension(String pluginId, String problemId) {
		return getObjectiveExtensions(pluginId)
				.stream()
				.filter(e -> e.getProblemId().equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Objective Extension was not found"));
	}
	
	public Map<String, List<AbstractObjective>> getObjectivesByGroups(String pluginId, String problemId) {

		Map<String, List<AbstractObjective>> map = new HashMap<>();
		
		ObjectiveExtension extension = getObjectiveExtension(pluginId, problemId);

		for (AbstractObjective objective : extension.getObjectives()) {

			if (!map.containsKey(objective.getGroupName())) {
				map.put(objective.getGroupName(), new ArrayList<>());
			}

			map.get(objective.getGroupName()).add(objective);
		}

		return map;
	}
	
	public List<AbstractObjective> getObjectivesByIds(String pluginId, String problemId, List<String> objectiveIds){
		
		ObjectiveExtension extension = getObjectiveExtension(pluginId, problemId);

		return extension.getObjectives()
				.stream()
				.filter(o -> objectiveIds.contains(o.getId()))
				.collect(Collectors.toList());
	}
	
	public AlgorithmFactory getAlgorithmFactory(String pluginId) {

		AlgorithmFactory factory = new AlgorithmFactory();

		for (AlgorithmExtension extension : getAlgorithmExtensions(pluginId)) {
			factory.getExtensions().add(extension);
		}

		Collections.sort(factory.getExtensions(), Ordering.usingToString());

		return factory;
	}
	
	public SelectionFactory getSelectionFactory(String pluginId) {

		SelectionFactory factory = new SelectionFactory();

		for (SelectionExtension extension : getSelectionExtensions(pluginId)) {
			factory.getExtensions().add(extension);
		}

		Collections.sort(factory.getExtensions(), Ordering.usingToString());

		return factory;
	}
	
	public CrossoverFactory getCrossoverFactory(String pluginId) {

		CrossoverFactory factory = new CrossoverFactory();

		for (CrossoverExtension extension : getCrossoverExtensions(pluginId)) {
			factory.getExtensions().add(extension);
		}

		Collections.sort(factory.getExtensions(), Ordering.usingToString());

		return factory;
	}
	
	public MutationFactory getMutationFactory(String pluginId) {

		MutationFactory factory = new MutationFactory();

		for (MutationExtension extension : getMutationExtensions(pluginId)) {
			factory.getExtensions().add(extension);
		}

		Collections.sort(factory.getExtensions(), Ordering.usingToString());

		return factory;
	}
	
	public IndicatorFactory getIndicatorFactory(String pluginId) {

		IndicatorFactory factory = new IndicatorFactory();

		for (IndicatorExtension extension : getIndicatorExtensions(pluginId)) {
			factory.add(extension);
		}

		Collections.sort(factory.getExtensions(), Ordering.usingToString());

		return factory;
	}

	public void deletePlugin(String pluginId) {
		this.pluginManager.deletePlugin(pluginId);
	}
}
