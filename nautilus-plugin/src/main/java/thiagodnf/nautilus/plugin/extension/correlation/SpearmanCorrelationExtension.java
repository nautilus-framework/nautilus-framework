package thiagodnf.nautilus.plugin.extension.correlation;

import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.correlation.SpearmanCorrelation;

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
