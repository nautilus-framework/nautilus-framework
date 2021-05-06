package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.NSGAII;
import org.nautilus.core.reduction.AbstractReduction;
import org.nautilus.core.reduction.RandomReduction;
import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class NSGAIIWithRandomReductionAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new NSGAII(builder);
	}

	@Override
	public String getName() {
		return "NSGA-II with Random Reduction";
	}
	
	public AbstractReduction getReduction() {
        return new RandomReduction();
    }
}
