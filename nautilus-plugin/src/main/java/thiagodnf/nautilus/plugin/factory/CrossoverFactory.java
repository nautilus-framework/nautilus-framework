package thiagodnf.nautilus.plugin.factory;

import java.util.ArrayList;
import java.util.List;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.crossover.IntegerSBXCrossoverExtension;
import thiagodnf.nautilus.plugin.extension.crossover.SinglePointCrossoverExtension;

public class CrossoverFactory {
	
	private List<CrossoverExtension> extensions = new ArrayList<>();
	
	public CrossoverFactory() {
		extensions.add(new SinglePointCrossoverExtension());
		extensions.add(new IntegerSBXCrossoverExtension());
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
