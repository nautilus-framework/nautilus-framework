package thiagodnf.nautilus.web.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import thiagodnf.nautilus.core.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.core.colorize.BySimilarityColorize;
import thiagodnf.nautilus.core.colorize.Colorize;
import thiagodnf.nautilus.core.colorize.NoColorColorize;
import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.PearsonCorrelation;
import thiagodnf.nautilus.core.correlation.SpearmanCorrelation;
import thiagodnf.nautilus.core.duplicated.ByObjectivesDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.ByVariablesDuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.DuplicatesRemover;
import thiagodnf.nautilus.core.duplicated.NoDuplicatesRemover;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.operator.crossover.Crossover;
import thiagodnf.nautilus.core.operator.mutation.Mutation;
import thiagodnf.nautilus.core.operator.selection.Selection;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.FormatterExtension;
import thiagodnf.nautilus.plugin.extension.InstanceDataExtension;
import thiagodnf.nautilus.plugin.extension.MutationExtension;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.extension.OperatorExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.SelectionExtension;
import thiagodnf.nautilus.plugin.factory.AlgorithmFactory;
import thiagodnf.nautilus.plugin.factory.CrossoverFactory;
import thiagodnf.nautilus.plugin.factory.MutationFactory;
import thiagodnf.nautilus.plugin.factory.SelectionFactory;
import thiagodnf.nautilus.web.exception.PluginNotFoundException;
import thiagodnf.nautilus.web.exception.ProblemNotFoundException;

@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private final PluginManager pluginManager = new DefaultPluginManager(); 
	
	private Map<String, Normalize> normalizers = new HashMap<>();
	
	private Map<String, Colorize> colorizers = new HashMap<>();
	
	private Map<String, DuplicatesRemover> duplicatesRemovers = new HashMap<>();
	
	private Map<String, Correlation> correlationers = new HashMap<>();
	
	@PostConstruct
	private void initIt() {

		loadPluginsFromDirectory();

		LOGGER.info("Done. Adding Colorizers");

		addColorizer(new NoColorColorize());
		addColorizer(new ByEuclideanDistanceColorize());
		addColorizer(new BySimilarityColorize());

		LOGGER.info("Done. Adding Normalizers");

		addNormalizer(new ByMaxAndMinValuesNormalize());
		addNormalizer(new ByParetoFrontValuesNormalize());
		
		LOGGER.info("Done. Adding Correlationers");

		addCorrelationer(new SpearmanCorrelation());
		addCorrelationer(new PearsonCorrelation());

		LOGGER.info("Done. Adding Duplicate Removers");
		
		addDuplicatesRemover(new ByVariablesDuplicatesRemover());
		addDuplicatesRemover(new ByObjectivesDuplicatesRemover());
		addDuplicatesRemover(new NoDuplicatesRemover());
		
		LOGGER.info("Done");
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

				String problemId = Converter.toKey(extension.getName());
				
				LOGGER.info("Creating folder for {}/{}", plugin.getPluginId(), problemId);

				fileService.createPluginDirectory(plugin.getPluginId(), problemId);
			}
		}
		
		LOGGER.info("Done. All plugins were loaded and started");
	}
	
	private void addColorizer(Colorize colorize) {

		this.colorizers.put(colorize.getKey(), colorize);
		
		LOGGER.info("Added '{}' colorizer", colorize.getKey());
	}

	private void addNormalizer(Normalize normalize) {

		this.normalizers.put(normalize.getKey(), normalize);
		
		LOGGER.info("Added '{}' normalizer", normalize.getKey());
	}
	
	private void addCorrelationer(Correlation correlation) {

		this.correlationers.put(correlation.getKey(), correlation);
		
		LOGGER.info("Added '{}' correlationer", correlation.getKey());
	}
	
	private void addDuplicatesRemover(DuplicatesRemover duplicatesRemover) {

		this.duplicatesRemovers.put(duplicatesRemover.getId(), duplicatesRemover);

		LOGGER.info("Added '{}' normalizer", duplicatesRemover.getId());
	}
	
	public Map<String, Normalize> getNormalizers() {
		return normalizers;
	}
	
	public Map<String, DuplicatesRemover> getDuplicatesRemovers() {
		return duplicatesRemovers;
	}

	public Map<String, Colorize> getColorizers() {
		return colorizers;
	}
	
	public Map<String, Correlation> getCorrelationers() {
		return correlationers;
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
	
//	public List<QualityIndicatorExtension> getQualityIndicatorExtensions(String pluginId) {
//		return pluginManager.getExtensions(QualityIndicatorExtension.class, pluginId);
//	}
	
	public InstanceDataExtension getInstanceDataExtension(String pluginId) {
		return getInstanceDataExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The instance data extension was not found"));
	}
	
	public ProblemExtension getProblemExtension(String pluginId, String problemId) {
		return getProblemExtensions(pluginId)
				.stream()
				.filter(p -> Converter.toKey(p.getName()).equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(ProblemNotFoundException::new);
	}
	
	public OperatorExtension getOperatorExtension(String pluginId) {
		return getOperatorExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The operator extension was not found"));
	}
	
	public ObjectiveExtension getObjectiveExtension(String pluginId, String problemId) {
		return pluginManager.getExtensions(ObjectiveExtension.class, pluginId)
				.stream()
				.filter(e -> e.getProblemId().equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Objective Extension was not found"));
	}

	public List<OperatorExtension> getOperatorExtensions(String pluginId) {
		return pluginManager.getExtensions(OperatorExtension.class, pluginId);
	}
	
	public List<FormatterExtension> getFormatters(String pluginId) {
		return pluginManager.getExtensions(FormatterExtension.class, pluginId);
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
	
//	public String formatInstanceFile(String pluginId, String problemId, String content) {
//
//		List<FormatterExtension> formatters = getFormatters(pluginId);
//
//		if (formatters.isEmpty()) {
//			return "The formatter was not defined";
//		}
//
//		return formatters.get(0).formatInstanceFile(problemId, content);
//	}
	
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
	
//	public QualityIndicatorFactory getQualityIndicatorFactory(String pluginId) {
//
//		QualityIndicatorFactory factory = new QualityIndicatorFactory();
//
//		for (QualityIndicatorExtension extension : getQualityIndicatorExtensions(pluginId)) {
//			factory.addExtension(extension);
//		}
//
//		return factory;
//	}
	
	public Crossover<?> getCrossoversById(String pluginId, String problemId, String crossoverId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getCrossoverOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(crossoverId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The crossoverId was not found"));			
	}

//	public List<Crossover<?>> getCrossovers(String pluginId, String problemId) {
//		
//		OperatorExtension extension = getOperatorExtension(pluginId);
//		
//		if(extension == null) {
//			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
//		}
//		
//		return extension.getCrossoverOperators(problemId);
//	}
	
	public Selection<?> getSelectionsById(String pluginId, String problemId, String selectionId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getSelectionOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(selectionId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The selectionId was not found"));			
	}
	
//	public List<Selection<?>> getSelections(String pluginId, String problemId) {
//		
//		OperatorExtension extension = getOperatorExtension(pluginId);
//		
//		if(extension == null) {
//			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
//		}
//		
//		return extension.getSelectionOperators(problemId);
//	}
	
	public Mutation<?> getMutationsById(String pluginId, String problemId, String mutationId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getMutationOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(mutationId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The mutationId was not found"));			
	}
	
//	public List<Mutation<?>> getMutations(String pluginId, String problemId) {
//		
//		OperatorExtension extension = getOperatorExtension(pluginId);
//		
//		if(extension == null) {
//			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
//		}
//		
//		return extension.getMutationOperators(problemId);
//	}

	public void deletePlugin(String pluginId) {
		this.pluginManager.deletePlugin(pluginId);
	}
}
