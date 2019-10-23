package org.nautilus.plugin.extension.correlation;

import org.nautilus.core.correlation.AbstractCorrelation;
import org.nautilus.core.correlation.SpearmanCorrelation;

public class SpearmanCorrelationExtension extends AbstractCorrelationExtension {

	@Override
	public String getName() {
		return "Spearman's Correlation";
	}

	@Override
	public AbstractCorrelation getCorrelation() {
		return new SpearmanCorrelation();
	}
}
