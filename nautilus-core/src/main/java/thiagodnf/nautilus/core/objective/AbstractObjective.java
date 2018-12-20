package thiagodnf.nautilus.core.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractObjective {

	private boolean isChecked = true;
	
	private boolean isDisabled = false;
	
	public boolean isMaximize() {
		return false;
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
	
	public boolean isDisabled() {
		return isDisabled;
	}

	public void setDisabled(boolean status) {
		this.isDisabled = status;
	}
	
	public double evaluate(InstanceData data, Solution<?> sol) {

		double value = calculate(data, sol);

		if (isMaximize()) {
			return -1.0 * value;
		}

		return value;
	}

	public abstract String getName();
	
	public abstract String getGroupName();
	
	public abstract double calculate(InstanceData data, Solution<?> solution);	
}
