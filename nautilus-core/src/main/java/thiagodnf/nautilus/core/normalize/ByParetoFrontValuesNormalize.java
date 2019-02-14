package thiagodnf.nautilus.core.normalize;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ByParetoFrontValuesNormalize extends AbstractNormalize {

	/**
	 * This method returns an array contains the minimum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of minimum values for each objective
	 */
	public double[] getMinimumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {

		int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

		double[] minimumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double minValue = Double.MAX_VALUE;

			for (Solution<?> solution : solutions) {

				if (solution.getObjective(i) < minValue) {
					minValue = solution.getObjective(i);
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
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {

		int numberOfObjectives = solutions.get(0).getNumberOfObjectives();

		double[] maximumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double maxValue = Double.NEGATIVE_INFINITY;
			
			for (Solution<?> solution : solutions) {

				if (solution.getObjective(i) > maxValue) {
					maxValue = solution.getObjective(i);
				}
			}

			maximumValues[i] = maxValue;
		}

		return maximumValues;
	}

	@Override
	public String getName() {
		return "By Pareto-front Values";
	}
}
