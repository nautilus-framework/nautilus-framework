package thiagodnf.nautilus.core.colorize;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.core.util.SolutionAttribute;
import thiagodnf.nautilus.core.util.SolutionListUtils;

public abstract class AbstractColorize {
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public List<NSolution<?>> execute(List<NSolution<?>> solutions) {
		
		List<NSolution<?>> selectedSolutions = SolutionListUtils.getSelectedSolutions(solutions);
		
		for (NSolution<?> solution : solutions) {
			solution.setAttribute(SolutionAttribute.DISTANCE, calculate(solution, selectedSolutions));
		}
		
		return solutions;
	}	
	
	public String calculate(NSolution<?> s, List<NSolution<?>> selectedSolutions) {
		
		if (selectedSolutions.isEmpty()) {
			return "0.0";
		}

		double minDistance = Double.MAX_VALUE;

		NSolution<?> closeSolution = null;

		for (NSolution<?> selected : selectedSolutions) {

			double distance = getDistance(s, selected);

			if (distance < minDistance) {
				minDistance = distance;
				closeSolution = selected;
			}
		}
		
		minDistance = Normalizer.normalize(minDistance, 0, Math.sqrt(2));

		double feedback = closeSolution.getUserFeedback();

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
