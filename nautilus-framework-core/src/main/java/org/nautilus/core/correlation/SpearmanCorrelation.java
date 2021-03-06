package org.nautilus.core.correlation;

import org.apache.commons.math3.stat.correlation.SpearmansCorrelation;

public class SpearmanCorrelation extends AbstractCorrelation {

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		return new SpearmansCorrelation().correlation(x, y);
	}
}
