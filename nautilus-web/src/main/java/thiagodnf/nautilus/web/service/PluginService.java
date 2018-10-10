package thiagodnf.nautilus.web.service;

import java.nio.file.Paths;
import java.util.ArrayList;
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
import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.core.colorize.ByEuclideanDistanceColorize;
import thiagodnf.nautilus.core.colorize.BySimilarityColorize;
import thiagodnf.nautilus.core.colorize.Colorize;
import thiagodnf.nautilus.core.colorize.NoColorColorize;
import thiagodnf.nautilus.core.correlation.Correlation;
import thiagodnf.nautilus.core.correlation.SpearmanCorrelation;
import thiagodnf.nautilus.core.normalize.ByMaxAndMinValuesNormalize;
import thiagodnf.nautilus.core.normalize.ByParetoFrontValuesNormalize;
import thiagodnf.nautilus.core.normalize.Normalize;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.operator.crossover.Crossover;
import thiagodnf.nautilus.core.operator.mutation.Mutation;
import thiagodnf.nautilus.core.operator.selection.Selection;
import thiagodnf.nautilus.plugin.extension.FormatterExtension;
import thiagodnf.nautilus.plugin.extension.GUIExtension;
import thiagodnf.nautilus.plugin.extension.ObjectiveExtension;
import thiagodnf.nautilus.plugin.extension.OperatorExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.extension.VariableExtension;
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
	
	private Map<String, Correlation> correlationers = new HashMap<>();
	
	@PostConstruct
	private void initIt() {

		LOGGER.info("Loading plugins");

		reload();

		LOGGER.info("Done. Adding Colorizers");

		addColorizer(new NoColorColorize());
		addColorizer(new ByEuclideanDistanceColorize());
		addColorizer(new BySimilarityColorize());

		LOGGER.info("Done. Adding Normalizers");

		addNormalizer(new ByMaxAndMinValuesNormalize());
		addNormalizer(new ByParetoFrontValuesNormalize());
		
		LOGGER.info("Done. Adding Correlationers");

		addCorrelationer(new SpearmanCorrelation());

		LOGGER.info("Done");
	}
	
	public void reload() {
		
		LOGGER.info("Stopping the plugins before load all of them");

		pluginManager.stopPlugins();

		LOGGER.info("Done. Loading plugins from directory");
		
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
		
		LOGGER.info("Done.");
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
	
	public Map<String, Normalize> getNormalizers() {
		return normalizers;
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
	
	public List<GUIExtension> getGUIExtensions(String pluginId) {
		return pluginManager.getExtensions(GUIExtension.class, pluginId);
	}
	
	public List<VariableExtension> getVariableExtensions(String pluginId) {
		return pluginManager.getExtensions(VariableExtension.class, pluginId);
	}
	
	public List<ProblemExtension> getProblemExtensions(String pluginId) {
		return pluginManager.getExtensions(ProblemExtension.class, pluginId);
	}
	
	public GUIExtension getGUIExtension(String pluginId) {
		return getGUIExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The gui extension was not found"));
	}
	
	public ProblemExtension getProblemExtension(String pluginId, String problemId) {
		return getProblemExtensions(pluginId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(problemId))
				.findFirst()
				.orElseThrow(ProblemNotFoundException::new);
	}
	
	public VariableExtension getVariableExtension(String pluginId) {
		return getVariableExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The variable extension was not found"));
	}
	
	public ObjectiveExtension getObjectiveExtension(String pluginId) {
		return getObjectiveExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The objective was not found"));
	}
	
	public OperatorExtension getOperatorExtension(String pluginId) {
		return getOperatorExtensions(pluginId)
				.stream()
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The operator extension was not found"));
	}
	
	public List<ObjectiveExtension> getObjectiveExtensions(String pluginId) {
		return pluginManager.getExtensions(ObjectiveExtension.class, pluginId);
	}

	public List<OperatorExtension> getOperatorExtensions(String pluginId) {
		return pluginManager.getExtensions(OperatorExtension.class, pluginId);
	}
	
	public List<FormatterExtension> getFormatters(String pluginId) {
		return pluginManager.getExtensions(FormatterExtension.class, pluginId);
	}
	
	public Map<String, List<AbstractObjective>> getObjectivesByGroups(String pluginId, String problemId) {

		Map<String, List<AbstractObjective>> map = new HashMap<>();

		ObjectiveExtension extension = getObjectiveExtension(pluginId);
		
		if (extension == null) {
			throw new RuntimeException("The problemId was not found");
		}

		for (AbstractObjective objective : extension.getObjectives(problemId)) {

			if (!map.containsKey(objective.getGroupName())) {
				map.put(objective.getGroupName(), new ArrayList<>());
			}

			map.get(objective.getGroupName()).add(objective);
		}

		return map;
	}
	
	
	
	public String formatInstanceFile(String pluginId, String problemId, String content) {

		List<FormatterExtension> formatters = getFormatters(pluginId);

		if (formatters.isEmpty()) {
			return "The formatter was not defined";
		}

		return formatters.get(0).formatInstanceFile(problemId, content);
	}
	
	public List<AbstractObjective> getObjectivesByIds(String pluginId, String problemId, List<String> objectiveIds){
		
		ObjectiveExtension extension = getObjectiveExtension(pluginId);

		if (extension == null) {
			throw new RuntimeException("The plugin id was not found");
		}

		return extension.getObjectives(problemId)
				.stream()
				.filter(o -> objectiveIds.contains(o.getId()))
				.collect(Collectors.toList());
	}
	
	public void store(String filename, MultipartFile file) {
		fileService.storePlugin(filename, file);
	}
	
	public Crossover<?> getCrossoversById(String pluginId, String problemId, String crossoverId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getCrossoverOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(crossoverId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The crossoverId was not found"));			
	}

	public List<Crossover<?>> getCrossovers(String pluginId, String problemId) {
		
		OperatorExtension extension = getOperatorExtension(pluginId);
		
		if(extension == null) {
			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
		}
		
		return extension.getCrossoverOperators(problemId);
	}
	
	public Selection<?> getSelectionsById(String pluginId, String problemId, String selectionId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getSelectionOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(selectionId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The selectionId was not found"));			
	}
	
	public List<Selection<?>> getSelections(String pluginId, String problemId) {
		
		OperatorExtension extension = getOperatorExtension(pluginId);
		
		if(extension == null) {
			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
		}
		
		return extension.getSelectionOperators(problemId);
	}
	
	public Mutation<?> getMutationsById(String pluginId, String problemId, String mutationId){
		
		OperatorExtension extension = getOperatorExtension(pluginId);

		return extension.getMutationOperators(problemId)
				.stream()
				.filter(p -> p.getId().equalsIgnoreCase(mutationId))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("The mutationId was not found"));			
	}
	
	public List<Mutation<?>> getMutations(String pluginId, String problemId) {
		
		OperatorExtension extension = getOperatorExtension(pluginId);
		
		if(extension == null) {
			throw new RuntimeException("There is no operator operator defined. Please contact the developer"); 
		}
		
		return extension.getMutationOperators(problemId);
	}

	public void stopAndUnload(String pluginId) {
		this.pluginManager.stopPlugin(pluginId);
		this.pluginManager.unloadPlugin(pluginId);
	}
	
}
