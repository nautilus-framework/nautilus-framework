package thiagodnf.nautilus.plugin.objective;

public abstract class AbstractObjective<S> {

	public double maximize(double value) {
		return -1.0 * value;
	}

	public double minimize(double value) {
		return value;
	}
	
	public abstract double evaluate(S solution);
}
