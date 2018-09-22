package thiagodnf.nautilus.core.objective;

public abstract class AbstractObjective {

	public double maximize(double value) {
		return -1.0 * value;
	}

	public double minimize(double value) {
		return value;
	}
	
	public double getMinimumValue() {
		return 0.0;
	}
	
	public double getMaximumValue() {
		return 1.0;
	}
	
	public abstract double evaluate(Object solution);
	
	public String getKey() {
		return getName().replaceAll("\\s+","-").toLowerCase();
	}
	
	public String toString() {
		return getKey();
	}
	
	public abstract String getName();
}
