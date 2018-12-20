package thiagodnf.nautilus.plugin.zdt.extension.problem;

import java.util.List;

import org.pf4j.Extension;
import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.problem.Problem;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.plugin.zdt.encoding.problem.ZDT1Problem;

@Extension
public class ZDT1ProblemExtension implements ProblemExtension {

	@Override
	public Problem<?> getProblem(InstanceData data, List<AbstractObjective> objectives) {
		return new ZDT1Problem(data, objectives);
	}

	@Override
	public String getName() {
		return "ZDT1 Problem";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public Class<?> supports() {
		return DoubleProblem.class;
	}
}