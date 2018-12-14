package thiagodnf.nautilus.core.correlation;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class PearsonCorrelation extends AbstractCorrelation {

	@Override
	public String getName() {
		return "Pearson's Correlation";
	}

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		return new PearsonsCorrelation().correlation(x, y);
	}
}
