package thiagodnf.nautilus.core.colorize;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;

public class NoColorColorize extends Colorize {

	@Override
	public double getDistance(AbstractPlugin plugin, Solution s, Solution selected) {
		return 0.0;		
	}

	@Override
	public String getName() {
		return "No Color";
	}
}
