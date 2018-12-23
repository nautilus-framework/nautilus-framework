package thiagodnf.nautilus.core.colorize;

import java.util.List;

import thiagodnf.nautilus.core.encoding.NSolution;
import thiagodnf.nautilus.core.util.SimilarityUtils;
import thiagodnf.nautilus.core.util.SolutionUtils;

public class BySimilarityWithJaccardIndexColorize extends AbstractColorize {

	@Override
	public double getDistance(NSolution<?> s, NSolution<?> selected) {
		
		List<String> varS = SolutionUtils.getVariablesAsList(s);
		List<String> varSelected = SolutionUtils.getVariablesAsList(selected);
		
		return 1.0 - SimilarityUtils.getJaccardIndex(varS, varSelected);		
	}

	@Override
	public String getName() {
		return "By Similarity with Jaccard Index";
	}
}
