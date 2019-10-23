package thiagodnf.nautilus.plugin.zdt.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

public class F1Objective extends AbstractObjective {

	@Override
	public double calculate(Instance data, Solution<?> sol) {

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
