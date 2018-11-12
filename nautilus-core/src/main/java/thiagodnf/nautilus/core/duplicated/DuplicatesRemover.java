package thiagodnf.nautilus.core.duplicated;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.util.Converter;

public abstract class DuplicatesRemover {

	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public List<Solution> execute(List<Solution> solutions) {
		
		List<Solution> nonRepeated = new ArrayList<>();

		for (Solution solution : solutions) {

			if (!contains(nonRepeated, solution)) {
				nonRepeated.add(solution.copy());
			}
		}

		return nonRepeated;
	}
	
	public boolean contains(List<Solution> solutions, Solution s2) {

		for (Solution s1 : solutions) {

			if (equals(s1, s2)) {
				return true;
			}
		}

		return false;
	}
	
	public abstract boolean equals(Solution s1, Solution s2);
	
	public abstract String getName() ;
}
