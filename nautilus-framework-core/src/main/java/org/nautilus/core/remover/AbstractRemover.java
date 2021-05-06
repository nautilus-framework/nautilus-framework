package org.nautilus.core.remover;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.util.Converter;

public abstract class AbstractRemover {

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
	
	public String toString() {
		return Converter.toJson(this);
	}
	
	public abstract boolean equals(NSolution<?> s1, NSolution<?> s2);
}
