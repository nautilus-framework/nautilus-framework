package thiagodnf.nautilus.core.factory;

import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.solution.Solution;

public class CrossoverFactory {

	@SuppressWarnings("rawtypes")
	public static<T extends Solution<?>> CrossoverOperator getCrossover(String name, double probability, double distributionIndex) {

		if (name.equalsIgnoreCase("IntegerSBXCrossover")) {
			return new IntegerSBXCrossover(probability, distributionIndex);
		} else if (name.equalsIgnoreCase("SBXCrossover")) {
			return new SBXCrossover(probability, distributionIndex);
		} else {
			throw new IllegalArgumentException("Invalid Crossover Name");
		}
	}
}
