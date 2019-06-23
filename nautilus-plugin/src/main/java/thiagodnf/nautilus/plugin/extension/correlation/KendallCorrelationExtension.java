package thiagodnf.nautilus.plugin.extension.correlation;



import thiagodnf.nautilus.core.correlation.AbstractCorrelation;
import thiagodnf.nautilus.core.correlation.KendallCorrelation;

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
