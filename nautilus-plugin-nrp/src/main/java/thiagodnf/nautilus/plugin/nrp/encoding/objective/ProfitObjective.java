package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;

public class ProfitObjective extends AbstractObjective {
	
	protected double sum;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		sum += instance.getProfit(i);
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		return 1.0-(double) sum / (double) instance.getSumOfProfits();
	}
	
//	public boolean isMaximize() {
//		return true;
//	}
	
//	public double getMinimumValue() {
//		return -1.0;
//	}
//
//	public double getMaximumValue() {
//		return 0.0;
//	}
	
	@Override
	public String getName() {
		return "Profit";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
