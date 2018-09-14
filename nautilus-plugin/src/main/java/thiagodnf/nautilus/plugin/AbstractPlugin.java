package thiagodnf.nautilus.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public abstract class AbstractPlugin {
	
	public Map<String, List<AbstractObjective>> getObjectives() {
		return new HashMap<>();
	}
	
	public abstract Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException;
	
	public String getProblemKey() {
		return getProblemName().replaceAll("\\s+", "-").toLowerCase();
	}
	
	public abstract String getProblemName();
	
	public List<String> getCrossoverNames(){
		return Arrays.asList();
	}
	
	public List<String> getMutationNames(){
		return Arrays.asList();
	}
}
