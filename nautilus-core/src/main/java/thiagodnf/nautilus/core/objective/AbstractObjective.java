package thiagodnf.nautilus.core.objective;

import thiagodnf.nautilus.core.util.Converter;

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

	public String getKey() {
		return Converter.toKey(getName());
	}

	public String toString() {
		return getKey();
	}

	public abstract double evaluate(Object solution);

	public abstract String getName();
}
