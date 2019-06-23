package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.NSGAII;
import thiagodnf.nautilus.core.reduction.AbstractReduction;
import thiagodnf.nautilus.core.reduction.ConfidenceBasedReduction;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class NSGAIIWithConfidenceBasedReductionAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new NSGAII(builder);
	}

	@Override
	public String getName() {
		return "NSGA-II with Confidence-based Reduction";
	}
	
	public AbstractReduction getReduction() {
        return new ConfidenceBasedReduction();
    }
}
