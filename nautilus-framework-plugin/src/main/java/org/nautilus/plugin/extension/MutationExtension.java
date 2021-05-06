package org.nautilus.plugin.extension;

import org.nautilus.plugin.annotations.Extension;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.solution.Solution;

public interface MutationExtension extends Extension {

	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex);
	
	public String getName();
	
	public String getId();
	
	public Class<? extends Solution<?>> supports();
}
