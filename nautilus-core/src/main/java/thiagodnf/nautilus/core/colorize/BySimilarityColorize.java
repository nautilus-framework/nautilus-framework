package thiagodnf.nautilus.core.colorize;

import java.util.List;
import java.util.stream.Collectors;

import thiagodnf.nautilus.core.distance.JaccardDistance;
import thiagodnf.nautilus.core.model.Solution;

public class BySimilarityColorize extends Colorize {

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
		
		return 1.0 - JaccardDistance.calculate(variables1, variables2);		
	}

	@Override
	public String getName() {
		return "By Similarity";
	}
}
