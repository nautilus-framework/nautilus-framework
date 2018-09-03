package thiagodnf.nautilus.plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public abstract class AbstractPlugin {
	
	public List<AbstractObjective> getObjectives() {
		return Arrays.asList();
	}
	
	public abstract Problem<?> getProblem(Path instance, List<AbstractObjective> objectives) throws IOException;
	
	public abstract String getProblemKey();
	
	public abstract String getProblemName();
}
