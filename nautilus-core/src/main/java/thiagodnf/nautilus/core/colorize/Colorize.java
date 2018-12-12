package thiagodnf.nautilus.core.colorize;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.core.util.Normalizer;

public abstract class Colorize {
	
	public String getKey() {
		return Converter.toKey(getName());
	}
	
	public List<? extends Solution<?>> execute(List<? extends Solution<?>> solutions) {
		
		List<? extends Solution<?>> selectedSolutions = getSelectedSolutions(solutions);
		
//		for (Solution<?> solution : solutions) {
//			solution.getProperties().put("distance", calculate(solution, selectedSolutions));
//		}
		
		return solutions;
	}

	public List<Solution<?>> getSelectedSolutions(List<? extends Solution<?>> solutions) {

		List<Solution<?>> selectedSolutions = new ArrayList<>();

//		for (Solution sol : solutions) {
//
//			if (sol.isSelected()) {
//
//				selectedSolutions.add(sol);
//			}
//		}

		return selectedSolutions;
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

//		double feedback = closeSolution.getUserFeeback();
//		
		double distance = 0.0;
//		
//		if (feedback == 0) {
//			distance = minDistance;
//		} else if (feedback > 0) {
//			distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
//		} else {
//			distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
//		}
		
		return String.valueOf(distance);
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract double getDistance(Solution<?> s, Solution<?> selectedSolutions) ;
	
	public abstract String getName() ;
}
