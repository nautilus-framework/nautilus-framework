package thiagodnf.nautilus.plugin.spl.encoding.objective;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.TXTInstanceData;

public class CostObjective extends AbstractObjective {
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		BinarySolution solution = (BinarySolution) sol;

		BinarySet binarySet = solution.getVariableValue(0) ;
		
		TXTInstanceData data = (TXTInstanceData) instanceData;
		
		double sum = 0.0;
		
		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
			
			if (binarySet.get(i)) {
		
				sum += data.getProductCost(i);
			}
		}
		
		return (double) sum / (double) data.getSumOfCosts();
	}
	
	@Override
	public double getMinimumValue() {
		return 0.0;
	}
	
	@Override
	public double getMaximumValue() {
		return 1.0;
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