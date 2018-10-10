package thiagodnf.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public interface ProblemExtension extends ExtensionPoint {

	public Problem<?> createProblem(InstanceData instanceData, List<AbstractObjective> objectives);
	
	public InstanceData readInstanceData(Path path);
	
	public String getName();
	
	public String getId();
}
