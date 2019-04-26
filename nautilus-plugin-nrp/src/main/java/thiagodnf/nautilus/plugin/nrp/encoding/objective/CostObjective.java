package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTFileInstance;

public class CostObjective extends AbstractObjective {
	
	protected double sum;
	
	@Override
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		
		TXTFileInstance instance = (TXTFileInstance) instanceData;
		
		sum += instance.getCost(i);
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {
		
		TXTFileInstance instance = (TXTFileInstance) instanceData;
		
		return (double) sum / (double) instance.getSumOfCosts();
	}
	
	public boolean isMaximize() {
		return false;
	}
	
	@Override
	public String getName() {
		return "Cost";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
