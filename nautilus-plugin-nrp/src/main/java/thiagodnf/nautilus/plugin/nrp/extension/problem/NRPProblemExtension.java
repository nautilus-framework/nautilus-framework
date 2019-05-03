package thiagodnf.nautilus.plugin.nrp.extension.problem;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.nrp.encoding.problem.NRPProblem;

@Extension
public class NRPProblemExtension implements ProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new NRPProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "NRP Problem";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public Class<?> supports() {
		return BinaryProblem.class;
	}
}
