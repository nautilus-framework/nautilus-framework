package thiagodnf.nautilus.core.correlation;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class PearsonCorrelation extends Correlation {

	@Override
	public String getName() {
		return "Pearson's Correlation";
	}

	@Override
	public double getCorrelation(double[] x, double[] y) {
		return new PearsonsCorrelation().correlation(x, y);
	}
}
