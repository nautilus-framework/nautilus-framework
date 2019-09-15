package thiagodnf.nautilus.core.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import org.uma.jmetal.solution.Solution;

import com.google.common.base.Preconditions;

import thiagodnf.nautilus.core.encoding.NSolution;

public class SolutionUtils {
	
	public static double getUserFeedback(NSolution<?> solution) {

		double sum = 0.0;
		double total = 0.0;

		for (Entry<Object, Object> entry : solution.getAttributes().entrySet()) {

			String key = entry.getKey().toString();

			if (key.startsWith(SolutionAttribute.FEEDBACK_FOR_VARIABLE)) {
				sum += (Double) entry.getValue();
				total++;
			}
		}

		if (total == 0.0) {
			return 0.0;
		}

		return sum / total;
	}
	
	public static NSolution<?> clearUserFeedback(NSolution<?> solution) {

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
		solution.getAttributes().remove(SolutionAttribute.FEEDBACK);

		return solution;
	}

	public static List<String> getVariablesAsList(Solution<?> solution) {

		List<String> variables = new ArrayList<>();

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			variables.addAll(VariableUtils.getVariableAsList(solution.getVariableValue(i)));
		}

		return variables;
	}
	
    public static Date getSelectedDate(Solution<?> solution) {

        Preconditions.checkNotNull(solution, "The solution must not be null");

        return (Date) solution.getAttribute(SolutionAttribute.SELECTED_DATE);
    }

    public static boolean isSelected(Solution<?> solution) {
        return getSelectedDate(solution) != null;
    }
}
