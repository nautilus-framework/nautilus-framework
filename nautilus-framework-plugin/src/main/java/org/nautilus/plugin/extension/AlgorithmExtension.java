package org.nautilus.plugin.extension;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.reduction.AbstractReduction;
import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

public interface AlgorithmExtension extends ExtensionPoint {

	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder);
	
	public AbstractReduction getReduction() ;
}
