package thiagodnf.nautilus.core.duplicated;

import org.uma.jmetal.solution.Solution;

public class ByObjectivesDuplicatesRemover extends DuplicatesRemover {

	@Override
	public String getName() {
		return "by Objective Values";
	}

	@Override
	public boolean equals(Solution<?> s1, Solution<?> s2) {
		
		// It is false if they have different number of objectives
		if (s1.getNumberOfObjectives() != s2.getNumberOfObjectives()) {
			return false;
		}

		for (int i = 0; i < s1.getNumberOfObjectives(); i++) {

			if (s1.getObjective(i) != s2.getObjective(i)) {
				return false;
			}
		}

		return true;
	}
}
