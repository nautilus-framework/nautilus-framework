package thiagodnf.nautilus.core.duplicated;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.Converter;

public abstract class AbstractDuplicatesRemover {

	public List<NSolution<?>> execute(List<NSolution<?>> solutions) {
		
		List<NSolution<?>> nonRepeated = new ArrayList<>();

		for (NSolution<?> solution : solutions) {

			if (!contains(nonRepeated, solution)) {
				nonRepeated.add((NSolution<?>) solution.copy());
			}
		}

		return nonRepeated;
	}
	
	public boolean contains(List<NSolution<?>> solutions, NSolution<?> s2) {

		for (NSolution<?> s1 : solutions) {

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
	
	public abstract boolean equals(NSolution<?> s1, NSolution<?> s2);
	
	public abstract String getName() ;
}
