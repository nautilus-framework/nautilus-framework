package org.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

public class CostObjective extends AbstractObjective {
	
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
//			if (binarySet.get(i)) {
//				sum += instance.getProductCost(i);
//			}
//		}
//		
//		return (double) sum / (double) instance.getSumOfCosts();
//	}
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.sum = 0.0;
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		sum += instance.getProductCost(i);
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		return (double) sum / (double) instance.getSumOfCosts();
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
