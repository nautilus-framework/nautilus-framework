package org.nautilus.plugin.extension;

import java.nio.file.Path;
import java.util.List;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.core.remover.AbstractRemover;
import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

public interface ProblemExtension extends ExtensionPoint {

	public Problem<?> getProblem(Instance instance, List<AbstractObjective> objectives);
	
	public Class<? extends Solution<?>> supports();
	
	public Instance getInstance(Path path);
	
	public List<AbstractObjective> getObjectives();
	
	public List<AbstractObjective> getObjectiveByIds(List<String> objectiveIds);
	
	public List<String> getVariablesAsList(Instance instance, Solution<?> solution);
	
	public AbstractRemover getRemover();
}
