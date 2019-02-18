package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;
import java.util.Set;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.AbstractTXTInstanceData;

public class UnselectedFeaturesObjective extends AbstractObjective {
	
	protected Set<Integer> selectedFeatures ;
	
//	@Override
//	public double calculate(InstanceData instanceData, Solution<?> sol) {
//
//		return 0.0;
//		BinarySolution solution = (BinarySolution) sol;
//
//		AbstractTXTInstanceData data = (AbstractTXTInstanceData) instanceData;
//
//		BinarySet binarySet = solution.getVariableValue(0);
//
//		Set<Integer> selectedFeatures = new HashSet<>();
//
//		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {
//			if (binarySet.get(i)) {
//				selectedFeatures.addAll(data.getProduct(i));
//			}
//		}
//
//		return 1.0 - (double) selectedFeatures.size() / (double) data.getNumberOfFeatures();
//	}
	
	@Override
	public void beforeProcess(InstanceData instanceData, Solution<?> sol) {
		this.selectedFeatures = new HashSet<>();
	}
	
	@Override
	public void process(InstanceData instanceData, Solution<?> sol,  int i) {
		
		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;
		
		selectedFeatures.addAll(instance.getProduct(i));
	}
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		AbstractTXTInstanceData instance = (AbstractTXTInstanceData) instanceData;

		return 1.0 - (double) selectedFeatures.size() / (double) instance.getNumberOfFeatures();
	}
	
	@Override
	public String getName() {
		return "Unselected Features";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
