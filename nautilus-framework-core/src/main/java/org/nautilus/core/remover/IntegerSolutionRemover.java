package org.nautilus.core.remover;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nautilus.core.encoding.NSolution;
import org.nautilus.core.util.SolutionUtils;

public class IntegerSolutionRemover extends AbstractRemover {

	@Override
	public boolean equals(NSolution<?> s1, NSolution<?> s2) {

		if (s1.getNumberOfVariables() != s2.getNumberOfVariables()) {
			return false;
		}

		List<String> varsS1 = SolutionUtils.getVariablesAsList(s1);
		List<String> varsS2 = SolutionUtils.getVariablesAsList(s2);

		if (varsS1.size() != varsS2.size()) {
			return false;
		}
		
		Map<String, Integer> mapS1 = new HashMap<>();
		Map<String, Integer> mapS2 = new HashMap<>();

		for (int i = 0; i < varsS1.size(); i++) {
			mapS1.merge(varsS1.get(i), 1, Integer::sum);
			mapS2.merge(varsS2.get(i), 1, Integer::sum);
		}

		for (Entry<String, Integer> entry : mapS1.entrySet()) {

			if (entry.getValue() != mapS2.get(entry.getKey())) {
				return false;
			}
		}

		return true;
	}
}
