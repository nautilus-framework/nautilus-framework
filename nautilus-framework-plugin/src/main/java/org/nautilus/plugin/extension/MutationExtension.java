package org.nautilus.plugin.extension;

import org.nautilus.plugin.annotations.ExtensionPoint;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

public interface MutationExtension extends ExtensionPoint {

	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex);
	
	public Class<? extends Solution<?>> supports();
}
