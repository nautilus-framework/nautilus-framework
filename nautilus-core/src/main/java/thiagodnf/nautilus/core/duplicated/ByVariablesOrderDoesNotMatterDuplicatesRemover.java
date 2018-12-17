package thiagodnf.nautilus.core.duplicated;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.SolutionUtils;

public class ByVariablesOrderDoesNotMatterDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "By Variables Values (Order Doesn't matter)";
	}

	@Override
	public boolean equals(Solution<?> s1, Solution<?> s2) {

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}

		List<String> varsS1 = SolutionUtils.getVariablesAsList(s1);
		List<String> varsS2 = SolutionUtils.getVariablesAsList(s2);

		if (varsS1.size() != varsS2.size()) {
			return false;
		}
		
		Map<String, Integer> mapS1 = new HashMap<>();
		Map<String, Integer> mapS2 = new HashMap<>();

		for (int i = 0; i < varsS1.size(); i++) {
			mapS1.merge(varsS1.get(i), 1, Integer::sum);
			mapS2.merge(varsS2.get(i), 1, Integer::sum);
		}

		for (Entry<String, Integer> entry : mapS1.entrySet()) {

			if (entry.getValue() != mapS2.get(entry.getKey())) {
				return false;
			}
		}

		return true;
	}
}
