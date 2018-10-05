package thiagodnf.nautilus.web.service;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.adapter.OperatorAdapter;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.zdt1.ZDT1Plugin;
import thiagodnf.nautilus.plugin.zdt3.ZDT3Plugin;

@Service
public class PluginService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginService.class);
	
	@Autowired
	private FileService fileService;

	private Map<String, AbstractPlugin> plugins = new HashMap<>();
	
	private final PluginManager pluginManager = new DefaultPluginManager(); 
	
	@PostConstruct
	private void initIt() {
		
		reload();
		
		LOGGER.info("Loading plugins from project");
		
		//add(new MIPPlugin());
		add(new ZDT1Plugin());
		add(new ZDT3Plugin());
		
		
        
        
        
        
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
	
	public void reload() {
		
		LOGGER.info("Reloading the plugins ");
		
		pluginManager.stopPlugins();
		
		List<String> files = fileService.getJarPlugins();

		LOGGER.info("Found {} .jar files", files.size());

		for (String file : files) {
			pluginManager.loadPlugin(Paths.get(file));
		}

		LOGGER.info("Loaded {} plugins. Starting all of them", pluginManager.getPlugins().size());

		pluginManager.startPlugins();
		
		List<ProblemExtension> greetings = pluginManager.getExtensions(ProblemExtension.class);
		
		
        System.out.println(String.format("Found %d extensions for extension point '%s'", greetings.size(), ProblemExtension.class.getName()));
        for (ProblemExtension greeting : greetings) {
        	System.out.println(greeting);
            //System.out.println(">>> " + greeting.getGreeting());
        }
        
        List<PluginWrapper> startedPlugins = pluginManager.getStartedPlugins();
		 
		 System.out.println("Extensions added by classpath:");
	        Set<String> extensionClassNames = pluginManager.getExtensionClassNames(null);
	        for (String extension : extensionClassNames) {
	            System.out.println("   " + extension);
	        }
		 
		 for (PluginWrapper plugin : startedPlugins) {
	            String pluginId = plugin.getDescriptor().getPluginId();
	            System.out.println(String.format("Extensions added by plugin '%s':", pluginId));
	            extensionClassNames = pluginManager.getExtensionClassNames(pluginId);
	            for (String extension : extensionClassNames) {
	                System.out.println("   " + extension);
	            }
	        }
		 
		for (PluginWrapper plugin : startedPlugins) {
           String pluginId = plugin.getDescriptor().getPluginId();

           System.out.println(plugin);
           
           List<ProblemExtension> extensions = pluginManager.getExtensions(ProblemExtension.class, pluginId);
           
           
           System.out.println(extensions);
		}
		
		  // print the extensions instances for Greeting extension point for each started plugin
		System.out.println(" oi");
        for (PluginWrapper plugin : startedPlugins) {
            String pluginId = plugin.getDescriptor().getPluginId();
            System.out.println(String.format("Extensions instances added by plugin '%s' for extension point '%s':", pluginId, ProblemExtension.class.getName()));
            List<ProblemExtension> extensions = pluginManager.getExtensions(ProblemExtension.class, pluginId);
            for (Object extension : extensions) {
                System.out.println("   " + extension);
            }
        }
		
		
		
		
		
		
		pluginManager.stopPlugins();
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
