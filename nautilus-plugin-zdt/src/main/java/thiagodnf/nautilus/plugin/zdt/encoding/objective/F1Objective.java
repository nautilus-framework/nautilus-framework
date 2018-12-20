package thiagodnf.nautilus.plugin.zdt.encoding.objective;

import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class F1Objective extends AbstractObjective {

	@Override
	public double calculate(InstanceData data, Solution<?> sol) {

		DoubleSolution solution = (DoubleSolution) sol;

		return solution.getVariableValue(0);
	}

	@Override
	public String getName() {
		return "F1";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
	
	@Override
	public boolean isDisabled() {
		return true;
	}
}
