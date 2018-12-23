package thiagodnf.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;

public class DontNormalize extends AbstractNormalize {

	public double[] getMinimumValues(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public List<NSolution<?>> normalize(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {

		List<NSolution<?>> copiedSolutions = new ArrayList<>(solutions.size());

		for (NSolution<?> solution : solutions) {
			copiedSolutions.add((NSolution<?>) solution.copy());
		}

		return copiedSolutions;
	}

	@Override
	public String getName() {
		return "Don't Normalize";
	}
}
