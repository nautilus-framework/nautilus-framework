package thiagodnf.nautilus.core.duplicated;

import org.uma.jmetal.solution.Solution;

public class DontDuplicatesRemover extends AbstractDuplicatesRemover {

	@Override
	public String getName() {
		return "Don't Remove";
	}

	@Override
	public boolean equals(Solution<?> s1, Solution<?> s2) {
		return false;
	}
}
