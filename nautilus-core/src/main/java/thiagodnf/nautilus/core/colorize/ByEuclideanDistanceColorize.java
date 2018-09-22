package thiagodnf.nautilus.core.colorize;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;

public class ByEuclideanDistanceColorize extends Colorize{

	@Override
	public double getDistance(AbstractPlugin plugin, Solution s, Solution selected) {
		return EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());
	}

	@Override
	public String getName() {
		return "By Euclidean Distance";
	}

}
