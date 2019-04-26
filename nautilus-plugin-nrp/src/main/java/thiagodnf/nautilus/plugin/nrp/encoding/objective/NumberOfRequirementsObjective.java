package thiagodnf.nautilus.plugin.nrp.encoding.objective;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.nrp.encoding.instance.TXTFileInstance;

public class NumberOfRequirementsObjective extends AbstractObjective {
	
	protected int numbersOfRequirements;
	
	@Override
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		this.numbersOfRequirements = 0;
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		numbersOfRequirements++;
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {
		
		TXTFileInstance instance = (TXTFileInstance) instanceData;
		
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
