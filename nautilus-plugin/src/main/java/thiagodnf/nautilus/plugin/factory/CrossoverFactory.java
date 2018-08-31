package thiagodnf.nautilus.plugin.factory;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.solution.Solution;

public class CrossoverFactory {

	@SuppressWarnings("rawtypes")
	public static<T extends Solution<?>> CrossoverOperator getCrossover(String name, double probability, double distributionIndex) {

		if (name.equalsIgnoreCase("IntegerSBXCrossover")) {
			return new IntegerSBXCrossover(probability, distributionIndex);
		} else {
			throw new IllegalArgumentException("Invalid Crossover Name");
		}
	}
}
