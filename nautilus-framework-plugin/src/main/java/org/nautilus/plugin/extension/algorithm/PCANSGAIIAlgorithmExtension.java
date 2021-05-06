package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.pcansgaii.PCANSGAII;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings({"rawtypes", "unchecked"})
public class PCANSGAIIAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new PCANSGAII(builder);
	}

	@Override
	public String getName() {
		return "PCA-NSGA-II";
	}
}
