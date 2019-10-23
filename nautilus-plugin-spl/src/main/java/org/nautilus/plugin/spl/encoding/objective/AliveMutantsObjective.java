package org.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;
import java.util.Set;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

public class AliveMutantsObjective extends AbstractObjective {

	protected Set<Integer> deadMutants;
	
//	@Override
//	public double calculate(InstanceData instanceData, Solution<?> sol) {
//return 0.0;
//		BinarySolution solution = (BinarySolution) sol;
//		
//		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
//
//		BinarySet binarySet = solution.getVariableValue(0) ;
//		
//		Set<Integer> deadMutants = new HashSet<>();
//
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//			if (binarySet.get(i)) {
//				deadMutants.addAll(instance.getMutants(i));
//			}
//		}
//
//		return 1.0 - (double) deadMutants.size() / (double) instance.getNumberOfMutants();
//	}
	
	@Override
	public void beforeProcess(Instance instanceData, Solution<?> sol) {
		this.deadMutants = new HashSet<>();
	}
	
	@Override
	public void process(Instance instanceData, Solution<?> sol, int i) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		deadMutants.addAll(instance.getMutants(i));
	}
	
	@Override
	public double calculate(Instance instanceData, Solution<?> sol) {

		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;

		return 1.0 - (double) deadMutants.size() / (double) instance.getNumberOfMutants();
	}
	
	@Override
	public String getName() {
		return "Alive Mutants";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
