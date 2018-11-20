package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

public class SolutionListUtils {

	public static List<Solution<?>> getNondominatedSolutions(List<List<Solution<?>>> paretoFronts) {

		List<Solution<?>> pfknown = new ArrayList<>();

		for (List<Solution<?>> solutions : paretoFronts) {
			pfknown.addAll(solutions);
		}

		return org.uma.jmetal.util.SolutionListUtils.getNondominatedSolutions(pfknown);
	}
}
