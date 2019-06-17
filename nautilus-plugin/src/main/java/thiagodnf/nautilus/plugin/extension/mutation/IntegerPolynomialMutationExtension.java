package thiagodnf.nautilus.plugin.extension.mutation;

import org.pf4j.Extension;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.solution.Solution;

@Extension
public class IntegerPolynomialMutationExtension extends AbstractMutationExtension {

	@Override
	public MutationOperator<? extends Solution<?>> getMutation(double probability, double distributionIndex) {
		return new IntegerPolynomialMutation(probability, distributionIndex);
	}

	@Override
	public String getName() {
		return "Integer Polynomial Mutation";
	}

	@Override
	public Class<? extends Solution<?>> supports() {
		return IntegerSolution.class;
	}
}
