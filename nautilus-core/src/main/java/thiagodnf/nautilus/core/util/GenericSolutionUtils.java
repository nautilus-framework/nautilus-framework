package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import thiagodnf.nautilus.core.model.GenericSolution;

public class GenericSolutionUtils {
	
	public static GenericSolution clearUserFeedback(GenericSolution solution) {

		List<String> keysToRemove = new ArrayList<>();

		for (Entry<Object, Object> entry : solution.getAttributes().entrySet()) {

			String key = entry.getKey().toString();

			if (key.startsWith(SolutionAttribute.FEEDBACK_FOR_VARIABLE)) {
				keysToRemove.add(key);
			}
		}

		for (String key : keysToRemove) {
			solution.getAttributes().remove(key);
		}

		solution.getAttributes().remove(SolutionAttribute.SELECTED);
		
		return solution;
	}
}
