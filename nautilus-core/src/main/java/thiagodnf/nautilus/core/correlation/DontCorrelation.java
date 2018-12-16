package thiagodnf.nautilus.core.correlation;

public class DontCorrelation extends AbstractCorrelation {

	public boolean isEnabled() {
		return false;
	}
	
	@Override
	public String getName() {
		return "Don't Correlate";
	}

	@Override
	public double getCorrelation(final double[] x, final double[] y) {
		return 0.0;
	}
}
