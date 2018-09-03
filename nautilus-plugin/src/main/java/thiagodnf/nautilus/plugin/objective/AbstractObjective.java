package thiagodnf.nautilus.plugin.objective;

public abstract class AbstractObjective {

	public double maximize(double value) {
		return -1.0 * value;
	}

	public double minimize(double value) {
		return value;
	}
	
	public abstract double evaluate(Object solution);
	
	public String getKey() {
		return getName().replaceAll("\\s+","-").toLowerCase();
	}
	
	public abstract String getName();
}
