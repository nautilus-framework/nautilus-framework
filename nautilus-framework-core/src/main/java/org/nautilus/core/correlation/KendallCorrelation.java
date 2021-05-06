package org.nautilus.core.correlation;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

public class KendallCorrelation extends AbstractCorrelation {

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		return new KendallsCorrelation().correlation(x, y);
	}
}
