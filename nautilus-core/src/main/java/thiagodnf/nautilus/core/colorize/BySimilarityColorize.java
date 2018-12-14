package thiagodnf.nautilus.core.colorize;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.distance.JaccardD;

public class BySimilarityColorize extends AbstractColorize {

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

		
		return 1.0 - JaccardD.calculate(null, null);		
	}

	@Override
	public String getName() {
		return "By Similarity with Jaccard Distance";
	}
}
