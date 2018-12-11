package thiagodnf.nautilus.plugin.toy.extension;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.toy.problem.ToyProblem;

@Extension
public class ToyProblemExtension implements ProblemExtension {

	@Override
	public Problem<?> createProblem(InstanceData data, List<AbstractObjective> objectives) {
		return new ToyProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "Toy Problem";
	}
}
