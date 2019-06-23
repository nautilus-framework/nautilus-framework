package thiagodnf.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.pf4j.ExtensionPoint;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.remover.AbstractRemover;

public interface ProblemExtension extends ExtensionPoint {

	public Problem<?> getProblem(Instance instance, List<AbstractObjective> objectives);
	
	public Class<? extends Solution<?>> supports();
	
	public Instance getInstance(Path path);
	
	public String getName();
	
	public String getId();
	
	public List<AbstractObjective> getObjectives();
	
	public List<AbstractObjective> getObjectiveByIds(List<String> objectiveIds);
	
	public List<String> getVariablesAsList(Instance instance, Solution<?> solution);
	
	public AbstractRemover getRemover();
}
