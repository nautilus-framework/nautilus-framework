package thiagodnf.nautilus.core.colorize;

import thiagodnf.nautilus.core.encoding.NSolution;

public class DontColorize extends AbstractColorize {

	@Override
	public double getDistance(NSolution<?> s, NSolution<?> selected) {
		return 0.0;		
	}

	@Override
	public String getName() {
		return "Don't Colorize";
	}
}
