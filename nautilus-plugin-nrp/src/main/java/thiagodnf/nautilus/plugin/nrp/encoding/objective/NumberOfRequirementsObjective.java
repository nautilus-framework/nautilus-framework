package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.Instance;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTInstance;

public class NumberOfRequirementsObjective extends AbstractObjective {
	
	protected int numbersOfRequirements;
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.numbersOfRequirements = 0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		numbersOfRequirements++;
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		TXTInstance instance = (TXTInstance) instanceData;
		
		return (double) numbersOfRequirements / (double) instance.getNumberOfRequirements();
	}
	
	public boolean isMaximize() {
		return false;
	}
	
	@Override
	public String getName() {
		return "Number of Requirements";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}