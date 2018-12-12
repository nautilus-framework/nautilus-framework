package thiagodnf.nautilus.core.duplicated;

import org.uma.jmetal.solution.Solution;

public class NoDuplicatesRemover extends DuplicatesRemover {

	@Override
	public String getName() {
		return "No Remove";
	}

	@Override
	public boolean equals(Solution<?> s1, Solution<?> s2) {
		return false;
	}
}
