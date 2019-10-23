package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.pf4j.Extension;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

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
