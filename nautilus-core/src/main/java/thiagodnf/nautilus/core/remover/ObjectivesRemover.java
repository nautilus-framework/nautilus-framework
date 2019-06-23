package thiagodnf.nautilus.core.remover;

import thiagodnf.nautilus.core.encoding.NSolution;

public class ObjectivesRemover extends AbstractRemover {

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
