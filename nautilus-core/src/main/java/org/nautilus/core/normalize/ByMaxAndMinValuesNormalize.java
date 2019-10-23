package org.nautilus.core.normalize;

import java.util.List;

import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.Solution;

public class ByMaxAndMinValuesNormalize extends AbstractNormalize {

	@Override
	public double[] getMinimumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		
		double[] minValues = new double[objectives.size()];
		
		for (int i = 0; i < objectives.size(); i++) {
			minValues[i] = objectives.get(i).getMinimumValue();
		}
		
		return minValues;
	}

	@Override
	public double[] getMaximumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		
		double[] maxValues = new double[objectives.size()];

		for (int i = 0; i < objectives.size(); i++) {
			maxValues[i] = objectives.get(i).getMaximumValue();
		}
		
		return maxValues;
	}
}
