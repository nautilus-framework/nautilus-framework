package thiagodnf.nautilus.core.normalize;

import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ByParetoFrontValuesNormalize extends Normalize {

	/**
	 * This method returns an array contains the minimum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of minimum values for each objective
	 */
	public static double[] getMinimumValues(List<Solution> solutions) {

		int numberOfObjectives = solutions.get(0).getObjectives().size();

		double[] minimumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double minValue = Double.MAX_VALUE;

			for (Solution solution : solutions) {

				if (solution.getObjective(i) < minValue) {
					minValue = solution.getObjectives().get(i);
				}
			}

			minimumValues[i] = minValue;
		}

		return minimumValues;
	}
	
	/**
	 * This method returns an array contains the maximum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of maximum values for each objective
	 */
	public static double[] getMaximumValues(List<Solution> solutions) {

		int numberOfObjectives = solutions.get(0).getObjectives().size();

		double[] maximumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double maxValue = Double.NEGATIVE_INFINITY;
			
			for (Solution solution : solutions) {

				if (solution.getObjective(i) > maxValue) {
					maxValue = solution.getObjectives().get(i);
				}
			}

			maximumValues[i] = maxValue;
		}

		return maximumValues;
	}
	
	/**
	 * This method returns a copy of a given list of solutions with normalized
	 * objective values.
	 * 
	 * @param solutions a list of solutions
	 * @return a copy of a given list of solutions with normalized objective values
	 */
	public List<Solution> normalize(List<AbstractObjective> objectives, List<Solution> solutions) {

		double[] minValues = getMinimumValues(solutions);
		double[] maxValues = getMaximumValues(solutions);

		return normalize(solutions, minValues, maxValues);
	}

	@Override
	public String getName() {
		return "by Pareto-front Values";
	}
}
