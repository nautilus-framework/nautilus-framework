package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.BruteForceSearch;
import org.nautilus.core.algorithm.Builder;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class BruteForceSearchAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new BruteForceSearch(builder);
	}

	@Override
	public String getName() {
		return "Brute-force Search";
	}
}
