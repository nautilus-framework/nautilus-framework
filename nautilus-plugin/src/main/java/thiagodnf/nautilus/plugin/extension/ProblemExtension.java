package thiagodnf.nautilus.plugin.extension;

import java.util.List;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public interface ProblemExtension extends ExtensionPoint {

	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives);
	
	public Class<?> supports();
	
	public String getName();
	
	public String getId();
}
