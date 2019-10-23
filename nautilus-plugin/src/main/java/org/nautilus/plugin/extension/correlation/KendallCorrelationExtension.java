package org.nautilus.plugin.extension.correlation;



import org.nautilus.core.correlation.AbstractCorrelation;
import org.nautilus.core.correlation.KendallCorrelation;

public class KendallCorrelationExtension extends AbstractCorrelationExtension {

	@Override
	public String getName() {
		return "Kendall's Correlation";
	}

	@Override
	public AbstractCorrelation getCorrelation() {
		return new KendallCorrelation();
	}
}
