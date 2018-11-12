package thiagodnf.nautilus.core.duplicated;

import java.util.Comparator;

import thiagodnf.nautilus.core.comparator.BinarySolutionComparator;
import thiagodnf.nautilus.core.comparator.IntegerSolutionComparator;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.solution.BinarySolution;
import thiagodnf.nautilus.core.solution.IntegerSolution;

public class ByVariablesDuplicatesRemover extends DuplicatesRemover {

	@Override
	public String getName() {
		return "by Variables Values";
	}

	@Override
	public boolean equals(Solution s1, Solution s2) {
		
		// It is false if both solutions have different solution types
		if (!s1.getType().equalsIgnoreCase(s2.getType())) {
			return false;
		}

		Comparator<Solution> comparator = null;

		if (s1.getType().equalsIgnoreCase(BinarySolution.class.getName())) {
			comparator = new BinarySolutionComparator();
		} else if (s1.getType().equalsIgnoreCase(IntegerSolution.class.getName())) {
			comparator = new IntegerSolutionComparator();
		}

		if (comparator.compare(s1, s2) == 0) {
			return false;
		}

		return true;
	}
}
