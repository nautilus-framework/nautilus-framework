package thiagodnf.nautilus.plugin.toy.extension.problem;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.IntegerProblem;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.toy.encoding.problem.ToyProblem;

@Extension
public class ToyProblemExtension implements ProblemExtension {

	@Override
	public Problem<?> getProblem(InstanceData data, List<AbstractObjective> objectives) {
		return new ToyProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "Toy Problem";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public Class<?> supports() {
		return IntegerProblem.class;
	}
}
