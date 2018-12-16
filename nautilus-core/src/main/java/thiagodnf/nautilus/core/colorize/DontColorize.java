package thiagodnf.nautilus.core.colorize;

import org.uma.jmetal.solution.Solution;

public class DontColorize extends AbstractColorize {

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		return 0.0;		
	}

	@Override
	public String getName() {
		return "Don't Colorize";
	}
}
