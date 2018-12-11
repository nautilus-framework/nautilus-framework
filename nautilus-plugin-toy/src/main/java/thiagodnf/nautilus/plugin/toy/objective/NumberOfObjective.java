package thiagodnf.nautilus.plugin.toy.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.solution.IntegerSolution;

public class NumberOfObjective extends AbstractObjective {

	private int searchFor;

	public NumberOfObjective(int searchFor) {
		this.searchFor = searchFor;
	}
	
	@Override
	public double calculate(InstanceData data, Solution<?> sol) {

		IntegerSolution solution = (IntegerSolution) sol;

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

		if (searchFor % 2 == 0) {
			return "Even";
		}

		return "Odd";
	}
}
