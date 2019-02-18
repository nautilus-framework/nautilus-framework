package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;
import java.util.Set;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class UncoveredPairsObjective extends AbstractObjective {
	
	protected Set<Integer> coveredPairs = new HashSet<>();
	
//	@Override
//	public double calculate(InstanceData instanceData, Solution<?> sol) {
//
//		return 0.0;
//		BinarySolution solution = (BinarySolution) sol;
//
//		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
//
//		BinarySet binarySet = solution.getVariableValue(0);
//
//		Set<Integer> coveredPairs = new HashSet<>();
//
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//			if (binarySet.get(i)) {
//				coveredPairs.addAll(instance.getPairs(i));
//			}
//		}
//
//		return 1.0 - (double) coveredPairs.size() / (double) instance.getNumberOfPairs();
//	}
	
	@Override
	public void beforeProcess(InstanceData instanceData) {
		this.coveredPairs = new HashSet<>();
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol, int i) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		coveredPairs.addAll(instance.getPairs(i));
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;

		return 1.0 - (double) coveredPairs.size() / (double) instance.getNumberOfPairs();
	}
	
	@Override
	public String getName() {
		return "Uncovered Pairs";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
