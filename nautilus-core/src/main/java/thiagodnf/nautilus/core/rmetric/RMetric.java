package thiagodnf.nautilus.core.rmetric;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.point.PointSolution;

import thiagodnf.nautilus.core.algorithm.RNSGAII.PointSolutionUtils;


/**
 * Based on paper:
 * 
 * R-Metric: Evaluating the Performance of Preference-Based
 * Evolutionary Multi-Objective Optimization Using Reference Points 
 * 
 * Ke Li, Member, IEEE, Kalyanmoy Deb, Fellow, IEEE, and Xin Yao, Fellow, IEEE"
 * 
 * @author thiagodnf
 * @since 2018-11-14
 * @version 1.0.0
 */
public class RMetric {
	
	private PointSolution zr;

	private double[] weights;
	
	private double delta;

	public RMetric(PointSolution zr, double[] weights, double delta) {
		this.zr = zr;
		this.weights = weights;
		this.delta = delta;
	}

	public RMetric(PointSolution zr, double delta) {
		this(zr, Collections.nCopies(zr.getNumberOfObjectives(), 1.0), delta);
	}

	public RMetric(PointSolution zr, List<Double> weights, double delta) {
		this(zr, weights.stream().mapToDouble(x -> x).toArray(), delta);
	}
	
	public List<PointSolution> execute(List<PointSolution> solutions) {
		
		List<PointSolution> S = PointSolutionUtils.copy(solutions);
		
		// Step 1
		S = prescreeningProcedure(S);
		
		// Step 2
		PointSolution zp = pivotPointIdentification(S);
		
		// Step 3
		S = trimmingProcedure(zp, S);
		
		// Step 4
		S = solutionTransfer(zp, S);
		
		return S;
	}
	
	public List<PointSolution> prescreeningProcedure(List<PointSolution> S) {
		return PointSolutionUtils.copy(SolutionListUtils.getNondominatedSolutions(S));
	}
	
	public PointSolution pivotPointIdentification(List<PointSolution> S) {

		ASF<PointSolution> asf = new WeightedChebyshev<>(zr, weights);

		PointSolution zp = null;
		double min = Double.POSITIVE_INFINITY;

		for (PointSolution x : S) {

			double value = asf.calculate(x);

			if (value < min) {
				min = value;
				zp = x.copy();
			}
		}

		return zp;
	}
	
	public List<PointSolution> trimmingProcedure(PointSolution zp, List<PointSolution> S) {

		Iterator<PointSolution> it = S.iterator();
		
		while (it.hasNext()) {
			
			PointSolution x = it.next();
		
			for (int j = 0; j < x.getNumberOfObjectives(); j++) {

				double value = Math.abs(x.getObjective(j) - zp.getObjective(j));

				if (value > (delta / 2.0)) {
					it.remove();
					break;
				}
			}
		}

		return S;
	}
	
	protected List<PointSolution> solutionTransfer(PointSolution zp, List<PointSolution> S) {
		
		PointSolution zw = getWorstPoint();
		
		PointSolution zl = getIsoASFPoint(zp, zw);
		
		List<PointSolution> shifted = new LinkedList<>();
		
		PointSolution vector = new PointSolution(zp.getNumberOfObjectives());

		for (int i = 0; i < vector.getNumberOfObjectives(); i++) {
			vector.setObjective(i, zl.getObjective(i) - zp.getObjective(i));
		}
		
		for(PointSolution x : S) {
			shifted.add(PointSolutionUtils.shift(x, vector));
		}
		
		return shifted;
	}
	
	public PointSolution getWorstPoint() {

		PointSolution zw = new PointSolution(zr.getNumberOfObjectives());

		for (int i = 0; i < zw.getNumberOfObjectives(); i++) {
			zw.setObjective(i, zr.getObjective(i) + 2.0 * weights[i]);
		}

		return zw;
	}
	
	protected PointSolution getIsoASFPoint(PointSolution zp, PointSolution zw) {
		
		int k = getObjectiveKThatContributesToTheASFValue(zp, zw);

		PointSolution zl = new PointSolution(zp.getNumberOfObjectives());

		double delta = (zp.getObjective(k) - zr.getObjective(k)) / (zw.getObjective(k) - zr.getObjective(k));

		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {
			zl.setObjective(i, zr.getObjective(i) + delta * (zw.getObjective(i) - zr.getObjective(i)));
		}

		return zl;
	}
	
	protected int getObjectiveKThatContributesToTheASFValue(PointSolution zp, PointSolution zw) {

		int k = Integer.MIN_VALUE;
		double max = Double.NEGATIVE_INFINITY;

		for (int i = 0; i < zp.getNumberOfObjectives(); i++) {

			double value = (zp.getObjective(i) - zr.getObjective(i)) / (zw.getObjective(i) - zr.getObjective(i));

			if (value > max) {
				max = value;
				k = i;
			}
		}

		return k;
	}

	public PointSolution getZr() {
		return zr;
	}	
}
