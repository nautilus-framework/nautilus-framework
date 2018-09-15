package thiagodnf.nautilus.plugin.zdt3.objective;

import org.uma.jmetal.solution.DoubleSolution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class F0Objective extends AbstractObjective {

	public F0Objective() {
		
	}

	@Override
	public double evaluate(Object sol) {
		
		DoubleSolution solution = (DoubleSolution) sol;
		
		double[] f = new double[solution.getNumberOfObjectives()];

	    f[0] = solution.getVariableValue(0);
	    double g = this.evalG(solution);
	    double h = this.evalH(f[0], g);
	    f[1] = h * g;
	    
	    return f[0];
	}
	
	/**
	 * Returns the value of the ZDT2 function G.
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
	 * Returns the value of the ZDT3 function H.
	 *
	 * @param f First argument of the function H.
	 * @param g Second argument of the function H.
	 */
	public double evalH(double f, double g) {
		double h;
		h = 1.0 - Math.sqrt(f / g) - (f / g) * Math.sin(10.0 * Math.PI * f);
		return h;
	}

	@Override
	public String getName() {
		return "F0";
	}
}
