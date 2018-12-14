package thiagodnf.nautilus.core.duplicated;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractDuplicatesRemover {

	public List<Solution<?>> execute(List<? extends Solution<?>> solutions) {
		
		List<Solution<?>> nonRepeated = new ArrayList<>();

		for (Solution<?> solution : solutions) {

			if (!contains(nonRepeated, solution)) {
				nonRepeated.add(solution.copy());
			}
		}

		return nonRepeated;
	}
	
	public boolean contains(List<? extends Solution<?>> solutions, Solution<?> s2) {

		for (Solution<?> s1 : solutions) {

			if (equals(s1, s2)) {
				return true;
			}
		}

		return false;
	}
	
	public String getId() {
		return Converter.toKey(getName());
	}
	
	public String toString() {
		return getName();
	}
	
	public abstract boolean equals(Solution<?> s1, Solution<?> s2);
	
	public abstract String getName() ;
}
