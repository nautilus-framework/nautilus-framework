package thiagodnf.nautilus.plugin.zdt1.objective;

import org.uma.jmetal.solution.DoubleSolution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class F1Objective extends AbstractObjective {

	@Override
	public double evaluate(Object sol) {

		DoubleSolution solution = (DoubleSolution) sol;

		double[] f = new double[solution.getNumberOfObjectives()];

	    f[0] = solution.getVariableValue(0);
	    double g = this.evalG(solution);
	    double h = this.evalH(f[0], g);
	    f[1] = h * g;

		return f[1];
	}

	/**
	 * Returns the value of the ZDT1 function G.
	 *
	 * @param solution Solution
	 */
	private double evalG(DoubleSolution solution) {
		double g = 0.0;
		for (int i = 1; i < solution.getNumberOfVariables(); i++) {
			g += solution.getVariableValue(i);
		}
		double constant = 9.0 / (solution.getNumberOfVariables() - 1);
		g = constant * g;
		g = g + 1.0;
		return g;
	}

	/**
	 * Returns the value of the ZDT1 function H.
	 *
	 * @param f First argument of the function H.
	 * @param g Second argument of the function H.
	 */
	public double evalH(double f, double g) {
		double h;
		h = 1.0 - Math.sqrt(f / g);
		return h;
	}

	@Override
	public String getName() {
		return "F1";
	}

	@Override
	public String getGroupName() {
		// TODO Auto-generated method stub
		return null;
	}
}
