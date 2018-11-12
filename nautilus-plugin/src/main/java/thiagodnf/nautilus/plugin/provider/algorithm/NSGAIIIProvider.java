package thiagodnf.nautilus.plugin.provider.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.NSGAIII;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class NSGAIIIProvider implements AlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new NSGAIII(builder);
	}

	@Override
	public String toString() {
		return "NSGA-III";
	}
}
