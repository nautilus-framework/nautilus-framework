package thiagodnf.nautilus.core.normalize;

import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ByMaxAndMinValuesNormalize extends AbstractNormalize {

	@Override
	public String getName() {
		return "By Max And Min Values";
	}

	@Override
	public double[] getMinimumValues(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		
		double[] minValues = new double[objectives.size()];
		
		for (int i = 0; i < objectives.size(); i++) {
			minValues[i] = objectives.get(i).getMinimumValue();
		}
		
		return minValues;
	}

	@Override
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		
		double[] maxValues = new double[objectives.size()];

		for (int i = 0; i < objectives.size(); i++) {
			maxValues[i] = objectives.get(i).getMaximumValue();
		}
		
		return maxValues;
	}
}
