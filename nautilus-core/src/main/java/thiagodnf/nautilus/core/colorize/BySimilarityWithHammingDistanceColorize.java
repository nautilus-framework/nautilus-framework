package thiagodnf.nautilus.core.colorize;

import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.SimilarityUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class BySimilarityWithHammingDistanceColorize extends AbstractColorize {

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		
		List<String> varS = SolutionUtils.getVariablesAsList(s);
		List<String> varSelected = SolutionUtils.getVariablesAsList(selected);
			
		int distance = SimilarityUtils.getHammingDistance(varS, varSelected);

		return ((double) distance / (double) varS.size());	
	}

	@Override
	public String getName() {
		return "By Similarity with Hamming Distance";
	}
}
