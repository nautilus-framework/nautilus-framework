package thiagodnf.nautilus.core.colorize;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.model.Solution;

public class ByEuclideanDistanceColorize extends Colorize{

	@Override
	public double getDistance(Solution s, Solution selected) {
		return EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());
	}

	@Override
	public String getName() {
		return "By Euclidean Distance";
	}

}
