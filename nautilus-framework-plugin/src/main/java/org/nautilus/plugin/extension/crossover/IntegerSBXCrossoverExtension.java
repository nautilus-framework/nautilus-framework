package org.nautilus.plugin.extension.crossover;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;

public class IntegerSBXCrossoverExtension extends AbstractCrossoverExtension {

	@Override
	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex) {
		return new IntegerSBXCrossover(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Integer SBX Crossover";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return IntegerSolution.class;
	}
}
