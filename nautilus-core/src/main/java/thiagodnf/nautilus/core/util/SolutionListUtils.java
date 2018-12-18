package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public class SolutionListUtils {

	public static List<Solution<?>> getSelectedSolutions(List<Solution<?>> solutions) {

		List<Solution<?>> selectedSolutions = new ArrayList<>();

		for (Solution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.SELECTED);

			if (value != null && (boolean) value == true) {
				selectedSolutions.add(sol.copy());
			}
		}

		return selectedSolutions;
	}
	
	public static List<Solution<?>> getVisualizedSolutions(List<Solution<?>> solutions) {

		List<Solution<?>> visualizedSolutions = new ArrayList<>();

		for (Solution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.VISUALIZED);

			if (value != null && (boolean) value == true) {
				visualizedSolutions.add(sol.copy());
			}
		}

		return visualizedSolutions;
	}
}
