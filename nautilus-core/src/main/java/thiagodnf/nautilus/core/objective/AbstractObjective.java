package thiagodnf.nautilus.core.objective;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

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
	
	public int getOrder() {
		return 1;
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
	
	public double evaluateAll(InstanceData instance, Solution<?> solution) {
		
		BinarySet binarySet = (BinarySet) solution.getVariableValue(0);

		beforeProcess(instance, solution);
		
		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

			if (binarySet.get(i)) {
				process(instance, solution, i);
			}
		}

		afterProcess(instance, solution);
		
		double value = calculate(instance, solution);
		
		if (isMaximize()) {
			return -1.0 * value;
		}

		return value;
	}
	
	public double evaluate(InstanceData data, Solution<?> sol) {

		double value = calculate(data, sol);

		if (isMaximize()) {
			return -1.0 * value;
		}

		return value;
	}
	
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		
	}
	
	public void process(InstanceData instance, Solution<?> sol, int i) {
		
	}
	
	public void afterProcess(InstanceData instanceData, Solution<?> sol) {
		
	}
	
	public double calculate(InstanceData instanceData) {
		return 0.0;
	}
	

	public abstract String getName();
	
	public abstract String getGroupName();
	
	public abstract double calculate(InstanceData data, Solution<?> solution);

			
}
