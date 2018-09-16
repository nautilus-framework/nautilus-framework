package thiagodnf.nautilus.plugin.mip.objective;

import org.uma.jmetal.solution.IntegerSolution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class MaximumNumberObjective extends AbstractObjective {

	private int searchFor;

	public MaximumNumberObjective(int searchFor) {
		this.searchFor = searchFor;
	}

	@Override
	public double evaluate(Object sol) {
		
		IntegerSolution solution = (IntegerSolution) sol;

		int numbers = 0;

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {

			if (solution.getVariableValue(i) == searchFor) {
				numbers++;
			}
		}
		
//		if(searchFor == 1) {
//			for (int i = 0; i < solution.getNumberOfVariables(); i++) {
//
//				if (solution.getVariableValue(i) == 10) {
//					numbers++;
//				}
//			}
//		}

		double value = (double) numbers / (double) solution.getNumberOfVariables();

		return maximize(value);
	}

	@Override
	public String getName() {
		return "Number of " + this.searchFor + "s";
	}
}
