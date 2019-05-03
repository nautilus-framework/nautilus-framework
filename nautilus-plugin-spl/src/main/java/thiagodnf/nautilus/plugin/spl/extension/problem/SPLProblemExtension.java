package thiagodnf.nautilus.plugin.spl.extension.problem;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.spl.encoding.problem.SPLProblem;

@Extension
public class SPLProblemExtension implements ProblemExtension {

	@Override
	public Problem<?> getProblem(Instance data, List<AbstractObjective> objectives) {
		return new SPLProblem(data, objectives);
	}

	@Override
	public String getName() {
		return "SPL Problem";
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
