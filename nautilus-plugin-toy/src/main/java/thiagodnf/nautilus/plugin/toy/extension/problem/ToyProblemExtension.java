package thiagodnf.nautilus.plugin.toy.extension.problem;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.extension.problem.AbstractProblemExtension;
import thiagodnf.nautilus.plugin.toy.encoding.problem.ToyProblem;

@Extension
public class ToyProblemExtension extends AbstractProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new ToyProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "Toy Problem";
	}

	@Override
	public Class<?> supports() {
		return IntegerProblem.class;
	}
}
