package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class UnimportantFeaturesObjective extends AbstractObjective {
	
	protected double sum;
	
//	@Override
//	public double calculate(InstanceData instanceData, Solution<?> sol) {
//
//		return 0.0;
//		BinarySolution solution = (BinarySolution) sol;
//
//		BinarySet binarySet = solution.getVariableValue(0) ;
//		
//		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
//		
//		double sum = 0.0;
//
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//
//			if (binarySet.get(i)) {
//				sum += instance.getProductImportance(i);
//			}
//		}
//
//		return 1.0 - ((double) sum / (double) instance.getSumOfImportance());
//	}
	
	@Override
	public void beforeProcess(InstanceData instanceData) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		sum += instance.getProductImportance(i);
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;

		return 1.0 - ((double) sum / (double) instance.getSumOfImportance());
	}
	
	@Override
	public String getName() {
		return "Unimportant Features";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
