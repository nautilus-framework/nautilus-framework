package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.NSGAII;
import org.nautilus.core.reduction.AbstractReduction;
import org.nautilus.core.reduction.ConfidenceBasedReduction;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings({"rawtypes", "unchecked"})
public class NSGAIIWithConfidenceBasedReductionAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new NSGAII(builder);
	}

	@Override
	public String getName() {
		return "COR-NSGA-II";
	}
	
	public AbstractReduction getReduction() {
        return new ConfidenceBasedReduction();
    }
}
