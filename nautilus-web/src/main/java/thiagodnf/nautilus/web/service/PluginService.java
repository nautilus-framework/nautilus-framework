package thiagodnf.nautilus.web.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.adapter.OperatorAdapter;
import thiagodnf.nautilus.core.adapter.ObjectiveAdapter;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.mip.MIPPlugin;
import thiagodnf.nautilus.plugin.zdt1.ZDT1Plugin;
import thiagodnf.nautilus.plugin.zdt3.ZDT3Plugin;

@Service
public class PluginService {
	
	@Autowired
	private FileService fileService;

	private Map<String, AbstractPlugin> plugins = new HashMap<>();
	
	@PostConstruct
	private void initIt() {
		
		add(new MIPPlugin());
		add(new ZDT3Plugin());
		add(new ZDT1Plugin());
	}
	
	public List<AbstractPlugin> getAllPlugins() {

		List<AbstractPlugin> all = plugins.values().stream().collect(Collectors.toList());

		all.sort(Comparator.comparing(AbstractPlugin::getProblemName));

		return all;
	}
	
	public void add(AbstractPlugin plugin) {
		
		plugins.put(plugin.getProblemKey(), plugin);
		
		
		plugin.initIt();
		
		// Creating directory
		
		fileService.createInstancesDirectory(plugin.getProblemKey());
	}
	
	public AbstractPlugin getPlugin(String problemKey) {

		if (!plugins.containsKey(problemKey)) {
			throw new RuntimeException("The problem key was not found");
		}

		return plugins.get(problemKey);
	}
	
	public Problem<?> getProblem(String problemKey, Path instance, List<AbstractObjective> objectives) throws IOException {
		return getPlugin(problemKey).getProblem(instance, objectives);
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

		if (adapter.getCrossoverNames().isEmpty()) {
			throw new RuntimeException("There is no crossover defined. Please contact the developer");
		}

		if (adapter.getMutationNames().isEmpty()) {
			throw new RuntimeException("There is no mutation defined. Please contact the developer");
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
}
