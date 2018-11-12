package thiagodnf.nautilus.plugin.provider.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.BruteForceSearch;
import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;

@Extension
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BruteForceSearchExtension implements AlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new BruteForceSearch(builder);
	}

	@Override
	public String toString() {
		return "Brute-force Search";
	}
}
