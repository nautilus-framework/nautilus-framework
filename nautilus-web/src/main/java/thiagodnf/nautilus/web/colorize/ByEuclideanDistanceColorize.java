package thiagodnf.nautilus.web.colorize;

import thiagodnf.nautilus.core.distance.EuclideanDistance;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.model.Solution;

public class ByEuclideanDistanceColorize extends Colorize{

	public ByEuclideanDistanceColorize(AbstractPlugin plugin) {
		super(plugin);
	}

	@Override
	public double getDistance(Solution s, Solution selected) {
		
		return EuclideanDistance.calculate(s.getObjectives(), selected.getObjectives());
	}

}
