package thiagodnf.nautilus.plugin.extension.crossover;

import org.pf4j.Extension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;

@Extension
public class SinglePointCrossoverExtension extends AbstractCrossoverExtension {

	@Override
	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex) {
		return new SinglePointCrossover(probability);
	}

	@Override
	public String getName() {
		return "Single Point Crossover";
	}
	
	@Override
	public Class<? extends Solution<?>> supports() {
		return BinarySolution.class;
	}
}
