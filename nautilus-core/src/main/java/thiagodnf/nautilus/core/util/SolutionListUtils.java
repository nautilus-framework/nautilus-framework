package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public class SolutionListUtils {

	public static List<? extends Solution<?>> getSelectedSolutions(List<? extends Solution<?>> solutions) {

		List<Solution<?>> selectedSolutions = new ArrayList<>();

		for (Solution<?> sol : solutions) {
			
			Object value = sol.getAttribute(SolutionAttribute.SELECTED);

			if (value != null && (boolean) value == true) {

				selectedSolutions.add(sol.copy());
			}
		}

		return selectedSolutions;
	}
}
