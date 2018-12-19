package thiagodnf.nautilus.plugin.extension.algorithm;

import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.core.algorithm.GA;
import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;

@Extension
@SuppressWarnings({"rawtypes", "unchecked"})
public class GAExtension implements AlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		return new GA(builder);
	}

	@Override
	public String getName() {
		return "GA";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
}
