package thiagodnf.nautilus.plugin.toy.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.solution.NIntegerSolution;
import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class NumberOfObjective extends AbstractObjective {

	private int searchFor;

	public NumberOfObjective(int searchFor) {
		this.searchFor = searchFor;
	}

	@Override
	public double calculate(Instance data, Solution<?> sol) {

		NIntegerSolution solution = (NIntegerSolution) sol;

		int numbers = 0;

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (solution.getVariableValue(i) == searchFor) {
				numbers++;
			}
		}

		return (double) numbers / (double) solution.getNumberOfVariables();
	}

	@Override
	public double getMinimumValue() {
		return -1.0;
	}

	@Override
	public double getMaximumValue() {
		return 0.0;
	}

	@Override
	public String getName() {
		return "Number of " + this.searchFor + "s";
	}

	@Override
	public boolean isMaximize() {
		return true;
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
