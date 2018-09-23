package thiagodnf.nautilus.core.colorize;

import java.util.List;
import java.util.stream.Collectors;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.plugin.AbstractPlugin;

public class BySimilarityColorize extends Colorize {

	@Override
	public double getDistance(AbstractPlugin plugin, Solution s, Solution selected) {
		
		List<String> variables1 = s.getVariables()
				.stream()
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		
		List<String> variables2 = selected.getVariables()
				.stream()
				.map(e -> e.getValue())
				.collect(Collectors.toList());
		
		return 1.0 - plugin.getSimilarityDistance(variables1, variables2);		
	}

	@Override
	public String getName() {
		return "By Similarity";
	}
}
