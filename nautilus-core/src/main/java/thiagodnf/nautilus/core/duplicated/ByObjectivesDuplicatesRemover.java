package thiagodnf.nautilus.core.duplicated;

import thiagodnf.nautilus.core.encoding.NSolution;

public class ByObjectivesDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "By Objective Values";
	}

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {
		
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
