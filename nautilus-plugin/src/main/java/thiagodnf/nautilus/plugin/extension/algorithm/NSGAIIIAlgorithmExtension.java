package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.NSGAIII;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class NSGAIIIAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new NSGAIII(builder);
	}

	@Override
	public String getName() {
		return "NSGA-III";
	}
}
