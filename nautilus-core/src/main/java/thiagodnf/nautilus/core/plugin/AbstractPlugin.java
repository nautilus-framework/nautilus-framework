package thiagodnf.nautilus.core.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public abstract class AbstractPlugin {
	
	public Map<String, List<AbstractObjective>> getObjectives() {
		return new HashMap<>();
	}
	
	public String getProblemKey() {
		return getProblemName().replaceAll("\\s+", "-").toLowerCase();
	}
	
	public List<String> getCrossoverNames(){
		return Arrays.asList();
	}
	
	public List<String> getMutationNames(){
		return Arrays.asList();
	}
	
	public double getJaccardDistance(List<String> variables1, List<String> variables2) {
		return 0.0;
	}
	
	public abstract Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException;
	
	public abstract String getProblemName();
}
