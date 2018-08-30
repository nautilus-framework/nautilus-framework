package thiagodnf.nautilus.plugin.mip.objective;

import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.plugin.objective.AbstractObjective;

public class MinimumNumberObjective extends AbstractObjective<IntegerSolution> {

	private int searchNumber;

	public MinimumNumberObjective(int searchNumber) {
		this.searchNumber = searchNumber;
	}

	@Override
	public double evaluate(IntegerSolution solution) {

		int numbers = 0;

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (solution.getVariableValue(i) == searchNumber) {
				numbers++;
			}
		}

		double value = (double) numbers / (double) solution.getNumberOfVariables();

		return minimize(value);
	}
}
