package thiagodnf.nautilus.core.colorize;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.SimilarityUtils;

public class BySimilarityWithHammingDistanceColorize extends AbstractColorize {

	@Override
	public double getDistance(Solution<?> s, Solution<?> selected) {
		
		List<String> varS = new ArrayList<>();
		List<String> varSelected = new ArrayList<>();
		
		for (int i = 0; i < s.getNumberOfVariables(); i++) {
			varS.add(s.getVariableValueString(i));			
		}
		
		for (int i = 0; i < selected.getNumberOfVariables(); i++) {
			varSelected.add(selected.getVariableValueString(i));			
		}
			
		int distance = SimilarityUtils.getHammingDistance(varS, varSelected);

		return 1.0 - ((double) distance / (double) varS.size());	
	}

	@Override
	public String getName() {
		return "By Similarity with Hamming Distance";
	}
}
