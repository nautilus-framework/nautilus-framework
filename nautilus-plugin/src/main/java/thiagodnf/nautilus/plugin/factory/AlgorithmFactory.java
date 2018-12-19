package thiagodnf.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.algorithm.Builder;
import thiagodnf.nautilus.plugin.extension.AlgorithmExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.BruteForceSearchExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.GAExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.NSGAIIExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.NSGAIIIExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.RNSGAIIExtension;
import thiagodnf.nautilus.plugin.extension.algorithm.SPEA2Extension;

public class AlgorithmFactory {

	private List<AlgorithmExtension> extensions = new ArrayList<>();

	public AlgorithmFactory() {
		getExtensions().add(new NSGAIIExtension());
		getExtensions().add(new SPEA2Extension());
		getExtensions().add(new RNSGAIIExtension());
		getExtensions().add(new NSGAIIIExtension());
		getExtensions().add(new BruteForceSearchExtension());
		getExtensions().add(new GAExtension());
	}

	public List<AlgorithmExtension> getExtensions() {
		return extensions;
	}

	public Algorithm<? extends Solution<?>> getAlgorithm(String name, Builder builder) {

		for (AlgorithmExtension extension : getExtensions()) {

			if (name.equalsIgnoreCase(extension.getId())) {
				return extension.getAlgorithm(builder);
			}
		}

		return null;
	}
}
