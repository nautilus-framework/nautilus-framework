package thiagodnf.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.provider.algorithm.BruteForceSearchExtension;
import thiagodnf.nautilus.plugin.provider.algorithm.NSGAIIIProvider;
import thiagodnf.nautilus.plugin.provider.algorithm.NSGAIIProvider;
import thiagodnf.nautilus.plugin.provider.algorithm.RNSGAIIProvider;
import thiagodnf.nautilus.plugin.provider.algorithm.SPEA2Provider;

public class AlgorithmFactory {
	
	private List<AlgorithmExtension> extensions = new ArrayList<>();
	
	public AlgorithmFactory() {
		getExtensions().add(new NSGAIIProvider());
		getExtensions().add(new SPEA2Provider());
		getExtensions().add(new RNSGAIIProvider());
		getExtensions().add(new NSGAIIIProvider());
		getExtensions().add(new BruteForceSearchExtension());
	}

	public List<AlgorithmExtension> getExtensions() {
		return extensions;
	}

	public Algorithm<? extends Solution<?>> getAlgorithm(String name, Builder builder) {

		for (AlgorithmExtension extension : getExtensions()) {

			if (name.equalsIgnoreCase(extension.toString())) {
				return extension.getAlgorithm(builder);
			}
		}

		return null;
	}
}
