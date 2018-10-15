package thiagodnf.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.pf4j.ExtensionPoint;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.model.Variable;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.problem.AbstractProblem;

public interface ProblemExtension extends ExtensionPoint {

	public AbstractProblem<?> createProblem(InstanceData instanceData, List<AbstractObjective> objectives);
	
	public InstanceData readInstanceData(Path path);
	
	public List<Variable> getVariables(InstanceData data);
	
	public String getName();
	
	public String getId();
}
