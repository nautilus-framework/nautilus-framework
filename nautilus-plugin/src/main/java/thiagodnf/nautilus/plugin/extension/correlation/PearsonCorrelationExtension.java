package thiagodnf.nautilus.plugin.extension.correlation;


import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.correlation.PearsonCorrelation;

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
