package thiagodnf.nautilus.core.colorize;

import org.apache.commons.math3.ml.distance.EuclideanDistance;

import thiagodnf.nautilus.core.encoding.NSolution;

public class ByEuclideanDistanceColorize extends AbstractColorize{

	@Override
	public double getDistance(NSolution<?> s, NSolution<?> selected) {
		return new EuclideanDistance().compute(s.getObjectives(), selected.getObjectives());
	}

	@Override
	public String getName() {
		return "By Euclidean Distance";
	}
}
