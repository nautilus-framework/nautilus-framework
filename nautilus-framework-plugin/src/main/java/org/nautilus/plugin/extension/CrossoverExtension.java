package org.nautilus.plugin.extension;

import org.nautilus.plugin.annotations.Extension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

public interface CrossoverExtension extends Extension {

	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex);
	
	public Class<? extends Solution<?>> supports();
		
	public String getName();
	
	public String getId();
}
