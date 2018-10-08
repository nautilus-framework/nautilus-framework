package thiagodnf.nautilus.core.objective;

import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractObjective {

	private boolean isChecked = true;
	
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

	public String getId() {
		return Converter.toKey(getName());
	}

	public String toString() {
		return getId();
	}
	
	/**
	 * It returns if this objective is going to be  'selected' in the screen
	 * 
	 * @return True is checked, otherwise
	 */
	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean status) {
		this.isChecked = status;
	}
	
	public abstract double evaluate(Object solution);

	public abstract String getName();
	
	public abstract String getGroupName();
}
