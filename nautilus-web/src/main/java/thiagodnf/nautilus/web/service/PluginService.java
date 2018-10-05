package thiagodnf.nautilus.web.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.adapter.OperatorAdapter;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.zdt1.ZDT1Plugin;
import thiagodnf.nautilus.plugin.zdt3.ZDT3Plugin;

@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private Map<String, AbstractPlugin> plugins = new HashMap<>();
	
	@PostConstruct
	private void initIt() {
		
		LOGGER.info("Loading plugins from project");
		
		//add(new MIPPlugin());
		add(new ZDT1Plugin());
		add(new ZDT3Plugin());
		
		
		LOGGER.info("Done. Loading plugins from .jar file");
		
		List<Path> paths = fileService.getPlugins();

//		try {
//			for (Path path : paths) {
//
//				URL[] urls = new URL[] { path.toFile().toURI().toURL() };
//
//				Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(urls));
//
//				for (Class<?> cls : reflections.getTypesAnnotatedWith(PluginBinding.class)) {
//					
//					add((AbstractPlugin) cls.newInstance());
//				}
//			}
//		} catch (Exception ex) {
//			throw new RuntimeException(ex);
//		}
		
		LOGGER.info("Done");
	}
	
	public List<AbstractPlugin> getAllPlugins() {

		List<AbstractPlugin> all = plugins.values()
				.stream()
				.collect(Collectors.toList());

		all.sort(Comparator.comparing(AbstractPlugin::getProblemName));

		return all;
	}
	
	public void add(AbstractPlugin plugin) {
		
		LOGGER.info("Adding plugin: " + plugin.getProblemKey());
		
		if (plugins.containsKey(plugin.getProblemKey())) {
			throw new RuntimeException("Plugin " + plugin.getProblemKey() + " already added");
		}
		
		plugins.put(plugin.getProblemKey(), plugin);
		
		plugin.initIt();
		
		fileService.createInstancesDirectory(plugin.getProblemKey());
	}
	
	public AbstractPlugin getPlugin(String problemKey) {

		if (!plugins.containsKey(problemKey)) {
			throw new RuntimeException("The problem key was not found");
		}

		return plugins.get(problemKey);
	}
	
	public ObjectiveAdapter getObjectiveAdapter(String problemKey) {

		ObjectiveAdapter adapter = getPlugin(problemKey).getObjectiveAdapter();

		if (adapter == null) {
			throw new RuntimeException("The objective adapter should not be null");
		}

		if (adapter.isEmpty()) {
			throw new RuntimeException("There are no objectives defined. Please contact the developer");
		}

		return adapter;
	}
	
	public OperatorAdapter getOperatorAdapter(String problemKey) {

		OperatorAdapter adapter = getPlugin(problemKey).getOperatorAdapter();

		if (adapter == null) {
			throw new RuntimeException("The operator adapter should not be null");
		}

		if (adapter.getCrossovers().isEmpty()) {
			throw new RuntimeException("There is no crossover operator defined. Please contact the developer");
		}

		if (adapter.getMutations().isEmpty()) {
			throw new RuntimeException("There is no mutation operator defined. Please contact the developer");
		}
		
		if (adapter.getSelections().isEmpty()) {
			throw new RuntimeException("There is no selection operator defined. Please contact the developer");
		}

		return adapter;
	}
	
	public List<AbstractObjective> getObjectives(String problemKey, List<String> objectiveKeys){
		
		Map<String, List<AbstractObjective>> objectiveMap = getObjectiveAdapter(problemKey).getGroups();

		List<AbstractObjective> objectives = new ArrayList<>();

		for(String objectiveKey : objectiveKeys) {
		
			for (Entry<String, List<AbstractObjective>> entry : objectiveMap.entrySet()) {
				
				for (AbstractObjective objective : entry.getValue()) {
					
					if(objective.getKey().equalsIgnoreCase(objectiveKey)) {
						objectives.add(objective);
					}
				}
			}
		}

		return objectives;
	}
	
	public void store(String filename, MultipartFile file) {
		fileService.storePlugin(filename, file);
	}
}
