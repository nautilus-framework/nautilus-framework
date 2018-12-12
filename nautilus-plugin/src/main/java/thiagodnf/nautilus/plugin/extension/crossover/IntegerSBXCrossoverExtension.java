package thiagodnf.nautilus.plugin.extension.crossover;

import org.pf4j.Extension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;

@Extension
public class IntegerSBXCrossoverExtension implements CrossoverExtension {

	@Override
	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex) {
		return new IntegerSBXCrossover(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Integer SBX Crossover";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}
}
