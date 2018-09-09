package thiagodnf.nautilus.core.distance;

import java.util.Collections;
import java.util.List;

public class EuclideanDistance {

	public static double calculate(List<Double> s1, List<Double> s2) {
		return calculate(Collections.nCopies(s1.size(), 1.0), s1, s2);
	}

	public static double calculate(List<Double> weights, List<Double> s1, List<Double> s2) {

		double sum = 0.0;

		for (int i = 0; i < s1.size(); i++) {
			sum += weights.get(i) * (Math.pow(s1.get(i) - s2.get(i), 2));
		}

		return Math.sqrt(sum);
	}
}
