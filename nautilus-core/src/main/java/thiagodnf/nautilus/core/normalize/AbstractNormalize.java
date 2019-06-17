package thiagodnf.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

public abstract class AbstractNormalize {

	public final String getId() {
		return Converter.toKey(getName());
	}
	
	/**
	 * This method returns a copy of a given list of solutions with normalized objective values.
	 * 
	 * @param solutions a list of solutions
	 * @return a copy of a given list of solutions with normalized objective values
	 */
	public List<NSolution<?>> normalize(List<AbstractObjective> objectives, List<NSolution<?>> solutions) {
		
		if (objectives.size() == 1) {
			return solutions;
		}
		
		if (solutions.isEmpty()) {
			return new ArrayList<>();
		}
		
		double[] minValues = getMinimumValues(objectives, solutions);
		double[] maxValues = getMaximumValues(objectives, solutions);

		List<NSolution<?>> normalizedSolutions = new ArrayList<>(solutions.size());

		int numberOfObjectives = minValues.length;

		for (NSolution<?> solution : solutions) {

			NSolution<?> copy = (NSolution<?>) solution.copy();

			for (int i = 0; i < numberOfObjectives; i++) {
				copy.setObjective(i, normalize(solution.getObjective(i), minValues[i], maxValues[i]));
			}

			normalizedSolutions.add(copy);
		}

		return normalizedSolutions;
	}
	
	protected double normalize(double value, double min, double max) {
		return Normalizer.normalize(value, min, max);
	}

	public Solution<?> normalize(List<AbstractObjective> objectives, Solution<?> solution) {
		return this.normalize(
			objectives, 
			solution, 
			getMinimumValues(objectives, Arrays.asList(solution)),
			getMaximumValues(objectives, Arrays.asList(solution))
		);
	}
	
	public Solution<?> normalize(List<AbstractObjective> objectives, Solution<?> solution, double[] minValues, double[] maxValues) {
		
		Solution<?> copy = (Solution<?>) solution.copy();

		for (int i = 0; i < solution.getNumberOfObjectives(); i++) {
			copy.setObjective(i, Normalizer.normalize(solution.getObjective(i), minValues[i], maxValues[i]));
		}

		return copy;
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
