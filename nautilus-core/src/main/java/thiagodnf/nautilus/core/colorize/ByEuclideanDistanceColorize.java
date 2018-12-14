package thiagodnf.nautilus.core.colorize;

import org.apache.commons.math3.ml.distance.EuclideanDistance;
import org.uma.jmetal.solution.Solution;

public class ByEuclideanDistanceColorize extends AbstractColorize{

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		return new EuclideanDistance().compute(s.getObjectives(), selected.getObjectives());
	}

	@Override
	public String getName() {
		return "By Euclidean Distance";
	}
}
