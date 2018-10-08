package thiagodnf.nautilus.core.colorize;

import thiagodnf.nautilus.core.model.Solution;

public class NoColorColorize extends Colorize {

	@Override
	public double getDistance(Solution s, Solution selected) {
		return 0.0;		
	}

	@Override
	public String getName() {
		return "No Color";
	}
}
