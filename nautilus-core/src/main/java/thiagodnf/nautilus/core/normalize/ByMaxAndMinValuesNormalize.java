package thiagodnf.nautilus.core.normalize;

import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class ByMaxAndMinValuesNormalize extends Normalize {

	public List<Solution> normalize(List<AbstractObjective> objectives, List<Solution> solutions) {

		double[] minValues = new double[objectives.size()];
		double[] maxValues = new double[objectives.size()];

		for (int i = 0; i < objectives.size(); i++) {
			
			minValues[i] = objectives.get(i).getMinimumValue();
			maxValues[i] = objectives.get(i).getMaximumValue();
		}
		
		return normalize(solutions, minValues, maxValues);
	}

	@Override
	public String getName() {
		return "by Max And Min Values";
	}
}
