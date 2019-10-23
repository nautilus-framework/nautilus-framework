package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;

public class CostObjective extends AbstractObjective {
	
	protected double sum;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		sum += instance.getCost(i);
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
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
