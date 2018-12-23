package thiagodnf.nautilus.core.duplicated;

import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class ByVariablesOrderMattersDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "By Variables Values (Order matters)";
	}

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}

		List<String> varsS1 = SolutionUtils.getVariablesAsList(s1);
		List<String> varsS2 = SolutionUtils.getVariablesAsList(s2);

		if (varsS1.size() != varsS2.size()) {
			return false;
		}

		for (int i = 0; i < varsS1.size(); i++) {

			if (!varsS1.get(i).equalsIgnoreCase(varsS2.get(i))) {
				return false;
			}
		}

		return true;
	}
}
