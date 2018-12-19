package thiagodnf.nautilus.plugin.extension.crossover;

import org.pf4j.Extension;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.impl.crossover.SinglePointCrossover;
import org.uma.jmetal.problem.BinaryProblem;
import org.uma.jmetal.solution.Solution;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.CrossoverExtension;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;

@Extension
public class SinglePointCrossoverExtension implements CrossoverExtension {

	@Override
	public CrossoverOperator<? extends Solution<?>> getCrossover(double probability, double distributionIndex) {
		return new SinglePointCrossover(probability);
	}

	@Override
	public String getName() {
		return "Single Point Crossover";
	}

	@Override
	public String getId() {
		return Converter.toKey(getName());
	}

	@Override
	public boolean supports(ProblemExtension extension) {

		if (extension == null || extension.supports() == null) {
			return false;
		}

		if (BinaryProblem.class.equals(extension.supports())) {
			return true;
		}

		return false;
	}
}
