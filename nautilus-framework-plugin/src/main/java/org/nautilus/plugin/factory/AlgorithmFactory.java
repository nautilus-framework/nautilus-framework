package org.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.plugin.extension.AlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.BruteForceSearchAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.GAAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.NSGAIIAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.NSGAIIIAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.RNSGAIIAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.RandomSearchAlgorithmExtension;
import org.nautilus.plugin.extension.algorithm.SPEA2AlgorithmExtension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

public class AlgorithmFactory {

	private List<AlgorithmExtension> extensions = new ArrayList<>();

	public AlgorithmFactory() {
		getExtensions().add(new NSGAIIAlgorithmExtension());
		getExtensions().add(new SPEA2AlgorithmExtension());
		getExtensions().add(new RNSGAIIAlgorithmExtension());
		getExtensions().add(new NSGAIIIAlgorithmExtension());
		getExtensions().add(new BruteForceSearchAlgorithmExtension());
		getExtensions().add(new GAAlgorithmExtension());
		getExtensions().add(new RandomSearchAlgorithmExtension());
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
