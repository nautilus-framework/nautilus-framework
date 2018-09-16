package thiagodnf.nautilus.web.colorize;

import java.util.List;
import java.util.stream.Collectors;

import thiagodnf.nautilus.core.plugin.AbstractPlugin;
import thiagodnf.nautilus.web.model.Solution;

public class ByJaccardDistanceColorize extends Colorize {

	public ByJaccardDistanceColorize(AbstractPlugin plugin) {
		super(plugin);
	}

	@Override
	public double getDistance(Solution s, Solution selected) {
		
		List<String> variables1 = s.getVariables()
				.stream()
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		
		List<String> variables2 = selected.getVariables()
				.stream()
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		
		return 1.0 - plugin.getJaccardDistance(variables1, variables2);		
	}
}
