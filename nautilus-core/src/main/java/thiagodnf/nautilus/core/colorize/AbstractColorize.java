package thiagodnf.nautilus.core.colorize;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public abstract class AbstractColorize {
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public List<Solution<?>> execute(List<Solution<?>> solutions) {
		
		List<Solution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions);
		
		for (Solution<?> solution : solutions) {
			solution.setAttribute(SolutionAttribute.DISTANCE, calculate(solution, selectedSolutions));
		}
		
		return solutions;
	}	
	
	public String calculate(Solution<?> s, List<Solution<?>> selectedSolutions) {
		
		if (selectedSolutions.isEmpty()) {
			return "0.0";
		}

		double minDistance = Double.MAX_VALUE;

		Solution<?> closeSolution = null;

		for (Solution<?> selected : selectedSolutions) {

			double distance = getDistance(s, selected);

			if (distance < minDistance) {
				minDistance = distance;
				closeSolution = selected;
			}
		}
		
		minDistance = Normalizer.normalize(minDistance, 0, Math.sqrt(2));

		double feedback = 0.0;

		if (closeSolution instanceof GenericSolution) {
			feedback = ((GenericSolution) closeSolution).getUserFeedback();
		}

		double distance = 0.0;

		if (feedback == 0) {
			distance = minDistance;
		} else if (feedback > 0) {
			distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
		} else {
			distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
		}
		
		return String.valueOf(distance);
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract double getDistance(Solution<?> s, Solution<?> selectedSolutions) ;
	
	public abstract String getName() ;
}
