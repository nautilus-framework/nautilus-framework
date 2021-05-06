package org.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.nautilus.plugin.extension.CrossoverExtension;
import org.nautilus.plugin.extension.crossover.IntegerSBXCrossoverExtension;
import org.nautilus.plugin.extension.crossover.SBXCrossoverExtension;
import org.nautilus.plugin.extension.crossover.SinglePointCrossoverExtension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

public class CrossoverFactory {
	
	private List<CrossoverExtension> extensions = new ArrayList<>();
	
	public CrossoverFactory() {
		extensions.add(new SinglePointCrossoverExtension());
		extensions.add(new IntegerSBXCrossoverExtension());
		extensions.add(new SBXCrossoverExtension());
	}

	public List<CrossoverExtension> getExtensions() {
		return extensions;
	}

	public CrossoverOperator<? extends Solution<?>> getCrossover(String crossoverId, double probability, double distributionIndex) {

		for (CrossoverExtension extension : getExtensions()) {

			if (crossoverId.equalsIgnoreCase(extension.getId())) {
				return extension.getCrossover(probability, distributionIndex);
			}
		}

		return null;
	}
}
