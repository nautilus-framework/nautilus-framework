package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;

public class SolutionListUtils {
	
	public static List<NSolution<?>> getSelectedSolutions(List<NSolution<?>> solutions) {

		List<NSolution<?>> selectedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.SELECTED);

			if (value != null && (boolean) value == true) {
				selectedSolutions.add((NSolution<?>) sol.copy());
			}
		}

		return selectedSolutions;
	}

	public static List<NSolution<?>> getVisualizedSolutions(List<NSolution<?>> solutions) {

		List<NSolution<?>> visualizedSolutions = new ArrayList<>();

		for (NSolution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.VISUALIZED);

			if (value != null && (boolean) value == true) {
				visualizedSolutions.add((NSolution<?>) sol.copy());
			}
		}

		return visualizedSolutions;
	}
}
