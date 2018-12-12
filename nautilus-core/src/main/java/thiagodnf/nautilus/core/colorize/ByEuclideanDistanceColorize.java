package thiagodnf.nautilus.core.colorize;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.distance.EuclideanDistance;

public class ByEuclideanDistanceColorize extends Colorize{

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		//return EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());
		return 1.0;
	}

	@Override
	public String getName() {
		return "By Euclidean Distance";
	}

}
