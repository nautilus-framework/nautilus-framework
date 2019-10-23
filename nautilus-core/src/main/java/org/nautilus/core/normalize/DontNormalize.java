package org.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.Solution;

public class DontNormalize extends AbstractNormalize {

	public double[] getMinimumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		return new double[solutions.get(0).getNumberOfObjectives()];
	}
	
	public List<NSolution<?>> normalize(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {

		List<NSolution<?>> copiedSolutions = new ArrayList<>(solutions.size());

		for (NSolution<?> solution : solutions) {
			copiedSolutions.add((NSolution<?>) solution.copy());
		}

		return copiedSolutions;
	}
}
