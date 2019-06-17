package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.SPEA2;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class SPEA2AlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new SPEA2(builder);
	}

	@Override
	public String getName() {
		return "SPEA2";
	}
}
