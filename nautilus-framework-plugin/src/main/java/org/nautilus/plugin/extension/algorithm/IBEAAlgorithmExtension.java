package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.IBEA;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings("unchecked")
public class IBEAAlgorithmExtension extends AbstractAlgorithmExtension {

	
    @Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
	    return new IBEA(builder);	    
	}

	@Override
	public String getName() {
		return "IBEA";
	}
}
