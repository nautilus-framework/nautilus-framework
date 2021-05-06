package org.nautilus.plugin.extension.correlation;


import org.nautilus.core.correlation.AbstractCorrelation;
import org.nautilus.core.correlation.PearsonCorrelation;

public class PearsonCorrelationExtension extends AbstractCorrelationExtension {

	@Override
	public String getName() {
		return "Pearson's Correlation";
	}

	@Override
	public AbstractCorrelation getCorrelation() {
		return new PearsonCorrelation();
	}
}
