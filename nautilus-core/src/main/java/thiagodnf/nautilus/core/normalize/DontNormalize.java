package thiagodnf.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;

public class DontNormalize extends Normalize {

	public double[] getMinimumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public List<Solution<?>> normalize(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {

		List<Solution<?>> copiedSolutions = new ArrayList<>(solutions.size());

		for (Solution<?> solution : solutions) {
			copiedSolutions.add(solution.copy());
		}

		return copiedSolutions;
	}

	@Override
	public String getName() {
		return "Don't Normalize";
	}
}
