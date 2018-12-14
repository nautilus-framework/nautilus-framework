package thiagodnf.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

public abstract class Normalize {

	public String getId() {
		return Converter.toKey(getName());
	}
	
	/**
	 * This method returns a copy of a given list of solutions with normalized objective values.
	 * 
	 * @param solutions a list of solutions
	 * @return a copy of a given list of solutions with normalized objective values
	 */
	public List<? extends Solution<?>> normalize(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) {
		
		if (objectives.size() == 1) {
			return solutions;
		}
		
		if (solutions.isEmpty()) {
			return new ArrayList<>();
		}
		
		double[] minValues = getMinimumValues(objectives, solutions);
		double[] maxValues = getMaximumValues(objectives, solutions);

		List<Solution<?>> normalizedSolutions = new ArrayList<>(solutions.size());

		int numberOfObjectives = minValues.length;

		for (Solution<?> solution : solutions) {

			Solution<?> copy = solution.copy();

			for (int i = 0; i < numberOfObjectives; i++) {
				copy.setObjective(i, Normalizer.normalize(solution.getObjective(i), minValues[i], maxValues[i]));
			}

			normalizedSolutions.add(copy);
		}

		return normalizedSolutions;
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract String getName() ;
	
	/**
	 * This method returns an array contains the minimum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of minimum values for each objective
	 */
	public abstract double[] getMinimumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) ;
	
	/**
	 * This method returns an array contains the maximum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of maximum values for each objective
	 */
	public abstract double[] getMaximumValues(List<AbstractObjective> objectives, List<? extends Solution<?>> solutions) ;
}
