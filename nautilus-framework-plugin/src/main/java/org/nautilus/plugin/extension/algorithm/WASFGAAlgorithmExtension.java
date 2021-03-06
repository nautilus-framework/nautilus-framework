package org.nautilus.plugin.extension.algorithm;

import org.nautilus.core.algorithm.Builder;
import org.nautilus.core.algorithm.WASFGA;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.Solution;

@SuppressWarnings({"rawtypes", "unchecked"})
public class WASFGAAlgorithmExtension extends AbstractAlgorithmExtension {

	@Override
	public Algorithm<? extends Solution<?>> getAlgorithm(Builder builder) {
		
//		PointSolution p1 = PointSolutionUtils.createSolution(-0.0, -0.0, -0.0, -0.0, -5.0, -0.0, -0.0, -0.0, -0.0, -0.5, -0.0, -0.0, -0.0, -0.0, -0.0);
		
		//PointSolution p1 = PointSolutionUtils.createSolution(0.0, -1.0, 0.0, -1.0, 0.0, 0.0); // MIP Problem
//		PointSolution p1 = PointSolutionUtils.createSolution(1.0, 0.0, 1.0, 1.0, 1.0, 0.0);
//		
//
//		builder.setReferencePoints(Arrays.asList(p1));
//		builder.setEpsilon(0.0001);
	    
	    
			
		return new WASFGA(builder);
	}

	@Override
	public String getName() {
		return "WASF-GA";
	}
}
