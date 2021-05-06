package org.nautilus.plugin.extension;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.reduction.AbstractReduction;
import org.pf4j.ExtensionPoint;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

public interface AlgorithmExtension extends ExtensionPoint {

	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder);
	
	public AbstractReduction getReduction() ;
	
	public String getName();
	
	public String getId();
	
	public String toString();
}
