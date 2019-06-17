package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.BruteForceSearch;
import thiagodnf.nautilus.core.algorithm.Builder;

@Extension
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
