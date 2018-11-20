package thiagodnf.nautilus.core.rmetric;

import java.util.Collections;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public abstract class ASF<S extends Solution<?>> {

	protected S zr;

	protected double[] weights;

	public ASF(S zr) {
		this(zr, Collections.nCopies(zr.getNumberOfObjectives(), 1.0));
	}

	public ASF(S zr, List<Double> weights) {
		this(zr, weights.stream().mapToDouble(x -> x).toArray());
	}

	public ASF(S zr, double[] weights) {
		this.zr = zr;
		this.weights = weights;
	}

	public abstract double calculate(S x);
}
