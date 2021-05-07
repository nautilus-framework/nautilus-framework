package org.nautilus.plugin.extension;

import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

public interface CrossoverExtension extends ExtensionPoint {

	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex);
	
	public Class<? extends Solution<?>> supports();
}
