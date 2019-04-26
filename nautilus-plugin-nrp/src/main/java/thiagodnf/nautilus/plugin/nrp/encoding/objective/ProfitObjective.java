package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTFileInstance;

public class ProfitObjective extends AbstractObjective {
	
	protected double sum;
	
	@Override
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		
		TXTFileInstance instance = (TXTFileInstance) instanceData;
		
		sum += instance.getProfit(i);
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {
		
		TXTFileInstance instance = (TXTFileInstance) instanceData;
		
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
