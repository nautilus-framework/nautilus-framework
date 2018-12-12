package thiagodnf.nautilus.core.colorize;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.distance.JaccardDistance;

public class BySimilarityColorize extends Colorize {

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		
//		List<String> variables1 = s.getVariables()
//				.stream()
//				.map(e -> e.getValue())
//				.collect(Collectors.toList());
//		
//		List<String> variables2 = selected.getVariables()
//				.stream()
//				.map(e -> e.getValue())
//				.collect(Collectors.toList());
		
		return 1.0 - JaccardDistance.calculate(null, null);		
	}

	@Override
	public String getName() {
		return "By Similarity";
	}
}
