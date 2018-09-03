package thiagodnf.nautilus.web.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.AbstractPlugin;
import thiagodnf.nautilus.plugin.mip.MIPPlugin;
import thiagodnf.nautilus.plugin.objective.AbstractObjective;

@Service
public class PluginService {
	
	@Autowired
	private FileService fileService;

	private Map<String, AbstractPlugin> plugins = new HashMap<>();
	
	@PostConstruct
	private void initIt() {
		
		add(new MIPPlugin());
		
		for (AbstractPlugin plugin : plugins.values()) {
			fileService.createInstancesDirectory(plugin.getProblemKey());
		}
	}
	
	public void add(AbstractPlugin plugin) {
		plugins.put(plugin.getProblemKey(), plugin);
	}
	
	public AbstractPlugin getPlugin(String problemKey) {
		return plugins.get(problemKey);
	}
	
	public Problem<?> getProblem(String problemKey, Path instance, List<AbstractObjective> objectives) throws IOException {
		return getPlugin(problemKey).getProblem(instance, objectives);
	}
	
	public List<AbstractObjective> getObjectives(String problemKey){
		return getPlugin(problemKey).getObjectives();
	}
	
	public List<AbstractObjective> getObjectives(String problemKey, List<String> objectiveKeys){
		
		List<AbstractObjective> objectives = getObjectives(problemKey);
		
		objectives = objectives
				.stream()
	            .filter(x -> objectiveKeys.contains(x.getKey()))
	            .collect(Collectors.toList());
		
		return objectives;
	}
}
