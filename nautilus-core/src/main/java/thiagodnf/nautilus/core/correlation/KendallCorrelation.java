package thiagodnf.nautilus.core.correlation;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

public class KendallCorrelation extends AbstractCorrelation {

	@Override
	public String getName() {
		return "Kendall's Correlation";
	}

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		return new KendallsCorrelation().correlation(x, y);
	}
}
