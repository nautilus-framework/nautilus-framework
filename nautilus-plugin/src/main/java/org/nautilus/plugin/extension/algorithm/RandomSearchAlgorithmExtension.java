package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.RandomSearch;
import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@Extension
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RandomSearchAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new RandomSearch(builder);
	}

	@Override
	public String getName() {
		return "Random Search";
	}
}
