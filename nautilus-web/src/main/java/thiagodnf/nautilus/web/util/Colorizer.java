package thiagodnf.nautilus.web.util;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.util.Normalizer;
import thiagodnf.nautilus.web.model.Solution;

public class Colorizer {
	
	public static List<Solution> execute(List<Solution> solutions) {
		
		List<Solution> selectedSolutions = getSelectedSolutions(solutions);
		
		for (Solution solution : solutions) {
			solution.getProperties().put("distance", getDistance(solution, selectedSolutions));
		}
		
		return solutions;
	}

	public static List<Solution> getSelectedSolutions(List<Solution> solutions) {

		List<Solution> selectedSolutions = new ArrayList<>();

		for (Solution sol : solutions) {

			if (sol.isSelected()) {

				selectedSolutions.add(sol);
			}
		}

		return selectedSolutions;
	}
	
	public static String getDistance(Solution s, List<Solution> selectedSolutions) {
		
		if (selectedSolutions.isEmpty()) {
			return "0.0";
		}

		double minDistance = Double.MAX_VALUE;

		Solution closeSolution = null;

		for (Solution selected : selectedSolutions) {

			double distance = EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());

			if (distance < minDistance) {
				minDistance = distance;
				closeSolution = selected;
			}
		}
		
		minDistance = Normalizer.normalize(minDistance, 0, Math.sqrt(2));

		double feedback = closeSolution.getUserFeeback();
		
		double distance = 0.0;
		
		if(feedback == 0) {
			distance = 0.0;
		}else if (feedback > 0) {
			distance = Math.pow(minDistance, 1.0 / Math.abs(feedback));
		}else {
			distance = Math.pow(1.0 - minDistance, 1.0 / Math.abs(feedback));
		}
		
		return String.valueOf(distance);
	}
}
