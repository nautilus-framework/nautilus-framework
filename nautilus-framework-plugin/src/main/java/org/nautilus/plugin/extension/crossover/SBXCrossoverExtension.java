package org.nautilus.plugin.extension.crossover;

import org.pf4j.Extension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

@Extension
public class SBXCrossoverExtension extends AbstractCrossoverExtension {

	@Override
	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex) {
		return new SBXCrossover(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "SBX Crossover";
	}
	
	@Override
	public Class<? extends Solution<?>> supports() {
		return DoubleSolution.class;
	}
}
