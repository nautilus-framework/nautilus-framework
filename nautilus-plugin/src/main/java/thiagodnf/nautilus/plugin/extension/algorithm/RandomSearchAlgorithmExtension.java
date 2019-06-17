package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.RandomSearch;

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
