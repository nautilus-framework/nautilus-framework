package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;

@Extension
public class ManuallyExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return null;
	}

	@Override
	public String getName() {
		return "Manually";
	}
}
