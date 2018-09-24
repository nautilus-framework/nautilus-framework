package thiagodnf.nautilus.core.distance;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.point.Point;

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
	
	public static double calculate(Front normalizedReferenceFront, Front normalizedFront) {

		double distance = 0.0;

		for (int i = 0; i < normalizedFront.getNumberOfPoints(); i++) {

			Point p1 = normalizedFront.getPoint(i);

			for (int j = 0; j < normalizedReferenceFront.getNumberOfPoints(); j++) {

				Point ref = normalizedReferenceFront.getPoint(j);

				List<Double> point1 = Arrays.stream(p1.getValues()).boxed().collect(Collectors.toList());
				List<Double> point2 = Arrays.stream(ref.getValues()).boxed().collect(Collectors.toList());

				distance += calculate(point1, point2);
			}
		}

		double total = normalizedReferenceFront.getNumberOfPoints() * normalizedFront.getNumberOfPoints();
		
		return (double) distance / (double) total;
	}
}
