package thiagodnf.nautilus.core.colorize;

import org.uma.jmetal.solution.Solution;

public class NoColorColorize extends Colorize {

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		return 0.0;		
	}

	@Override
	public String getName() {
		return "No Color";
	}
}
