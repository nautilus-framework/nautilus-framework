package thiagodnf.nautilus.plugin.spl.encoding.objective;

import java.util.HashSet;
import java.util.Set;

import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.binarySet.BinarySet;

import thiagodnf.nautilus.core.model.InstanceData;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.plugin.spl.encoding.instance.TXTInstanceData;

public class UnselectedFeaturesObjective extends AbstractObjective {
	
	@Override
	public double calculate(InstanceData instanceData, Solution<?> sol) {

		BinarySolution solution = (BinarySolution) sol;

		TXTInstanceData data = (TXTInstanceData) instanceData;

		BinarySet binarySet = solution.getVariableValue(0);

		Set<String> selectedFeatures = new HashSet<>();

		for (int i = 0; i < binarySet.getBinarySetLength(); i++) {

			if (binarySet.get(i)) {

				for (String feature : data.getProduct(i)) {
					selectedFeatures.add(feature);
				}
			}
		}

		return 1.0 - (double) selectedFeatures.size() / (double) data.getNumberOfFeatures();
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
		return "Unselected Features";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
}
