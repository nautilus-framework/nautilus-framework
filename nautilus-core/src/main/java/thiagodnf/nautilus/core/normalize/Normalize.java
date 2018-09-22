package thiagodnf.nautilus.core.normalize;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.objective.AbstractObjective;
import thiagodnf.nautilus.core.util.Normalizer;

public abstract class Normalize {

	public String getKey() {
		return getName().replaceAll("\\s+","-").toLowerCase();
	}
	
	protected List<Solution> normalize(List<Solution> solutions, double[] minValues, double[] maxValues) {
		
		if (solutions.isEmpty()) {
			return new ArrayList<>(solutions.size());
		}

		if (solutions.size() == 1) {
			return new ArrayList<>(solutions);
		}

		List<Solution> normalizedSolutions = new ArrayList<>(solutions.size());

		int numberOfObjectives = solutions.get(0).getObjectives().size();

		for (Solution solution : solutions) {

			Solution copy = solution.copy();

			for (int j = 0; j < numberOfObjectives; j++) {
				copy.getObjectives().set(j, Normalizer.normalize(solution.getObjective(j), minValues[j], maxValues[j]));
			}

			normalizedSolutions.add(copy);
		}

		return normalizedSolutions;
	}
	
	public String toString() {
		return getName();
	}
	
	/**
	 * This method returns a copy of a given list of solutions with normalized objective values.
	 * 
	 * @param solutions a list of solutions
	 * @return a copy of a given list of solutions with normalized objective values
	 */
	public abstract List<Solution> normalize(List<AbstractObjective> objectives, List<Solution> solutions);
	
	public abstract String getName() ;
}
