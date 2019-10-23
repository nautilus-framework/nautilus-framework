package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;

public class ImportanceObjective extends AbstractObjective {
	
	protected double sum;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		sum += instance.getImportance(i);
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		return 1.0-(double) sum / (double) instance.getSumOfImportances();
	}
	
//	public boolean isMaximize() {
//		return true;
//	}
//	
//	public double getMinimumValue() {
//		return -1.0;
//	}
//
//	public double getMaximumValue() {
//		return 0.0;
//	}
	
	@Override
	public String getName() {
		return "Importance";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
