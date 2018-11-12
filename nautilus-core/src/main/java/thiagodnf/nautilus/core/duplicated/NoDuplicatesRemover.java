package thiagodnf.nautilus.core.duplicated;

import thiagodnf.nautilus.core.model.Solution;

public class NoDuplicatesRemover extends DuplicatesRemover {

	@Override
	public String getName() {
		return "No Remove";
	}

	@Override
	public boolean equals(Solution s1, Solution s2) {
		return false;
	}
}
