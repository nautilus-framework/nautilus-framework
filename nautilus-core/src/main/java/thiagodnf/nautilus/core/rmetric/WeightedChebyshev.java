package thiagodnf.nautilus.core.rmetric;

import org.uma.jmetal.solution.Solution;

/**
 * Classic weighted Chebyshev function
 * 
 * @author thiagodnf
 *
 */
public class WeightedChebyshev<S extends Solution<?>> extends ASF<S> {

	/**
	 * Constructor
	 * 
	 * @param zr      the reference point that represents the DM’s aspiration level
	 *                for each objective
	 * @param weights is the weight vector that implies the relative importance of
	 *                objectives
	 */
	public WeightedChebyshev(S zr, double[] weights) {
		super(zr, weights);
	}
	
	public WeightedChebyshev(S zr) {
		super(zr);
	}

	@Override
	public double calculate(S x) {

		int m = x.getNumberOfObjectives();

		double max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < m; i++) {

			double value = (x.getObjective(i) - zr.getObjective(i)) / weights[i];

			if (value > max) {
				max = value;
			}
		}

		return max;
	}

}
