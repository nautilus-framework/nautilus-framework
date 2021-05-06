package org.nautilus.web.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.util.Strings;
import org.nautilus.core.encoding.NProblem;
import org.nautilus.core.encoding.problem.NBinaryProblem;
import org.nautilus.core.encoding.solution.NBinarySolution;
import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.CorrelationExtension;
import org.nautilus.plugin.extension.CrossoverExtension;
import org.nautilus.plugin.extension.IndicatorExtension;
import org.nautilus.plugin.extension.MutationExtension;
import org.nautilus.plugin.extension.NormalizerExtension;
import org.nautilus.plugin.extension.ProblemExtension;
import org.nautilus.plugin.extension.RemoverExtension;
import org.nautilus.plugin.extension.SelectionExtension;
import org.nautilus.web.exception.PluginNotFoundException;
import org.nautilus.web.exception.ProblemNotFoundException;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import lombok.Getter;

@Getter
@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private final PluginManager pluginManager = new DefaultPluginManager();
	
	private Map<String, ProblemExtension> problems = new TreeMap<>();
	
	private Map<String, AlgorithmExtension> algorithms = new TreeMap<>();
	
	private Map<String, CrossoverExtension> crossovers = new TreeMap<>();
	
	private Map<String, MutationExtension> mutations = new TreeMap<>();
	
	private Map<String, SelectionExtension> selections = new TreeMap<>();
	
	private Map<String, NormalizerExtension> normalizers = new TreeMap<>();
	
	private Map<String, CorrelationExtension> correlations = new TreeMap<>();
	
	private Map<String, RemoverExtension> removers = new TreeMap<>();
    
	
	@PostConstruct
	private void initIt() {
		loadPluginsFromDirectory();
	}
	
	public void loadPluginsFromDirectory() {
		
		LOGGER.info("Loading plugins from directory");
		
		List<String> files = fileService.getPlugins();

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
	
	public void addProblemExtension(ProblemExtension problemExtension) {

	    String key = problemExtension.getId();
	    
		if (this.problems.containsKey(key)) {
			throw new RuntimeException("The problems w");
		}
		
		this.problems.put(key, problemExtension);
		
		this.fileService.createInstancesDirectory(key);
		
		LOGGER.info("Added '{}' problem extension", key);
		
		Path toolFolder = Paths.get(System.getProperty("user.dir"));
		
		Path parentFolder = toolFolder.getParent().getParent();
		
		Path directory = fileService.getInstancesLocation().resolve(key);

		LOGGER.info("Copying the instance files from '{}'", key);
		
		for (Path instance : problemExtension.getAllInstances()) {

            Path original = parentFolder.resolve(instance);
            Path copied = directory.resolve(original.getFileName());
            
            if (Files.exists(original) && !Files.exists(copied)) {

                LOGGER.info("> Copying {}", original);

                try {
                    Files.copy(original, copied, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    new RuntimeException(ex);
                }
            }
        }
	}
		
	public void addAlgorithmExtension(AlgorithmExtension algorithmExtension) {

		if (this.algorithms.containsKey(algorithmExtension.getId())) {
			throw new RuntimeException("The algorithm w");
		}

		this.algorithms.put(algorithmExtension.getId(), algorithmExtension);
		
		LOGGER.info("Added '{}' algorithm extension", algorithmExtension.getId());
	}
	
	public void addCrossoverExtension(CrossoverExtension crossoverExtension) {
	    
	    System.out.println(crossoverExtension);

		if (this.crossovers.containsKey(crossoverExtension.getId())) {
			throw new RuntimeException("The crossover w");
		}

		this.crossovers.put(crossoverExtension.getId(), crossoverExtension);
		
		LOGGER.info("Added '{}' crossover extension", crossoverExtension.getId());
	}
	
	public void addMutationExtension(MutationExtension mutationExtension) {

		if (this.mutations.containsKey(mutationExtension.getId())) {
			throw new RuntimeException("The mutation w");
		}

		this.mutations.put(mutationExtension.getId(), mutationExtension);

		LOGGER.info("Added '{}' mutation extension", mutationExtension.getId());
	}
	
	public void addSelectionExtension(SelectionExtension selectionExtension) {

		if (this.selections.containsKey(selectionExtension.getId())) {
			throw new RuntimeException("The selection w");
		}

		this.selections.put(selectionExtension.getId(), selectionExtension);

		LOGGER.info("Added '{}' selection extension", selectionExtension.getId());
	}
	
	public void addNormalizerExtension(NormalizerExtension normalizerExtension) {

        if (this.normalizers.containsKey(normalizerExtension.getId())) {
            throw new RuntimeException("The normalizer w");
        }

        this.normalizers.put(normalizerExtension.getId(), normalizerExtension);

        LOGGER.info("Added '{}' normalizer extension", normalizerExtension.getId());
    }
	
	public void addCorrelationExtension(CorrelationExtension correlationExtension) {

        if (this.correlations.containsKey(correlationExtension.getId())) {
            throw new RuntimeException("The correlation w");
        }

        this.correlations.put(correlationExtension.getId(), correlationExtension);

        LOGGER.info("Added '{}' correlation extension", correlationExtension.getId());
    }
    
	public void addRemoverExtension(RemoverExtension removerExtension) {

        if (this.removers.containsKey(removerExtension.getId())) {
            throw new RuntimeException("The remover w");
        }

        this.removers.put(removerExtension.getId(), removerExtension);

        LOGGER.info("Added '{}' remover extension", removerExtension.getId());
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
	
	public ProblemExtension getProblemById(String id) {
		
        if (Strings.isBlank(id) || !problems.containsKey(id)) {
            throw new ProblemNotFoundException();
        }

        return problems.get(id);
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
	
	public NormalizerExtension getNormalizerExtensionById(String id) {

        if (normalizers.containsKey(id)) {
            return normalizers.get(id);
        }

        throw new RuntimeException("The normalizer was not found");
    }
	
	public CorrelationExtension getCorrelationExtensionById(String id) {

        if (correlations.containsKey(id)) {
            return correlations.get(id);
        }

        throw new RuntimeException("The correlation was not found");
    }
	
	public RemoverExtension getRemoverExtensionById(String id) {

        if (removers.containsKey(id)) {
            return removers.get(id);
        }

        throw new RuntimeException("The removers was not found");
    }
	
	
	public Solution<?> getSolution(String problemId, String instanceId, List<String> variables){
	    
        Path path = fileService.getInstance(problemId, instanceId);

        ProblemExtension problemExtension = getProblemById(problemId);

        Instance instance = problemExtension.getInstance(path);

        List<AbstractObjective> objectives = problemExtension.getObjectives();

        NProblem<?> problem = (NProblem<?>) problemExtension.getProblem(instance, objectives);
	    
	    if (problem instanceof NBinaryProblem) {
            
            NBinaryProblem pro = (NBinaryProblem) problem;

            NBinarySolution sol = (NBinarySolution) pro.createSolution();

            BinarySet set = new BinarySet(sol.getTotalNumberOfBits());

            set.clear();

            for (String variable : variables) {
                set.set(Integer.valueOf(variable), true);
            }

            sol.setVariableValue(0, set);
            
            pro.evaluate(sol);
           
            return sol;
        }
	    
	    return null;
	}
	
}
